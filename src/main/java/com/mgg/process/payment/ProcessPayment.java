package com.mgg.process.payment;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgg.constant.ConstantCode;
import com.mgg.constant.ConstantValidate;
import com.mgg.model.master.MHost;
import com.mgg.model.master.TbTrans;
import com.mgg.model.payment.ReqPayment;
import com.mgg.service.MHostMapper;
import com.mgg.service.MparamMapper;
import com.mgg.service.TbTransMapper;

import java.math.BigDecimal;

@Service
public class ProcessPayment {

	Logger logger = LoggerFactory.getLogger(ProcessPayment.class);

	@Autowired
	private MHostMapper mHostMapper;

	@Autowired
	private MparamMapper mParamMapper;

	@Autowired
	private TbTransMapper tbTransMapper;

	private String getToken(ReqPayment reqPayment, String user, String pass) throws Exception {

		HttpParams httpParams = new BasicHttpParams();
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);

		String url = decrypt(mParamMapper.getParam(cekHost(reqPayment.getSourceBankCode()).getKd_host(), "url")) + "/signon";

		HttpPost httpPost = new HttpPost(url);

		logger.info("[pay-process] REQUEST TOKEN TO BILLER : " + encrypt(url));

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", decrypt(mParamMapper.getParam(cekHost(reqPayment.getSourceBankCode()).getKd_host(), "username"))));
		params.add(new BasicNameValuePair("password", decrypt(mParamMapper.getParam(cekHost(reqPayment.getSourceBankCode()).getKd_host(), "password"))));

		logger.info("[pay-process] FORM DATA TOKEN TO BILLER : " + encrypt(params.toString()));

		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity entity = response.getEntity();

		String content = EntityUtils.toString(entity);

		logger.info("[pay-process] RESPONSE TOKEN FROM BILLER : " + content);

