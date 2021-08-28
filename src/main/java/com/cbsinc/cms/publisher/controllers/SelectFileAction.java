package com.cbsinc.cms.publisher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.PublisherBean;


@RestController
public class SelectFileAction extends CMSObjects{
  
  @Autowired
  ProductPostAllFaced productPostAllFaced;

  @PostMapping(value="/doPostSelectFile", consumes = "application/json", produces = "application/json")
	public PublisherBean action(String file_id) throws Exception {	
		PublisherBean publisherBeanId = (PublisherBean) getHttpSession().getAttribute("publisherBeanId");
		AuthorizationPageBean authorizationPageBeanId = (AuthorizationPageBean) getHttpSession().getAttribute("authorizationPageBeanId");
		 		
        if( file_id != null) 
        {
	        if ( file_id != null ) {  publisherBeanId.setFile_id( file_id );  productPostAllFaced.setFileNameByFile_ID(file_id,publisherBeanId); }
        }
        
        publisherBeanId.setSelect_files(productPostAllFaced.getComboBoxWithJavaScriptBigImage("file_id", publisherBeanId.getFile_id() ,"select file_id , name  from file  where  user_id = "+ authorizationPageBeanId.getIntUserID() + " ORDER BY file_id DESC" , "onChange=\"changeImage()\"" , publisherBeanId  ) );
        return publisherBeanId;
	}
	

}
