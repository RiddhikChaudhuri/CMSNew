package com.cbsinc.cms.services.tomcat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XML11Serializer;




public class AddAliase 
{

 
	DocumentBuilder db = null ;

	public AddAliase() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			 db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public Document loadConfig( File f )
    {
		Document doc = null ;
    	try 
		{
    		doc = db.parse(f);
		}
		catch (SAXException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return doc ;
    }
	
	
	
	public void AddAliaseToHost(Document doc, String hostName , String aliase)
	{
		XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        //Server/Service/Engine
        XPathExpression expr = null;
		try 
		{
			expr = xpath.compile("//Server/Service/Engine/Host[@name='"+hostName+"'  ]");
			//expr = xpath.compile("//Server/Service/Engine/Host[Alias/text()='online-spb.com'  ]");
		}
		catch (XPathExpressionException e) 
		{
			e.printStackTrace();
		}
        Object result = null;
		try 
		{
			result = expr.evaluate(doc, XPathConstants.NODESET);
		} 
		catch (XPathExpressionException e) 
		{
			e.printStackTrace();
		}
		
        NodeList nodes = (NodeList) result;
  
//        for (int i = 0; i < nodes.getLength(); i++) 
//        {
//           System.out.println(nodes.item(i).getNodeValue());
//        }

        if(nodes.getLength() == 1 ) 
        {
        	
        	Element e1 = doc.createElement("Alias");
            Node  n1 = doc.createTextNode(aliase);
            e1.appendChild(n1);
        	nodes.item(0).appendChild(e1);
        }
        
	}
	
	public void RemoveAllAliase(Document doc, String hostName )
	{
		XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        //Server/Service/Engine
        XPathExpression expr = null;
		try 
		{
			expr = xpath.compile("//Server/Service/Engine/Host[@name='"+hostName+"'  ]");
			//expr = xpath.compile("//Server/Service/Engine/Host[Alias/text()='online-spb.com'  ]");
		}
		catch (XPathExpressionException e) 
		{
			e.printStackTrace();
		}
        Object result = null;
		try 
		{
			result = expr.evaluate(doc, XPathConstants.NODESET);
		} 
		catch (XPathExpressionException e) 
		{
			e.printStackTrace();
		}
		
        NodeList nodes = (NodeList) result;
  
        for (int i = 0; i < nodes.getLength(); i++) 
        {
        	NodeList nodes1 = nodes.item(i).getChildNodes() ;
        	for (int y = 0; y < nodes1.getLength(); y++) 
           {
        		if(nodes1.item(y).getNodeName().endsWith("Alias"))
        		{
        			((Element)nodes1).removeChild(nodes1.item(y));        		}
        		//System.out.println(nodes1.item(y).getNodeName());
           		}
           
        }
        
	}
	
//	 This method writes a DOM document to a file
	public static void writeXmlFile(Document document, File file) 
	{
		OutputFormat format = new OutputFormat();
		FileOutputStream fileOutputStream = null ;
		XML11Serializer serializer = null;
		try 
		{
			format.setIndenting(true);
			fileOutputStream = new FileOutputStream(file) ;
			serializer = new XML11Serializer(fileOutputStream, format);
			serializer.serialize(document);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			
			if(fileOutputStream != null)
				try 
				{
					fileOutputStream.close() ;
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
		}

	}

	
	public static void main(String[] args) 
	{
		//File f = new File("C:/apache-tomcat-6.0.14/conf/server.xml") ;
		File f1 = new File("C:/apache-tomcat-6.0.14/conf/server1.xml") ;
		AddAliase al = new AddAliase();
		Document doc = al.loadConfig(f1);

		//al.AddAliaseToHost(doc, "www.online-spb.com", "www.11.ru") ;
		//al.AddAliaseToHost(doc, "www.online-spb.com", "www.22.ru") ;
		//al.AddAliaseToHost(doc, "www.online-spb.com", "www.33.ru") ;
		al.RemoveAllAliase(doc, "www.online-spb.com") ;
		al.writeXmlFile(doc,f1);
		
		
	}
	
	

	
	
}
