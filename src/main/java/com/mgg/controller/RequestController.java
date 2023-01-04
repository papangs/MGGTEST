package com.mgg.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mgg.constant.ConstantCode;
import com.mgg.constant.ConstantValidate;
import com.mgg.model.inquiry.ReqInquiry;
import com.mgg.model.inquiry.ResInquiry;
import com.mgg.model.inquiry.ResInquiryAdditionalInfo;
import com.mgg.model.inquiry.ResInquiryTotalAmount;
import com.mgg.model.inquiry.ResInquiryVirtualAccountData;
import com.mgg.model.jwt.ReqJwt;
import com.mgg.model.jwt.ResJwt;
import com.mgg.model.master.MHost;
import com.mgg.process.inquiry.ProcessInquiry;
import com.mgg.security.JwtTokenUtil;
import com.mgg.service.MHostMapper;
import com.mgg.service.MparamMapper;
import com.mgg.service.TbTransMapper;
import com.mgg.service.impl.JwtUserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
@RestController
@RequestMapping("/mgg")
public class RequestController {

	Logger logger = LoggerFactory.getLogger(RequestController.class);

	@Autowired
	private MHostMapper mHostMapper;

	@Autowired
	private MparamMapper mParamMapper;

	@Autowired
	private TbTransMapper tbTransMapper;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	@Autowired
	private JwtUserDetailsServiceImpl jwtUserDetailsService;

	@Autowired
	private ProcessInquiry processInquiry;

	@Value("${server.port}")
	private String serverPort;

	ObjectMapper mapper = new ObjectMapper();
	Gson gson = new Gson();

	// ========================================== REQUEST =====================================================

	@RequestMapping(value = "/token", method = RequestMethod.POST)
	private ResponseEntity<?> getToken(@RequestBody String body) throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		String responseBody = "";

		logger.info("[token-mgg] Get Token Request." + body);

		ReqJwt reqJwt = mapper.readValue(body, ReqJwt.class);

		// TOKEN JWT
		String decodedString = "";
		try {
			String originalInput = reqJwt.getParam();
//			String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
			byte[] decodedBytes = Base64.getDecoder().decode(originalInput);
			decodedString = new String(decodedBytes);

			logger.info("[token-mgg] GrantType is : " + decodedString);

			authenticate(decodedString.split("\\:")[0], decodedString.split("\\:")[1]);
		} catch (DisabledException e) {
			responseBody = gson.toJson(new ResJwt(ConstantCode.ErrCode73, ConstantCode.ErrCode73Desc));
			return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body(responseBody);
		} catch (BadCredentialsException e) {
			responseBody = gson.toJson(new ResJwt(ConstantCode.ErrCode73, ConstantCode.ErrCode73Desc));
			return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body(responseBody);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[token-mgg] :" + e.toString());
			printTrace(e);
			responseBody = gson.toJson(new ResJwt(ConstantCode.ErrCode73, ConstantCode.ErrCode73Desc));
			return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body(responseBody);
		}

		final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(decodedString.split("\\:")[0]);
		final String token = jwtTokenUtil.generateToken(userDetails);

		logger.info("[token-mgg] TOKEN : " + token);

		DecodedJWT jwt = JWT.decode(token);
		long jwtExp = jwt.getExpiresAt().getTime();
		logger.info("[token-mgg] : jwtExp : " + jwtExp);
		Date now = new Date();
		long timeNow = now.getTime();
		logger.info("[token-mgg] : timeNow : " + timeNow);
		long exp = (jwtExp - timeNow) / 1000;
		logger.info("[token-mgg] : Expired : " + exp);

