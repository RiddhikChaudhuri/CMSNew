package com.cbsinc.cms.jms.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


public class StoreCashMessageBean extends AbstractMessageBean
{

  private XLogger log = XLoggerFactory.getXLogger(StoreCashMessageBean.class.getName());

	static public String messageQuery = "mq_store_cash" ;
	

	public void onMessage(Message message, ServletContext applicationContext, HttpSession httpSession) {
		
		byte[] htmlBytes  = null ;
		String  FileName = null ;
		if( message.get("cashPageBodyByteArray") instanceof  byte[] ) htmlBytes = (byte[]) message.get("cashPageBodyByteArray") ;
		if( message.get("cashPageName") instanceof  String ) FileName = (String) message.get("cashPageName") ;
		String dir =  getCashDir(applicationContext ) ;
		try 
		{
			savePageInCash(   FileName ,  dir , htmlBytes ) ;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
		}
	}

	

	void savePageInCash(  String  FileName ,  String  dir ,  byte[] htmlBytes )throws Exception
 	{

 		
		if( FileName == null ) new Exception(" FileName arg == null") ;
		if( dir == null ) new Exception(" dir arg == null") ;
		if( htmlBytes == null ) new Exception(" htmlBytes arg == null") ;
		FileOutputStream fileOutputStream  = null ; 
		File file = null ;
 		try
 		{

 			file  = new File(dir,FileName ) ;
 			if(!file.exists()) file.createNewFile() ;
 			fileOutputStream = new FileOutputStream(file);
 	 		fileOutputStream.write(htmlBytes) ;
	
		} 
		catch (IOException e) 
		{
		log.error(e.getLocalizedMessage()) ;
		}
		finally
		{
			if( fileOutputStream != null ) 
			{	
				try
				{	
					fileOutputStream.close();
				} 
				catch (IOException e) 
				{
					log.error(e.getLocalizedMessage()) ;
				}
			}
		}
 	}



	void savePageInCash(  String  FileName ,  byte[] htmlBytes )throws Exception
 	{

 		
		if( FileName == null ) new Exception(" FileName arg == null") ;
		if( htmlBytes == null ) new Exception(" htmlBytes arg == null") ;
		
		FileOutputStream fileOutputStream  = null ; 
 		//String  FileName =  httpSession.getId() + ".ser"; 
 		//String  FileName =  System.currentTimeMillis() + ".cash";
 		
 		String pageStorePath = this.getClass().getResource("").getPath();
 		pageStorePath = pageStorePath.substring(1, pageStorePath.indexOf("/WEB-INF/"));
		File file = new File(pageStorePath + "//cashes");
 		try
 		{

 			if(!file.exists())
 			{ 
 			  if( file.mkdirs())
 			  {
 				file  = new File(file.getPath(),FileName ) ;
 				file.createNewFile() ;
 			  }
 			}
 			else 
 			{
 				file  = new File(file.getPath(),FileName ) ;
 				if(!file.exists()) file.createNewFile() ;
 			}

 	 		
 			fileOutputStream = new FileOutputStream(file);
 	 		fileOutputStream.write(htmlBytes) ;
 	 		//fileOutputStream.flush() ;
	
		} 
		catch (IOException e) 
		{
		log.error(e.getLocalizedMessage()) ;
		}
		finally
		{
			if( fileOutputStream != null ) 
			{	
				try
				{	
					fileOutputStream.close();
				} 
				catch (IOException e) 
				{
					log.error(e.getLocalizedMessage()) ;
				}
			}
		}
 	}


	
	String getCashDir(ServletContext  servletContext )
	{
		return  (String)servletContext.getAttribute("cash_dir") ;
	}
	
	
}
