package com.cbsinc.cms.publisher.controllers;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.response.CMSBlogExtProductResponseDTO;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.PublisherBean;




@RestController
public class SelectSmallImageAction extends CMSObjects
{
  
    private XLogger logger = XLoggerFactory.getXLogger(SelectSmallImageAction.class.getName());

  
	@Autowired
	ProductPostAllFaced productPostAllFaced; 
	
	@PostMapping(value="/doPostSelectSmallImage", consumes = "application/json", produces = "application/json")
	public CMSBlogExtProductResponseDTO action(String image_id) throws Exception {

	    logger.entry();
		
		PublisherBean publisherBeanId = (PublisherBean) getHttpServletRequest().getAttribute("publisherBeanId");
	    AuthorizationPageBean authorizationPageBeanId = (AuthorizationPageBean) getHttpServletRequest().getAttribute("authorizationPageBeanId");
		StringBuffer sbuff = new StringBuffer(); 

  		
        if( image_id != null) 
        {
	       
	        if ( image_id != null ) {  publisherBeanId.setImage_id( image_id );  productPostAllFaced.setImageNameByImage_ID(image_id,publisherBeanId); }
        }
        
        publisherBeanId.setSelect_small_images(productPostAllFaced.getComboBoxWithJavaScriptSmallImage("image_id", publisherBeanId.getImage_id() ,"SELECT image_id,imgname FROM images WHERE user_id = "+ authorizationPageBeanId.getIntUserID() + " ORDER BY image_id DESC ", "onChange=\"changeImage()\"" , publisherBeanId ) );
        
        if(publisherBeanId.getImgname().lastIndexOf(".") != -1){
        sbuff = new StringBuffer(); 
        sbuff.append("imgpositions/") ;
        sbuff.append(publisherBeanId.getImage_id()) ;
        sbuff.append(publisherBeanId.getImgname().substring(publisherBeanId.getImgname().lastIndexOf("."))) ;
        publisherBeanId.setSelect_small_image_url(sbuff.toString());
        }
        else
        {
        	publisherBeanId.setSelect_small_image_url("imgpositions/empty.gif");
        }
        
        logger.exit();
        return new CMSBlogExtProductResponseDTO(publisherBeanId,authorizationPageBeanId);
		
	}

}
