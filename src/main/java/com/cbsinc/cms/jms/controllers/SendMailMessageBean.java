package com.cbsinc.cms.jms.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class SendMailMessageBean extends AbstractMessageBean {



  private XLogger log = XLoggerFactory.getXLogger(SendMailMessageBean.class.getName());
	
	 String host = "smtp.mail.ru";

	 int port = 25;

	 String mailfrom = "grabko@mail.ru";

	 String mailto = "grabko@mail.ru";

	 transient  ResourceBundle resources;
	
	static public String messageQuery = "mq_send_mail" ;
	
	
	public SendMailMessageBean()
	{
		if(resources == null) resources = PropertyResourceBundle.getBundle("SetupApplicationResources");
		host = resources.getString("sendmailagent.host");
		port = new Long(resources.getString("sendmailagent.port")).intValue();
		mailfrom = resources.getString("sendmailagent.mailfrom");
		mailto = resources.getString("sendmailagent.mailto");
	}
	

	
	
	private  boolean sendMessage(String toaddr  , String mailfromaddr ,String fromperson , String subject ,  String pathmessage ,  String attachFile ,  HashMap fields) 
	{
		Properties p = new Properties();
		p.put("mail.smtp.host", host);
		Session session = null;
		session = Session.getDefaultInstance(p, null);
		if (toaddr != null) if (toaddr.length() > 0) mailto = toaddr;
		if (mailfromaddr != null) if (mailfromaddr.length() > 0) mailfrom = mailfromaddr;
		
		try 
		{
				String msgText1  = getBody(pathmessage, fields) ;
			    MimeMessage msg = new MimeMessage(session);
			    if(fromperson.equals("")) msg.setFrom(new InternetAddress(mailfrom));
				else msg.setFrom(new InternetAddress(mailfrom,fromperson));
				InternetAddress[] address = { new InternetAddress(mailto) };
				msg.setRecipients(Message.RecipientType.TO, address);
				msg.setSubject(subject,"UTF-8");
				msg.setHeader("X-Mailer", SendMailMessageBean.class.getName()); 
				msg.addHeader("Content-Type", "text/plain"); 
				msg.addHeader("charset", "UTF-8"); 
 

				// create and fill the first message part
				MimeBodyPart mbp1 = new MimeBodyPart();
				//mbp1.setText(msgText1);
				mbp1.setText(msgText1,"UTF-8");
				// create the second message part
				MimeBodyPart mbp2 = new MimeBodyPart();

				// attach the file to the message

				FileDataSource fds = new FileDataSource(attachFile);
				mbp2.setDataHandler(new DataHandler(fds));
				mbp2.setFileName(fds.getName());

				// create the Multipart and add its parts to it
				Multipart mp = new MimeMultipart();
				mp.addBodyPart(mbp1);
				
				if (attachFile != null) if (attachFile.length() > 0) 	mp.addBodyPart(mbp2);

				// add the Multipart to the message
				msg.setContent(mp);

				// set the Date: header
				msg.setSentDate(new Date());
				msg.saveChanges();
				// send the message
				// Transport t = session.getTransport("smtp");
				Transport.send(msg);
				
			} 
			catch (Throwable th) 
			{
				log.error(th.getLocalizedMessage());
				th.printStackTrace();

			}

			return true;
	}

	private  boolean sendMessage(String toaddr  , String mailfromaddr , String fromperson  , String subject ,  String body  ) 
	{
		Properties p = new Properties();
		p.put("mail.smtp.host", host);
		Session session = null;
		session = Session.getDefaultInstance(p, null);
		if (toaddr != null) if (toaddr.length() > 0) mailto = toaddr;
		if (mailfromaddr != null) if (mailfromaddr.length() > 0) mailfrom = mailfromaddr;
		
		try 
		{
				//String msgText1  = getBody(pathmessage, fields) ;
			    MimeMessage msg = new MimeMessage(session);
				if(fromperson.equals("")) msg.setFrom(new InternetAddress(mailfrom));
				else msg.setFrom(new InternetAddress(mailfrom,fromperson));
				InternetAddress[] address = { new InternetAddress(mailto) };
				msg.setRecipients(Message.RecipientType.TO, address);
				msg.setSubject(subject,"UTF-8");
				msg.setHeader("X-Mailer", SendMailMessageBean.class.getName()); 
				msg.addHeader("Content-Type", "text/plain"); 
				msg.addHeader("charset", "UTF-8");
 

				// create and fill the first message part
				MimeBodyPart mbp1 = new MimeBodyPart();
				//mbp1.setText(body);
				mbp1.setText(body,"UTF-8");
				//mbp1.setText(body,"windows-1251");
				//String[] lang = {"windows-1251"} ;
				//mbp1.setContentLanguage(lang) ;
				//mbp1.setText(body,"cp1251");
				//mbp1.setText(body);
				//mbp1.setContentLanguage(arg0)
				//mbp1.addHeader(arg0, arg1)
			

				// create the Multipart and add its parts to it
				Multipart mp = new MimeMultipart();
				mp.addBodyPart(mbp1);
				

				// add the Multipart to the message
				msg.setContent(mp);

				//msg.setT
				// set the Date: header
				msg.setSentDate(new Date());
				msg.saveChanges();
				// send the message
				// Transport t = session.getTransport("smtp");
				Transport.send(msg);
				
			} 
			catch (Throwable th) 
			{
				log.error(th.getLocalizedMessage());
				th.printStackTrace();

			}

			return true;
	}

	
	public static String getBody(String filename, HashMap fields) {
		String body = "";
		byte[] b = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filename);
			b = new byte[fis.available()];
			fis.read(b);
			body = new String(b);

			Iterator iterator = fields.keySet().iterator();
			String key = "";
			String value = "";
			while (iterator.hasNext()) {
				key = (String) iterator.next();
				//value = new String( ((String) fields.get(key)).getBytes(),"UTF-8") ;
				value = (String) fields.get(key);
				body = body.replaceAll(key, value);
			}

			// body.replaceAll()
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return body;
	}


	

	
	public void onMessage(com.cbsinc.cms.jms.controllers.Message message, ServletContext applicationContext, HttpSession httpSession) 
	{

		String pathmessage = "" ;
		String subject = "" ;
		String to = null ;
		String mailfrom = null ;
		String attachFile = "" ;
		String body = "" ;
		String fromperson = "" ;
		HashMap fields = null ;

		if( message.get("pathmessage") instanceof  String ) pathmessage = (String) message.get("pathmessage") ;
		if( message.get("body") instanceof  String ) body = (String) message.get("body") ;
		if( message.get("subject") instanceof  String ) subject = (String) message.get("subject") ;
		if( message.get("attachFile") instanceof  String ) attachFile = (String) message.get("attachFile") ;
		if( message.get("fields") instanceof  HashMap ) fields = (HashMap) message.get("fields") ;
		if( message.get("to") instanceof  String ) to = (String) message.get("to") ;
		if( message.get("mailfrom") instanceof  String ) mailfrom = (String) message.get("mailfrom") ;
		if( message.get("fromperson") instanceof  String ) fromperson = (String) message.get("fromperson") ;
		
		

		try 
		{
			
			if( body.equals(""))sendMessage( to, mailfrom , fromperson , subject, pathmessage,  attachFile,  fields) ;
			else sendMessage( to, mailfrom ,fromperson, subject,   body  ) ;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
		}
		
		
	}
	

}