		responseBody = gson.toJson(new ResJwt(token, "BearerToken", String.valueOf(exp)));
		return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(responseBody);
		// TOKEN JWT
	}

	@RequestMapping(value = "/inquiry", method = RequestMethod.POST)
	private ResponseEntity<?> getInquiry(@RequestHeader HttpHeaders headers, @RequestBody String body) {

		MDC.put("tid", generateUniqKey());

		String responseBody = "";

		logger.info("[inq-mgg] Authorization header : " + headers.get("Authorization").get(0));

		ReqInquiry reqInquiry = new ReqInquiry();
		ResInquiry resInquiry = new ResInquiry();
		ResInquiryVirtualAccountData accountData = new ResInquiryVirtualAccountData();

		// TOKEN
		try {
			authenticate(headers.get("Authorization").get(0));
		} catch (Exception e) {
			logger.error("[inq-mgg] :" + e.toString());
			printTrace(e);
			responseBody = gson.toJson(new ResInquiry(ConstantCode.ErrCode24, ConstantCode.ErrCode24Desc));
			return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body(responseBody);
		}
		// TOKEN

		logger.info("[inq-mgg] Request Start.");
		logger.info("[inq-mgg] Inquiry Params Request : " + body);
		logger.info("[inq-mgg] Check Server Port" + serverPort);

		try {

			logger.info("[inq-mgg] Send to client Start.");

			reqInquiry = mapper.readValue(body, ReqInquiry.class);

			HashMap<String, Object> proses = processInquiry.sendToProcess(reqInquiry);

			logger.info("[inq-mgg] RESPONSE : " + mapper.writeValueAsString(proses));

			if (proses.get("resultCd").equals(ConstantValidate.valCoded00)) {
				HashMap<String, Object> data = mapper.readValue(proses.get("data_pelanggan").toString(), HashMap.class);
				accountData = new ResInquiryVirtualAccountData(reqInquiry.getRetnum(), reqInquiry.getTrx_date(),
						reqInquiry.getTrx_time(), data.get("custno").toString(), data.get("custname").toString(),
						data.get("custaddr").toString(), new ResInquiryTotalAmount(data.get("total").toString(), "IDR"),
						new ResInquiryAdditionalInfo(proses.get("trxid").toString()));
				responseBody = gson.toJson(new ResInquiry(proses.get("resultCd").toString(),
						proses.get("resultMsg").toString(), accountData));
				return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(responseBody);
			} else {
				responseBody = gson
						.toJson(new ResInquiry(proses.get("resultCd").toString(), proses.get("resultMsg").toString()));
				return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(responseBody);
			}

		} catch (RestClientException e) {

			if (e.getCause() instanceof ConnectException) {
				logger.error("[inq-mgg] Could not connect to client.");
				responseBody = gson.toJson(new ResInquiry(ConstantCode.ErrCode86, ConstantCode.ErrCode86Desc));
				return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON).body(responseBody);
			} else if (e.getCause() instanceof UnknownHostException) {
				logger.error("[inq-mgg] Unknow client host.");
				responseBody = gson.toJson(new ResInquiry(ConstantCode.ErrCode86, ConstantCode.ErrCode86Desc));
				return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON).body(responseBody);
			} else if (e.getCause() instanceof SocketTimeoutException) {
				logger.error("[inq-mgg] Read Timeout to client.");
				responseBody = gson.toJson(new ResInquiry(ConstantCode.ErrCode86, ConstantCode.ErrCode86Desc));
				return ResponseEntity.status(504).contentType(MediaType.APPLICATION_JSON).body(responseBody);
			}

			throw e; // rethrow if not a ConnectException

		} catch (Exception e) {

			logger.info("[inq-mgg] Unknown Error.");
			e.printStackTrace();
			logger.error("[inq-mgg] " + e.toString());
			printTrace(e);
			responseBody = gson.toJson(new ResInquiry(ConstantCode.ErrCode90, ConstantCode.ErrCode90Desc));
			return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON).body(responseBody);
		}
	}

	// ========================================== FUNCTION ==========================================================

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		} catch (Exception e) {
			printTrace(e);
			throw e;
		}
	}

	private void authenticate(String requestTokenHeader) throws Exception {
		String username = null;
		String jwtToken = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {

				DecodedJWT jwt = JWT.decode(jwtToken);
				long jwtExp = jwt.getExpiresAt().getTime();
				logger.info("[jwt-mgg] : jwtExp : " + jwtExp);
				Date now = new Date();
				long timeNow = now.getTime();
				logger.info("[jwt-mgg] : timeNow : " + timeNow);
				long exp = (jwtExp - timeNow) / 1000;
				logger.info("[jwt-mgg] : Expired Difference : " + exp);

				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				logger.error("[jwt-mgg] : Unable to get JWT Token");
				throw e;
			} catch (ExpiredJwtException e) {
				logger.error("[jwt-mgg] : JWT Token has expired");
				throw e;
			} catch (SignatureException e) {
				logger.error("[jwt-mgg] : Invalid JWT Signature");
				throw e;
			} catch (Exception e) {
				logger.error("[jwt-mgg] :" + e.toString());
				printTrace(e);
				throw e;
			}
		} else {
			logger.info("[jwt-mgg] : JWT Token does not begin with Bearer String");
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			} else
				throw new Exception("Invalid Token");
		}
	}

	private void printTrace(Exception e) {
		Writer writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		logger.error(writer.toString());
	}

	private static String generateUniqKey() {
		Date currentDt = new Date();
		Random random = new Random();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddhhmmss.SSSZ." + random.nextInt(10));
		String uniqKey = sf.format(currentDt);
		return uniqKey;
	}

	private MHost cekHost(String product) throws Exception {

		HashMap<String, Object> cekHost = new HashMap<String, Object>();
		cekHost.put("product_id", product);
		MHost cekHostName = mHostMapper.findByBankCode(cekHost);

		return cekHostName;
	}

}
