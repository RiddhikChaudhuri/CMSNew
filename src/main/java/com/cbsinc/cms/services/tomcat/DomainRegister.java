package com.cbsinc.cms.services.tomcat;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

//import javax.servlet.ServletContextEvent;


import org.w3c.dom.Document;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;
import com.cbsinc.cms.publisher.dao.TransactionSupport;



@Component
public class DomainRegister {

  private final XLogger logger = XLoggerFactory.getXLogger(DomainRegister.class.getName());
    
    static AuthorizationPageFaced authorizationPageFaced;
    
    @Autowired
    TransactionSupport transactionSupport;
    
    @Autowired
    public void setAuthorizationPageFaced(AuthorizationPageFaced authorizationPageFaced){
      DomainRegister.authorizationPageFaced = authorizationPageFaced;
    }
	
    String authId = "" ;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		authorizationPageFaced.PostToDNSServerAliases();
	}
	
	public String getSiteIdByHost(String host)
	{
		
		String siteId = "2" ;
		String login = "admin" ;
		String query = "select site_id ,login from tuser where levelup_cd = 2 "  ;
		TransactionStatus trxnStatus = transactionSupport.beginTransaction();
		try 
		{
			DomainRegister r = new DomainRegister();
			String auth = r.getAuth() ;
			List<Map<String,Object>> siteIdByHostList = transactionSupport.getJdbcTemplate().queryForList(query);
			for (int i = 0; siteIdByHostList.size() > i; i++) 
			{
				siteId =  (String) siteIdByHostList.get(i).get("site_id");
				login =  (String) siteIdByHostList.get(i).get("login");
				String queryUpdate = "update site set host = '" + "www." + login + ".irr.bz" + "' where site_id =  " + siteId ;
				transactionSupport.getJdbcTemplate().update(queryUpdate);
				logger.info(" site " + login + " id " + siteId) ;
				transactionSupport.commitTransaction(trxnStatus);
			}
		}
		catch (Exception e) 
		{
			transactionSupport.rollbackTransaction(trxnStatus);
		}
		finally
		{
			logger.exit();
		}

	  return siteId ;
	}
	

	public String getAuth()
	{
		
		StringBuffer buff = new StringBuffer() ;
		try {
			String data = "username=grabko@mail.ru&password=hdotoodu" ;
		    URL url = new URL("https://dmapi.joker.com/request/login");
		    URLConnection conn = url.openConnection();
		    conn.setDoOutput(true);
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(data);
		    wr.flush();
		    //AuthId
		    // Get the response
		    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line = "";
		    String[] array = new String[0];
		    while ((line = rd.readLine()) != null) 
		    {
		    	if(line.indexOf("Auth-Sid") != -1)
		    	{
		    		array  = line.split(":");
		    		buff.append("Auth-Sid=" + array[1].trim()) ;
		    	}
		    }
		    wr.close();
		    rd.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		

	  return buff.toString() ;
	}
	
	public String setDomainAliasesOld(String domain , String zone , String authId )
	{
		
		StringBuffer buff = new StringBuffer() ;
		try {
		    // Construct data
		    // Send data
			String data = "domain="+domain+"&zone="+ URLEncoder.encode(zone,"UTF-8")+"&" + authId ;
		    URL url = new URL("https://dmapi.joker.com/request/dns-zone-put?");
		    //URLConnection conn = url.openConnection();
		    URLConnection conn = url.openConnection();
		    conn.setDoOutput(true);
		    //conn.set
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(data);
		    wr.flush();

		    // Get the response
		    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line;
		    while ((line = rd.readLine()) != null) 
		    {
		    	buff.append(line + "\n") ;
		    }
		    wr.close();
		    rd.close();
		} 
		catch (Exception e) 
		{
		e.printStackTrace();
		}

	  return buff.toString() ;
	}
	
	
	public String setDomainAliases(String domain , String zone , String authId )
	{
		
		StringBuffer buff = new StringBuffer() ;
		try {
		    // Construct data
		    // Send data
			String data = "domain="+domain+"&zone="+ URLEncoder.encode(zone,"UTF-8")+"&" + authId ;
		    URL url = new URL("https://dmapi.joker.com/request/dns-zone-put?");
		    //URLConnection conn = url.openConnection();
		    HttpURLConnection conn= (HttpURLConnection) url.openConnection();
		    conn.setDoOutput(true);
		    conn.setInstanceFollowRedirects( false );
		    conn.setRequestMethod( "POST" );
		    conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
		    conn.setRequestProperty( "charset", "utf-8");
		    conn.setRequestProperty( "Content-Length", Integer.toString( data.length() ));
		    conn.setUseCaches( false );
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(data);
		    wr.flush();

		    // Get the response
		    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line;
		    while ((line = rd.readLine()) != null) 
		    {
		    	buff.append(line + "\n") ;
		    }
		    wr.close();
		    rd.close();
		} 
		catch (Exception e) 
		{
		e.printStackTrace();
		}

	  return buff.toString() ;
	}
	
	public String getPostCommandOld(String authId)
	{
		
		StringBuffer buff = new StringBuffer() ;
		try {
		    // Construct data
		    //String data = URLEncoder.encode("key1", "UTF-8") + "=" + URLEncoder.encode("value1", "UTF-8");
		    //data += "&" + URLEncoder.encode("key2", "UTF-8") + "=" + URLEncoder.encode("value2", "UTF-8");

		    // Send data
		    //https://dmapi.joker.com/request/dns-zone-get?domain=irr.bz
			String data = "domain=irr.bz&" + authId ;
		    URL url = new URL("https://dmapi.joker.com/request/dns-zone-get");
		    URLConnection conn = url.openConnection();
		    conn.setDoOutput(true);
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(data);
		    wr.flush();

		    // Get the response
		    int i = 0 ;
		    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line;
		    while ((line = rd.readLine()) != null) {
		    	buff.append(++i + " " + line + "\n") ;
		    }
		    wr.close();
		    rd.close();
		}
		catch (Exception e) 
		{
		e.printStackTrace();
		}

		
		

	  return buff.toString() ;
	}
	
	
	
	public String getPostCommand(String authId)
	{
		
		StringBuffer buff = new StringBuffer() ;
		try {
		    // Construct data
		    //String data = URLEncoder.encode("key1", "UTF-8") + "=" + URLEncoder.encode("value1", "UTF-8");
		    //data += "&" + URLEncoder.encode("key2", "UTF-8") + "=" + URLEncoder.encode("value2", "UTF-8");

		    // Send data
		    //https://dmapi.joker.com/request/dns-zone-get?domain=irr.bz
			String data = "domain=irr.bz&" + authId ;
		    URL url = new URL("https://dmapi.joker.com/request/dns-zone-get");
		    //URLConnection conn = url.openConnection();
		    HttpURLConnection conn= (HttpURLConnection) url.openConnection();
		    conn.setDoOutput(true);
		    conn.setInstanceFollowRedirects( false );
		    conn.setRequestMethod( "POST" );
		    conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
		    conn.setRequestProperty( "charset", "utf-8");
		    conn.setRequestProperty( "Content-Length", Integer.toString( data.length() ));
		    conn.setUseCaches( false );
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(data);
		    wr.flush();

		    // Get the response
		    int i = 0 ;
		    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line;
		    while ((line = rd.readLine()) != null) {
		    	buff.append(++i + " " + line + "\n") ;
		    }
		    wr.close();
		    rd.close();
		}
		catch (Exception e) 
		{
		e.printStackTrace();
		}

		
		

	  return buff.toString() ;
	}
	
	
	
public void addAliases() {
		AddAliase addAliase;
		Document doc ;
		//String query = "select host  from site" ;
		String query = "select host  from site group by host" ;
		String host = "www.siteforyou.net" ;
		String aliase = "" ;
		
		StringBuffer hosts_for_dns = new StringBuffer();
		File file =  new File("/") ;
		 String server_config = System.getProperty("conf");
		 if(server_config == null || server_config.length() == 0) 
		 {
			 //String dir = System.getProperty("user.dir");
			 //dir = dir.substring(0 ,dir.indexOf("bin")) + "conf" + File.separatorChar+"server.xml" ;
			//File f = new File("C:/apache-tomcat-6.0.14/conf/server.xml") ;
			 file = new File("C:/apache-tomcat-6.0.14/conf/server.xml") ;
			 //file = new File(dir) ;
		 }
		 else 
		 {
			 file = new File(server_config) ; 
		 }

		
		
		try 
		{ 
			addAliase = new AddAliase();
			doc = addAliase.loadConfig(file);
			addAliase.RemoveAllAliase(doc, host) ;
	         List<Map<String,Object>> siteIdByHostList = transactionSupport.getJdbcTemplate().queryForList(query);
			for (int i = 0; siteIdByHostList.size() > i; i++) 
			{
				aliase =  (String) siteIdByHostList.get(i).get("host");
				int indexof = aliase.indexOf(".irr.bz");
				if(indexof != -1 && isAnsi(aliase) )
				{
					addAliase.AddAliaseToHost(doc, host, aliase) ;
					hosts_for_dns.append(aliase.substring(0,indexof));	
					hosts_for_dns.append(" A 0 54.201.41.182 86400\n");
				}
			}
			addAliase.writeXmlFile(doc,file);	 
		}
		catch (Exception e) 
		{
			logger.error(" server config: " + file.getPath());
			e.printStackTrace();
		}
		finally
		{

			DomainRegister r = new DomainRegister();
			String auth = r.getAuth() ;
			r.setDomainAliases("irr.bz" , hosts_for_dns.toString() ,auth );
			logger.error(r.getPostCommand(auth));
		}

		
	}
	
public boolean isAnsi(String tmp) {
	if (tmp == null)
		return false;
	tmp = tmp.toLowerCase();
	String IntField = "0123456789qwertyuiopasdfghjklzxcvbnm-_.";
	for (int i = 0; i < tmp.length(); i++) {

		if (IntField.indexOf(tmp.charAt(i)) == -1) {
			if (tmp.charAt(i) != '-' && i != 0)
				return false;
		}
	}
	return true;
}


}