		return content;
	}

	public HashMap<String, Object> sendToProcess(ReqPayment reqPayment) {

		HashMap<String, Object> prepReq = new HashMap<String, Object>();

		try {

			logger.info("[pay-process] =====================================");
			logger.info("[pay-process] SEND TO BILLER SERVER START : " + reqPayment.getCustomerNo());

			HashMap<String, Object> cp = cekPayment(reqPayment);

			if (cp == null) {
				
				logger.error("[pay-process] Validation Payment Error.");
				prepReq.put("resultCd", ConstantCode.ErrCode26);
				prepReq.put("resultMsg", ConstantCode.ErrCode26Desc);
				return prepReq;
				
			} else {
				
				prepReq.put("trxData", cp.get("trxData"));
				
				if (!cp.get("resultCd").equals(ConstantCode.ErrCode00)) {
					prepReq.put("resultCd", cp.get("resultCd"));
					prepReq.put("resultMsg", cp.get("resultMsg"));
					return prepReq;
				}
				
				HttpParams httpParams = new BasicHttpParams();
				DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);

				String user = decrypt(mParamMapper.getParam(cekHost(reqPayment.getSourceBankCode()).getKd_host(), "username"));
				String pass = decrypt(mParamMapper.getParam(cekHost(reqPayment.getSourceBankCode()).getKd_host(), "password"));
				String url = decrypt(mParamMapper.getParam(cekHost(reqPayment.getSourceBankCode()).getKd_host(), "url")) + "/payment/" + reqPayment.getCustomerNo()+"/"+cp.get("total");

				logger.info("[pay-process] REQUEST TO BILLER : " + encrypt(url));

				HttpPost httpPost = new HttpPost(url);

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("token", getToken(reqPayment, user, pass)));

				logger.info("[pay-process] FORM DATA TO BILLER : " + encrypt(params.toString()));

				httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();

				String content = EntityUtils.toString(entity);

				logger.info("[pay-process] RESPONSE FROM BILLER : " + content);

				JSONParser parser = new JSONParser();
				JSONObject result = (JSONObject) parser.parse(content);

				if (result == null) {

					logger.error("[pay-process] Response from client is null.");
					prepReq.put("resultCd", ConstantCode.ErrCode99);
					prepReq.put("resultMsg", ConstantCode.ErrCode99Desc);

				} else if (result != null) {

					logger.info("[pay-process] RESPONSE BILLER CODE : " + result.get("respcode"));
					logger.info("[pay-process] RESPONSE BILLER CODE Message : " + result.get("message"));
					logger.info("[pay-process] =====================================");

					String ret = ""; //result.get("code").toString();
					String msg = ""; //result.get("message").toString();

					String resultcode = result.get("respcode").toString();
					String resultmsg = result.get("message").toString();
					
					if(resultcode.equals("00")) {
						ret = ConstantCode.ErrCode00;
						msg = ConstantCode.ErrCode00Desc;
					} else if(resultcode.equals("10")) {
						ret = ConstantCode.ErrCode56;
						msg = ConstantCode.ErrCode56Desc;
					} else if(resultcode.equals("13")) {
						ret = ConstantCode.ErrCode88;
						msg = ConstantCode.ErrCode88Desc;
					} else {
						
						logger.info("[pay-process] Error need to be mapped : "+ resultmsg);
						ret = ConstantCode.ErrCode99;
						msg = ConstantCode.ErrCode99Desc;
					}
					
					if (resultcode.equals(ConstantValidate.valCoded00)) {

						logger.info("[pay-process] Response from biller success.");
						logger.info("[pay-process] Begin update db after bill success.");

						Integer updateTrx = updateTrans(content, cp.get("trxid").toString(), reqPayment);

						if (updateTrx.equals(222)) {

							logger.info("[pay-process] Update db after bill success.");

							prepReq.put("resultCd", ConstantCode.ErrCode00);
							prepReq.put("resultMsg", ConstantCode.ErrCode00Desc);
							prepReq.put("amount", cp.get("amount"));
							prepReq.put("charge", cp.get("charge"));
							prepReq.put("total", cp.get("total"));
							prepReq.put("nama", cp.get("nama"));
							prepReq.put("alamat", cp.get("alamat"));
							prepReq.put("trxid", cp.get("trxid"));
							prepReq.put("idbilling", cp.get("idbilling"));

						} else {

							logger.info("[pay-process] Error when update table tb_trans.");
							prepReq.put("resultCd", ConstantCode.ErrCode28);
							prepReq.put("resultMsg", ConstantCode.ErrCode28Desc);

						}

					} else if (resultcode.equals(ConstantValidate.valCoded88)) {

						logger.info("[pay-process] Response Biller " + content);
						prepReq.put("resultCd", ConstantCode.ErrCode88);
						prepReq.put("resultMsg", ConstantCode.ErrCode88Desc);

					} else if (resultcode.equals(ConstantValidate.valCoded00) == false) {

						prepReq.put("resultCd", ret);
						prepReq.put("resultMsg", msg);

					} else {

						prepReq.put("resultCd", ret);
						prepReq.put("resultMsg", msg);

					}

				}
			}
			
			return prepReq;

		} catch (RestClientException e) {

			logger.info("[pay-process] RESPONSE CATCH 1 : " + e.getCause().toString());

			if (e.getCause() instanceof ConnectException) {

				logger.error("[pay-process] Could not connect to pdam.");
				prepReq.put("resultCd", ConstantCode.ErrCode85);
				prepReq.put("resultMsg", ConstantCode.ErrCode85Desc);

				return prepReq;

			} else if (e.getCause() instanceof ConnectTimeoutException) {

				logger.error("[pay-process] Could not connect to pdam.");
				prepReq.put("resultCd", ConstantCode.ErrCode85);
				prepReq.put("resultMsg", ConstantCode.ErrCode85Desc);

				return prepReq;

			} else if (e.getCause() instanceof UnknownHostException) {

				logger.error("[pay-process] Unknown pdam host.");
				prepReq.put("resultCd", ConstantCode.ErrCode84);
				prepReq.put("resultMsg", ConstantCode.ErrCode84Desc);

				return prepReq;

			} else if (e.getCause() instanceof SocketTimeoutException) {

				logger.error("[pay-process] Read Timeout to pdam.");
				prepReq.put("resultCd", ConstantCode.ErrCode86);
				prepReq.put("resultMsg", ConstantCode.ErrCode86Desc);

				return prepReq;

			}

			throw e; // rethrow if not a ConnectException

		} catch (Exception e) {

			logger.info("[pay-process] RESPONSE CATCH 2 : " + e.getMessage());

			e.printStackTrace();
			logger.info("[pay-process] Unknown Error.");

			prepReq.put("resultCd", ConstantCode.ErrCode99);
			prepReq.put("resultMsg", ConstantCode.ErrCode99Desc);

			return prepReq;

		}
	}

	public HashMap<String, Object> cekPayment(ReqPayment reqPayment) {

		logger.info("[pay-process] Cek payment");

		HashMap<String, Object> prepReq = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();

		try {

			List<TbTrans> list = tbTransMapper.findTxId(reqPayment.getTrxid());

			logger.info("[pay-process] Data From DB : "+mapper.writeValueAsString(list));
			
			String status = "";
			String trxid = "";
			String retnum = "";
			String idbilling = "";
			String nama = "";
			String alamat = "";

			BigDecimal Rek = BigDecimal.ZERO;
			BigDecimal TotalTag = BigDecimal.ZERO;
			BigDecimal TotalDenda = BigDecimal.ZERO;
			BigDecimal totSettl = BigDecimal.ZERO;
			BigDecimal totAdmin = BigDecimal.ZERO;
						
			Integer i = 0;
			
			for (TbTrans tbTrans : list) {
				
				i++;

				Rek = new BigDecimal(tbTrans.getAmount());
				BigDecimal Settl = new BigDecimal(tbTrans.getAmount())
						.add(new BigDecimal(tbTrans.getFine()));

				TotalTag = TotalTag.add(Rek);
				TotalDenda = TotalDenda.add(new BigDecimal(tbTrans.getFine()));
				totSettl = totSettl.add(Settl);

			}
			
			status = list.get(0).getStatus();
			retnum = list.get(0).getRetnum();
			trxid = list.get(0).getTrxid();
			idbilling = list.get(0).getCustomer_id();
			nama = list.get(0).getCustomer_name();
			alamat = list.get(0).getCustomer_address();
			
			prepReq.put("trxData", list.get(0).getResponse_inquiry());
			prepReq.put("status", status);
			prepReq.put("amount", TotalTag);
			prepReq.put("charge", TotalDenda);
			prepReq.put("total", totSettl);
			prepReq.put("adm_fee", totAdmin);
			prepReq.put("transStr", list);
			prepReq.put("trxid", trxid);
			prepReq.put("retnum", retnum);
			prepReq.put("nama", nama);
			prepReq.put("alamat", alamat);
			prepReq.put("idbilling", idbilling);

			if (idbilling.equals(reqPayment.getCustomerNo()) == false) {
				logger.error("[pay-bjb-edupay] Customer ID is Different");
				prepReq.put("resultCd", ConstantCode.ErrCode56);
				prepReq.put("resultMsg", ConstantCode.ErrCode56Desc);
				return prepReq;
			}
			
			if (status == null) {
				logger.error("[pay-process] Error, Trx Not Found");
				prepReq.put("resultCd", ConstantCode.ErrCode32);
				prepReq.put("resultMsg", ConstantCode.ErrCode32Desc);
				return prepReq;
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			logger.error("[pay-process] Error when select trxid");
			prepReq.put("resultCd", ConstantCode.ErrCode24);
			prepReq.put("resultMsg", ConstantCode.ErrCode24Desc);

			return prepReq;
			
		}

		logger.info("[pay-process] Pass Check trxid");
		prepReq.put("resultCd", ConstantCode.ErrCode00);
		prepReq.put("resultMsg", ConstantCode.ErrCode00Desc);

		return prepReq;
		
	}
	
	public Integer updateTrans(String content, String trxid, ReqPayment reqPayment) {
		try {
			
			HashMap<String, Object> inp = new HashMap<String, Object>();
			logger.info("[pay-process] Prepare update to tb_trans.");

			List<TbTrans> list = tbTransMapper.findTxId(trxid);
			
			Integer sel = 0;

			for (TbTrans tbTrans : list) {
				
				inp.put("trxid", trxid);
				inp.put("status", "00");
				inp.put("response_payment", content);

				logger.info("[pay-process] update to tb_trans : " + inp);

				sel = tbTransMapper.updateTxIdAdditional(inp);
				
			}
			
			if (sel > 0) {
				logger.info("[pay-process] update to tb_trans success.");
				return 222;
			} else {
				logger.info("[pay-process] update to tb_trans fail");
				return 111;
			}

		} catch (Exception e) {
			
			logger.info("[pay-process] update to tb_trans fail");
			e.printStackTrace();
			return 111;
			
		}
	}
	
	private MHost cekHost(String product) throws Exception {

		HashMap<String, Object> cekHost = new HashMap<String, Object>();
		cekHost.put("product_id", product);
		MHost cekHostName = mHostMapper.findByBankCode(cekHost);
		
		return cekHostName;
	}
	
	public String encrypt(String plain){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("p1nt4rkau23mggTest"); // encryptor's private key
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        
        return encryptor.encrypt(plain);
    }
	
	public String decrypt(String plain){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("p1nt4rkau23mggTest"); // encryptor's private key
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        
        return encryptor.decrypt(plain);
    }
}
