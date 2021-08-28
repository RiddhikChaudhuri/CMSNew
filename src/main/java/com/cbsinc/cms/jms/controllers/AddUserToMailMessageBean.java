package com.cbsinc.cms.jms.controllers;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.james.domainlist.api.DomainListManagementMBean;
import org.apache.james.user.api.UsersRepositoryManagementMBean;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
@PropertySources(value = {
    @PropertySource(value="classpath:SetupApplicationResources.properties")
  })
public class AddUserToMailMessageBean extends AbstractMessageBean {

  private XLogger logger = XLoggerFactory.getXLogger(AddUserToMailMessageBean.class.getName());
  
   static public String messageQuery = "mq_adduser_to_mail" ;
    
    transient private ResourceBundle resources_cms_settings = null ;

    
    @Value("${james_login}")
    private String james_login;
    
    @Value("${james_password}")
    private String james_password;
    
    @Value("${james_host}")
    private String james_host;

    
    public AddUserToMailMessageBean()
    {
      logger.entry(james_host,james_login,james_password);
      logger.exit();
    }
    

    
    
    

    
    public void onMessage(com.cbsinc.cms.jms.controllers.Message message, ServletContext applicationContext, HttpSession httpSession) 
    {

        String user_login = "" ;
        String user_password = "" ;
        if( message.get("user_login") instanceof  String ) user_login = (String) message.get("user_login") ;
        if( message.get("user_password") instanceof  String ) user_password = (String) message.get("user_password") ;
        
        try 
        {
            
          //mailSettingsSession.exec(login ,password ,user_login ,user_password.substring(0,4)+ user_login,host);    
          addUser(james_login ,james_password ,user_login ,user_password.substring(0,4)+ user_login,james_host , "9999") ;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage());
        }
        
        
    }
    
    
    
     void addUser(String jlogin , String jpassword,  String email , String password, String host , String port  ){
        try{
            String serverUrl = "service:jmx:rmi:///jndi/rmi://"+host+":"+port+"/jmxrmi"; // default port 9999
            String beanNameUser = "org.apache.james:type=component,name=usersrepository";
            String beanNameDomain = "org.apache.james:type=component,name=domainlist";

            MBeanServerConnection server = JMXConnectorFactory.connect(new JMXServiceURL(serverUrl)).getMBeanServerConnection();

            UsersRepositoryManagementMBean userBean =  MBeanServerInvocationHandler.newProxyInstance(server, new ObjectName(beanNameUser), UsersRepositoryManagementMBean.class, false);
            DomainListManagementMBean domainBean =  MBeanServerInvocationHandler.newProxyInstance(server, new ObjectName(beanNameDomain), DomainListManagementMBean.class, false);

            if(domainBean.containsDomain(email.split("@")[1])
                    && !userBean.verifyExists(email)){
                System.out.println("creating email : "+email );
                userBean.addUser(email,password);
                System.out.println(" email : "+email + "was created" );
            }else{
              logger.error("domain does not exist or user already exists !!");
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.error("Something went wrong",e);
        }
    }
    

}
