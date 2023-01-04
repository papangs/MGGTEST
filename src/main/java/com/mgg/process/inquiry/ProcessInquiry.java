package com.mgg.process.inquiry;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
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
import com.mgg.model.inquiry.ReqInquiry;
import com.mgg.model.master.MHost;
import com.mgg.model.master.TbTrans;
import com.mgg.service.MHostMapper;
import com.mgg.service.MparamMapper;
import com.mgg.service.TbTransMapper;

import java.math.BigDecimal;

@Service
public class ProcessInquiry {

	Logger logger = LoggerFactory.getLogger(ProcessInquiry.class);

	@Autowired
	private MHostMapper mHostMapper;

	@Autowired
	private MparamMapper mParamMapper;

	@Autowired
	private TbTransMapper tbTransMapper;

	private String kode01;
	private String tXid;
	
	private String getToken(ReqInquiry reqInquiry, String user, String pass) throws Exception {

		HttpParams httpParams = new BasicHttpParams();
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);

		String url = decrypt(mParamMapper.getParam(cekHost(reqInquiry.getSourceBankCode()).getKd_host(), "url")) + "/signon";

		HttpPost httpPost = new HttpPost(url);

		logger.info("[inq-process] REQUEST TOKEN TO BILLER : " + encrypt(url));

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", decrypt(mParamMapper.getParam(cekHost(reqInquiry.getSourceBankCode()).getKd_host(), "username"))));
		params.add(new BasicNameValuePair("password", decrypt(mParamMapper.getParam(cekHost(reqInquiry.getSourceBankCode()).getKd_host(), "password"))));

		logger.info("[inq-process] FORM DATA TOKEN TO BILLER : " + encrypt(params.toString()));

		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity entity = response.getEntity();

		String content = EntityUtils.toString(entity);

		logger.info("[inq-process] RESPONSE TOKEN FROM BILLER : " + content);

