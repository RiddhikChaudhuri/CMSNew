package com.cbsinc.cms.card;

import java.net.URL;
import java.util.Vector;
import org.apache.soap.Constants;
import org.apache.soap.Fault;
import org.apache.soap.encoding.SOAPMappingRegistry;
import org.apache.soap.rpc.Call;
import org.apache.soap.rpc.Parameter;
import org.apache.soap.rpc.Response;
import org.apache.soap.transport.http.SOAPHTTPConnection;
import org.apache.soap.util.xml.QName;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Rate {
  
  private XLogger logger = XLoggerFactory.getXLogger(Rate.class.getName());


	public RateInfo getRateInfo(String shop_id, String login, String password, String currency, String date)
			throws Exception {
		// public RateInfo getRateInfo (String shop_id ,String login, String
		// password ,String currency ,String date ) {
		RateInfo answer = null;
		String encodingStyleURI = Constants.NS_URI_SOAP_ENC;
		// url = "http://secure.assist.ru/rate/rateusd.cfm?" + request;
		URL url = new URL("http://secure.assist.ru/rate/rateusd.cfm?format=4");
		String OBJECT_URI = "http://www.assist.ru/type/";

		logger.info(encodingStyleURI);
		logger.info(Constants.NS_URI_SOAP_ENC);
		logger.info(Constants.NS_URI_CURRENT_SCHEMA_XSI);
		logger.info(Constants.NS_URI_CURRENT_SCHEMA_XSD);

		SOAPMappingRegistry smr = new SOAPMappingRegistry();
		RateinfoSerializer sd = new RateinfoSerializer();
		smr.mapTypes(Constants.NS_URI_SOAP_ENC, new QName(OBJECT_URI, "SOAPStruct"), RateInfo.class, null, sd);

		// create the transport and set parameters
		SOAPHTTPConnection st = new SOAPHTTPConnection();

		// Build the call.
		Call call = new Call();
		call.setTargetObjectURI("http://www.assist.ru/message/");
		call.setSOAPTransport(st);
		call.setSOAPMappingRegistry(smr);

		call.setMethodName("GetRate");
		call.setEncodingStyleURI(encodingStyleURI);

		Vector<Parameter> params = new Vector<Parameter>();
		params.addElement(new Parameter("shop_id", Integer.class, shop_id, null));
		params.addElement(new Parameter("login", String.class, login, null));
		params.addElement(new Parameter("password", String.class, password, null));
		params.addElement(new Parameter("currency", String.class, currency, null));
		params.addElement(new Parameter("date", String.class, date, null));
		call.setParams(params);

		// make the call: note that the action URI is empty because the
		// XML-SOAP rpc router does not need this. This may change in the
		// future.
		Response resp = call.invoke(/* router URL */url, /* actionURI */"");

		// Check the response.
		if (resp.generatedFault()) {
			Fault fault = resp.getFault();
			logger.info("Fuult: ");
			logger.info("  Fault Code   = " + fault.getFaultCode());
			logger.info("  Fault String = " + fault.getFaultString());
		} else {
			Parameter result = resp.getReturnValue();
			answer = (RateInfo) result.getValue();

			logger.info(result.getName());
			logger.info("=" + answer.getcurrency());
			logger.info("=" + answer.getdate());
			logger.info("=" + answer.getrate());
		}
		return answer;
	}

}
