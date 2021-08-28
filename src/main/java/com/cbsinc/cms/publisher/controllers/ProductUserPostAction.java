package com.cbsinc.cms.publisher.controllers;

import java.util.Map;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.ProductPostCreForm;
import com.cbsinc.cms.dto.pages.response.CMSNewBlockPostCreResponseModel;
import com.cbsinc.cms.jms.controllers.Message;
import com.cbsinc.cms.jms.controllers.MessageSender;
import com.cbsinc.cms.jms.controllers.SendMailMessageBean;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.CatalogAddBean;
import com.cbsinc.cms.publisher.models.CatalogEditBean;
import com.cbsinc.cms.publisher.models.CatalogListBean;
import com.cbsinc.cms.publisher.models.PublisherBean;

@RestController
public class ProductUserPostAction extends CMSObjects{
  
    private XLogger logger = XLoggerFactory.getXLogger(ProductUserPostAction.class.getName());
  
	private Map messageMail ;
	
	@Autowired
	private ProductPostAllFaced productPostAllFaced ;
	
	private String gen_code = "" ;
	
    @PostMapping(value="/doPostProductUser", consumes = "application/json", produces = "application/json")
	public CMSNewBlockPostCreResponseModel doPost(@RequestBody  ProductPostCreForm productListWebForm) throws Exception 
	{
      gen_code = (String)productListWebForm.getGen_number();
		PublisherBean publisherBeanId   = getPublisherBean().get();
		CatalogListBean catalogListBeanId  = getCatalogListBean().get();
		CatalogEditBean catalogEditBeanId  = getCatalogEditBean().get();
		CatalogAddBean catalogAddBeanId  = getCatalogAddBean().get();
		AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
		messageMail = getMessageMail().get();
		if(publisherBeanId == null || catalogListBeanId == null || catalogEditBeanId == null || catalogAddBeanId == null || authorizationPageBeanId == null || messageMail == null || productPostAllFaced == null ) return null ;
	

		action(productListWebForm) ;
		
			if ( productListWebForm.getGen_number() != null )
			{
				String val1 =productListWebForm.getGen_number().trim() ;
				if(!val1.equals(gen_code.trim())) 
				{
					authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("wrong_gen_number"));
				}
			}
			else
			{
				authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("wrong_gen_number"));
			}
			
		if ( productListWebForm.getBigimage_id() == null ) {  publisherBeanId.setBigimage_id( "-1" ); }
		if ( productListWebForm.getImage_id() == null ) {  publisherBeanId.setImage_id( "-1" ); }	
		publisherBeanId.setSite_id(authorizationPageBeanId.getSite_id());
		if(publisherBeanId.getSoft_id().compareTo("-1")==0)
		{
			if(productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId,true))
			 {
				 authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite")); 
		          return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);
			 }
			productPostAllFaced.saveInformationWithCheck(publisherBeanId,authorizationPageBeanId);
		}
		else productPostAllFaced.updateInformationWithCheck(publisherBeanId,authorizationPageBeanId);

		messageMail.clear();
		messageMail.put("@FirstName", authorizationPageBeanId.getStrFirstName() ) ;
		messageMail.put("@LastName", authorizationPageBeanId.getStrLastName() ) ;
		messageMail.put("@Site", authorizationPageBeanId.getSite_dir() ) ;

		String sitePath = (String)getHttpSession().getAttribute("site_path");
		String addinfo = sitePath +"\\mail\\addinfo.txt" ;
		String attachFile =  sitePath + "\\mail\\info.txt" ;
		/////System.out.println("rezalt: " + sendMailAgent.putMessageInPool(null ,"Has added new info on site ",   addinfo   , attachFile, messageMail  ) ) ;

		MessageSender mqSender = new MessageSender( getHttpSession(),SendMailMessageBean.messageQuery) ;
		Message message = new Message();
		message.put("to" , null  ) ;
		message.put("subject" , "Has added new info on site " ) ;
		message.put("pathmessage" , addinfo ) ;
		message.put("attachFile" , attachFile ) ;
		message.put("fields" , messageMail ) ;
		mqSender.send(message);
	       return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);

		
		
	}
    
    @GetMapping(value="/doGetProductUser", consumes = "application/json", produces = "application/json")
	public CMSNewBlockPostCreResponseModel doGet(@RequestBody ProductPostCreForm productListWebForm) throws Exception 
	{

      gen_code = (String)productListWebForm.getGen_number();
      PublisherBean publisherBeanId   = getPublisherBean().get();
      CatalogListBean catalogListBeanId  = getCatalogListBean().get();
      CatalogEditBean catalogEditBeanId  = getCatalogEditBean().get();
      CatalogAddBean catalogAddBeanId  = getCatalogAddBean().get();
      AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
      messageMail = getMessageMail().get();
        if (publisherBeanId == null || catalogListBeanId == null || catalogEditBeanId == null
            || catalogAddBeanId == null || authorizationPageBeanId == null || messageMail == null
            || productPostAllFaced == null)
          return null;
	
		//if insert and limmit not add message
	    
	    
		action(productListWebForm) ;
		productPostAllFaced.initPage(productListWebForm.getProduct_id(),  publisherBeanId , authorizationPageBeanId);
		if(productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId,false) && publisherBeanId.getSoft_id().compareTo("-1")==0 )
		 {
			 authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite")); 
			 return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);
		 }
		
        return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);


	}

	
	public void action(ProductPostCreForm productListWebForm) throws Exception 
	{
		
	  gen_code = (String)productListWebForm.getGen_number();
      PublisherBean publisherBeanId   = getPublisherBean().get();
      CatalogListBean catalogListBeanId  = getCatalogListBean().get();
      CatalogEditBean catalogEditBeanId  = getCatalogEditBean().get();
      CatalogAddBean catalogAddBeanId  = getCatalogAddBean().get();
      AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
		
		if(publisherBeanId == null || catalogListBeanId == null || catalogEditBeanId == null || catalogAddBeanId == null || authorizationPageBeanId == null || messageMail == null || productPostAllFaced == null ) return ;
	
		
//		 Start Novigator ---
		if( productListWebForm.getParent_id()!= null)
		{
			authorizationPageBeanId.setCatalogParent_id(productListWebForm.getParent_id());
		}

		if(productListWebForm.getRow() != null)
		{
		int index =  catalogListBeanId.stringToInt(productListWebForm.getRow()) ;
		catalogListBeanId.setIndx_select(index);
		}
		if(productListWebForm.getDel()!= null)
		{
		int index =  catalogListBeanId.stringToInt(productListWebForm.getDel()) ;
		String catalog_id = catalogListBeanId.rows[index][0] ;
		if(catalog_id != null)catalogListBeanId.delete(catalog_id,authorizationPageBeanId) ;
		productListWebForm.setDel(null);
		}
		if(productListWebForm.getOffset() != null)
		{
		catalogListBeanId.setOffset(  catalogListBeanId.stringToInt(productListWebForm.getOffset()));
		}
//		 End Novigator ---

		if(productListWebForm.getCreteria1_id() !=null ) publisherBeanId.setCreteria1_id(productListWebForm.getCreteria1_id());
	     if( productListWebForm.getCreteria2_id() !=null ) publisherBeanId.setCreteria2_id( productListWebForm.getCreteria2_id());
	     if( productListWebForm.getCreteria3_id() !=null ) publisherBeanId.setCreteria3_id( productListWebForm.getCreteria3_id());
	     if( productListWebForm.getCreteria4_id()!=null ) publisherBeanId.setCreteria4_id( productListWebForm.getCreteria4_id());
	     if( productListWebForm.getCreteria5_id() !=null ) publisherBeanId.setCreteria5_id(  productListWebForm.getCreteria5_id());
	     if( productListWebForm.getCreteria6_id() !=null ) publisherBeanId.setCreteria6_id(  productListWebForm.getCreteria6_id());
	     if( productListWebForm.getCreteria7_id() !=null ) publisherBeanId.setCreteria7_id(  productListWebForm.getCreteria7_id());
	     if( productListWebForm.getCreteria8_id() !=null ) publisherBeanId.setCreteria8_id( productListWebForm.getCreteria8_id());
	     if( productListWebForm.getCreteria9_id() !=null ) publisherBeanId.setCreteria9_id(  productListWebForm.getCreteria9_id());
	     if( productListWebForm.getCreteria10_id()!=null ) publisherBeanId.setCreteria10_id( productListWebForm.getCreteria10_id());

         String softname = productListWebForm.getSoftname();
         if (softname != null) {
           publisherBeanId.setStrSoftName(softname);
         }

         String catalog_id = productListWebForm.getCatalog_id();
         if (catalog_id != null) {
           authorizationPageBeanId.setCatalog_id(catalog_id);
         }

         String softcost = productListWebForm.getSoftcost();
         if (softcost != null) {
           publisherBeanId.setStrSoftCost(softcost);
         }

         String currency_id = productListWebForm.getCurrency_id();
         if (currency_id != null) {
           publisherBeanId.setStrCurrency(currency_id);
         }

         String description = productListWebForm.getDescription();
         if (description != null) {
           publisherBeanId.setStrSoftDescription(description);
         }

         String fulldescription = productListWebForm.getFulldescription();
         if (fulldescription != null) {
           publisherBeanId.setProduct_fulldescription(fulldescription);
         }

         String imagename = productListWebForm.getImagename();
         if (imagename != null) {
           publisherBeanId.setImgname(imagename);
         }

         String image_id = productListWebForm.getImage_id();
         if (image_id != null) {
           publisherBeanId.setImage_id(image_id);
         }



		if ( productListWebForm.getPortlettype_id() != null ) {  publisherBeanId.setPortlettype_id( productListWebForm.getPortlettype_id()); }

		String   filename  = productListWebForm.getFilename();
		if ( filename != null ) {  publisherBeanId.setSample( filename ); }
		else{ publisherBeanId.setSample(""); }
		filename = null ;



        String bigimagename = productListWebForm.getBigimagename();
        if (bigimagename != null) {
          publisherBeanId.setBigimgname(bigimagename);
        } else
          publisherBeanId.setBigimgname("-1");

        String bigimage_id = productListWebForm.getBigimage_id();
        if (bigimage_id != null) {
          publisherBeanId.setBigimage_id(bigimage_id);
        }


        if (productListWebForm.getSalelogic_id() != null)
          publisherBeanId.setProgname_id(productListWebForm.getSalelogic_id());

        if (authorizationPageBeanId.getIntUserID() == 0) {
          authorizationPageBeanId.setStrMessage(authorizationPageBeanId
              .getLocalization(getServletContext()).getString("session_time_out"));
        } else
          publisherBeanId.setUser_id("" + authorizationPageBeanId.getIntUserID());

        if (authorizationPageBeanId.getIntLevelUp() == 0) {
          authorizationPageBeanId.setStrMessage(authorizationPageBeanId
              .getLocalization(getServletContext()).getString("post_message_notaccess_admin"));
        }
		
		
	}
}