		return content;
	}

	public HashMap<String, Object> sendToProcess(ReqInquiry reqInquiry) {

		HashMap<String, Object> prepReq = new HashMap<String, Object>();

		try {

			logger.info("[inq-process] =====================================");
			logger.info("[inq-process] SEND TO BILLER SERVER START : " + reqInquiry.getCustomerNo());

			HttpParams httpParams = new BasicHttpParams();
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);

			String user = decrypt(mParamMapper.getParam(cekHost(reqInquiry.getSourceBankCode()).getKd_host(), "username"));
			String pass = decrypt(mParamMapper.getParam(cekHost(reqInquiry.getSourceBankCode()).getKd_host(), "password"));
			String url = decrypt(mParamMapper.getParam(cekHost(reqInquiry.getSourceBankCode()).getKd_host(), "url")) + "/inquiry/" + reqInquiry.getCustomerNo();

			logger.info("[inq-process] REQUEST TO BILLER : " + encrypt(url));

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("token", getToken(reqInquiry, user, pass)));

			logger.info("[inq-process] FORM DATA TO BILLER : " + encrypt(params.toString()));

			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			String content = EntityUtils.toString(entity);

			logger.info("[inq-process] RESPONSE FROM BILLER : " + content);

			JSONParser parser = new JSONParser();
			JSONObject result = (JSONObject) parser.parse(content);

			if (result == null) {

				logger.error("[inq-process] Response from client is null.");
				prepReq.put("resultCd", ConstantCode.ErrCode99);
				prepReq.put("resultMsg", ConstantCode.ErrCode99Desc);

			} else if (result != null) {

				logger.info("[inq-process] RESPONSE BILLER CODE : " + result.get("respcode"));
				logger.info("[inq-process] RESPONSE BILLER CODE Message : " + result.get("message"));
				logger.info("[inq-process] =====================================");

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
					
					logger.info("[inq-process] Error need to be mapped : "+ resultmsg);
					ret = ConstantCode.ErrCode99;
					msg = ConstantCode.ErrCode99Desc;
				}
				
				if (resultcode.equals(ConstantValidate.valCoded00)) {

					prepReq.put("resultCd", ConstantCode.ErrCode00);
					prepReq.put("resultMsg", ConstantCode.ErrCode00Desc);

					Integer iTrans = insertTrans(content, reqInquiry);

					if (iTrans.equals(222)) {

						prepReq.put("resultCd", ConstantCode.ErrCode00);
						prepReq.put("resultMsg", ConstantCode.ErrCode00Desc);
						prepReq.put("data_pelanggan", content);
						prepReq.put("trxid", tXid);

					} else {

						if (kode01.equals("ok")) {

							logger.info("[inq-process] find tb_trans status 01 00 success 2");
							prepReq.put("resultCd", ConstantCode.ErrCode88);
							prepReq.put("resultMsg", ConstantCode.ErrCode88Desc);

						} else {

							logger.info("[inq-process] Error when input table tb_trans.");
							prepReq.put("resultCd", "63");
							prepReq.put("resultMsg", "Error when input to db");
						}

					}

				} else if (resultcode.equals(ConstantValidate.valCoded88)) {


					logger.info("[inq-process] Response Biller " + content);
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

			return prepReq;

		} catch (RestClientException e) {

			logger.info("[inq-process] RESPONSE CATCH 1 : " + e.getCause().toString());

			if (e.getCause() instanceof ConnectException) {

				logger.error("[inq-process] Could not connect to pdam.");
				prepReq.put("resultCd", ConstantCode.ErrCode85);
				prepReq.put("resultMsg", ConstantCode.ErrCode85Desc);

				return prepReq;

			} else if (e.getCause() instanceof ConnectTimeoutException) {

				logger.error("[inq-process] Could not connect to pdam.");
				prepReq.put("resultCd", ConstantCode.ErrCode85);
				prepReq.put("resultMsg", ConstantCode.ErrCode85Desc);

				return prepReq;

			} else if (e.getCause() instanceof UnknownHostException) {

				logger.error("[inq-process] Unknown pdam host.");
				prepReq.put("resultCd", ConstantCode.ErrCode84);
				prepReq.put("resultMsg", ConstantCode.ErrCode84Desc);

				return prepReq;

			} else if (e.getCause() instanceof SocketTimeoutException) {

				logger.error("[inq-process] Read Timeout to pdam.");
				prepReq.put("resultCd", ConstantCode.ErrCode86);
				prepReq.put("resultMsg", ConstantCode.ErrCode86Desc);

				return prepReq;

			}

			throw e; // rethrow if not a ConnectException

		} catch (Exception e) {

			logger.info("[inq-process] RESPONSE CATCH 2 : " + e.getMessage());

			e.printStackTrace();
			logger.info("[inq-process] Unknown Error.");

			prepReq.put("resultCd", ConstantCode.ErrCode99);
			prepReq.put("resultMsg", ConstantCode.ErrCode99Desc);

			return prepReq;

		}
	}

	private String getYear(String period) {
		String year = "";
		String[] tahun = period.split(",");
		ArrayList<String> arraylist = new ArrayList<String>();
		for (String string : tahun) {
			arraylist.add(string);
		}
		for (String str : arraylist) {
			year = str;
		}
		return year;
	}
	
	private Integer insertTrans(String strParams, ReqInquiry reqInquiry) {

		try {

			HashMap<String, Object> inp = new HashMap<String, Object>();
			HashMap<String, Object> inp1 = new HashMap<String, Object>();
			HashMap<String, Object> inps = new HashMap<String, Object>();
			HashMap<String, Object> inp2 = new HashMap<String, Object>();
			HashMap<String, Object> inp3 = new HashMap<String, Object>();

			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

			Random randomInt = new Random();
			Integer intRand = randomInt.nextInt(999999);
			String strRand = String.format("%6s", intRand).replace(' ', '0');

			ObjectMapper mapper = new ObjectMapper();

			HashMap<String, Object> data = mapper.readValue(strParams, HashMap.class);

			logger.info("[inq-process] Prepare insert to tb_trans.");

			BigDecimal TotalTag = BigDecimal.ZERO;
			BigDecimal TotalDenda = BigDecimal.ZERO;
			BigDecimal TotalSettlement = BigDecimal.ZERO;
			Integer bill_reapeat = 0;

			//MAPPING TAHUN
			String year = getYear(data.get("periods").toString());
			//MAPPING TAHUN
			
			// CHEK DATA YANG STATUS 01 00
			List<TbTrans> lists = new ArrayList<TbTrans>();

			for (int i = 0; i < 1; i++) {
				
				HashMap<String, Object> inn = new HashMap<String, Object>();

				inn.put("customer_id", reqInquiry.getCustomerNo());
				inn.put("product_id", reqInquiry.getSourceBankCode());
				inn.put("periode", year);

				TbTrans tbTranss = tbTransMapper.find0100Commit(inn);

				if (tbTranss == null) {
					lists.isEmpty();
				} else {
					lists.add(tbTranss);
				}

			}

			if (lists.size() > 0) {

				logger.info("[inq-process] find tb_trans status 01 00 success");
				kode01 = "ok";
				return 111;

			}
			// CHEK DATA YANG STATUS 01 00

			// DELETE DATA YANG STATUS 03 02
			HashMap<String, Object> in = new HashMap<String, Object>();

			in.put("customer_id", reqInquiry.getCustomerNo());
			in.put("product_id", reqInquiry.getSourceBankCode());

			List<TbTrans> tbTrans = tbTransMapper.findTxBeforePayment(in);

			if (tbTrans.size() > 0) {

				Integer del = tbTransMapper.deleteByTxIdBeforePayment(in);

				logger.info("[inq-process] Response Delete : " + del);

				if (del.equals(0)) {
					logger.info("[inq-process] delete tb_trans status 03 fail");
				}

				logger.info("[inq-process] delete tb_trans status 03 success");
			}
			// DELETE DATA YANG STATUS 03 02

			for (int i = 0; i < 1; i++ ) {

				bill_reapeat = bill_reapeat + 1;

				TotalTag = new BigDecimal(data.get("bill").toString());
				TotalDenda = new BigDecimal(data.get("fines").toString());
				TotalSettlement = new BigDecimal(data.get("total").toString());

				String TRXID = "TRXIDMGG" + dateFormat.format(date) + strRand;
				
				inp.put("trxid", TRXID);
				inp.put("status", "03");
				inp.put("retnum", reqInquiry.getRetnum());
				inp.put("customer_id", data.get("custno"));
				inp.put("customer_name", data.get("custname"));
				inp.put("customer_address", data.get("custaddr"));
				inp.put("amount", TotalTag);
				inp.put("fine", TotalDenda);
				inp.put("total", TotalSettlement);
				inp.put("response_inquiry", strParams);
				inp.put("response_payment", "");
				inp.put("additional_data", "");
				inp.put("product_id", reqInquiry.getSourceBankCode());
				inp.put("merchant_id", "");
				inp.put("periode", year);

				logger.info("[inq-process] insert to tb_trans : " + inp);

				TbTrans cTrans = tbTransMapper.findTxVacctNo(inp);

				if (cTrans == null) {

					inp1.put("trxid", TRXID);
					inp1.put("status", "03");
					inp1.put("retnum", reqInquiry.getRetnum());
					inp1.put("customer_id", data.get("custno"));
					inp1.put("customer_name", data.get("custname"));
					inp1.put("customer_address", data.get("custaddr"));
					inp1.put("amount", TotalTag);
					inp1.put("fine", TotalDenda);
					inp1.put("total", TotalSettlement);
					inp1.put("response_inquiry", strParams);
					inp1.put("response_payment", "");
					inp1.put("additional_data", "");
					inp1.put("product_id", reqInquiry.getSourceBankCode());
					inp1.put("merchant_id", "");
					inp1.put("periode", year);

					Integer sel1 = tbTransMapper.insertTxId(inp1);
					tXid = inp1.get("trxid").toString();

					if (sel1.equals(1) == false) {

						logger.info("[inq-process] insert to tb_trans fail");
						return 111;

					}

				} else {

					if (cTrans.getStatus().equals("04")) {

						inps.put("trxid", TRXID);
						inps.put("trxid_old", cTrans.getTrxid());
						inps.put("status", "03");
						inps.put("retnum", reqInquiry.getRetnum());
						inps.put("customer_id", data.get("custno"));
						inps.put("customer_name", data.get("custname"));
						inps.put("customer_address", data.get("custaddr"));
						inps.put("amount", TotalTag);
						inps.put("fine", TotalDenda);
						inps.put("total", TotalSettlement);
						inps.put("response_inquiry", strParams);
						inps.put("response_payment", "");
						inps.put("additional_data", "");
						inps.put("product_id", reqInquiry.getSourceBankCode());
						inps.put("merchant_id", "");
						inps.put("periode", year);
					
						Integer sels = tbTransMapper.updateTxId0403(inps);
						tXid = inps.get("trxid").toString();

						if (sels.equals(1) == false) {

							logger.info("[inq-process] insert to tb_trans fail");
							return 111;

						}

					} else if (cTrans.getStatus().equals("03")) {

						inp2.put("trxid", TRXID);
						inp2.put("trxid_old", cTrans.getTrxid());
						inp2.put("status", "03");
						inp2.put("retnum", reqInquiry.getRetnum());
						inp2.put("customer_id", data.get("custno"));
						inp2.put("customer_name", data.get("custname"));
						inp2.put("customer_address", data.get("custaddr"));
						inp2.put("amount", TotalTag);
						inp2.put("fine", TotalDenda);
						inp2.put("total", TotalSettlement);
						inp2.put("response_inquiry", strParams);
						inp2.put("response_payment", "");
						inp2.put("additional_data", "");
						inp2.put("product_id", reqInquiry.getSourceBankCode());
						inp2.put("merchant_id", "");
						inp2.put("periode", year);

						Integer sel1 = tbTransMapper.updateTxIdd(inp2);
						tXid = inp2.get("trxid").toString();

						if (sel1.equals(1) == false) {

							logger.info("[inq-process] update to tb_trans fail");
							return 111;

						}

					} else if (cTrans.getStatus().equals("00")) {

						tXid = cTrans.getTrxid();
						logger.info("[inq-process] insert to tb_trans success.");
						return 222;

					} else if (cTrans.getStatus().equals("02")) {

						inp3.put("trxid", TRXID);
						inp3.put("trxid_old", cTrans.getTrxid());
						inp3.put("status", "03");
						inp3.put("retnum", reqInquiry.getRetnum());
						inp3.put("customer_id", data.get("custno"));
						inp3.put("customer_name", data.get("custname"));
						inp3.put("customer_address", data.get("custaddr"));
						inp3.put("amount", TotalTag);
						inp3.put("fine", TotalDenda);
						inp3.put("total", TotalSettlement);
						inp3.put("response_inquiry", strParams);
						inp3.put("response_payment", "");
						inp3.put("additional_data", "");
						inp3.put("product_id", reqInquiry.getSourceBankCode());
						inp3.put("merchant_id", "");
						inp3.put("periode", year);

						Integer sel1 = tbTransMapper.updateTxId0203(inp3);
						tXid = inp3.get("trxid").toString();

						if (sel1.equals(1) == false) {

							logger.info("[inq-process] update to tb_trans fail");
							return 111;

						}
					}

				}
			}

			logger.info("[inq-process] insert to tb_trans success.");
			return 222;

		} catch (Exception e) {

			logger.info("[inq-process] insert to tb_trans fail");
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
