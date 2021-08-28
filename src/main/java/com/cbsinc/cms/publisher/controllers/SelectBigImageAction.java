package com.cbsinc.cms.publisher.controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.PublisherBean;



@RestController
public class SelectBigImageAction extends CMSObjects {
    @Autowired
    PublisherBean publisherBeanId;

    @Autowired
    ProductPostAllFaced productPostAllFaced;

    @Autowired
    AuthorizationPageBean authorizationPageBeanId;

    @PostMapping(value = "/doPostSelectBigImage", consumes = "application/json",produces = "application/json")
    public PublisherBean action(@RequestParam("bigimage_id")Optional<String> bigimage_id) throws Exception {
        StringBuffer sbuff = new StringBuffer();

        if (bigimage_id.isPresent()) {
            if (bigimage_id != null) {
                publisherBeanId.setBigimage_id(bigimage_id.get());
                productPostAllFaced.setBigImageNameByImage_ID(bigimage_id.get(), publisherBeanId);
            }
        }

        publisherBeanId.setSelect_big_images(productPostAllFaced.getComboBoxWithJavaScriptBigImage(
            "bigimage_id", publisherBeanId.getBigimage_id(),
            "SELECT big_images_id,imgname FROM big_images WHERE user_id = " +
            authorizationPageBeanId.getIntUserID() + " ORDER BY big_images_id DESC ",
            "onChange=\"changeImage()\"", publisherBeanId));

        if (publisherBeanId.getBigimgname().lastIndexOf(".") != -1) {
            sbuff = new StringBuffer();
            sbuff.append("big_imgpositions/");
            sbuff.append(publisherBeanId.getBigimage_id());
            sbuff.append(publisherBeanId.getBigimgname()
                .substring(publisherBeanId.getBigimgname().lastIndexOf(".")));
            publisherBeanId.setSelect_big_image_url(sbuff.toString());
        } else {
            publisherBeanId.setSelect_big_image_url("big_imgpositions/empty.gif");
        }

        return publisherBeanId;


    }



}