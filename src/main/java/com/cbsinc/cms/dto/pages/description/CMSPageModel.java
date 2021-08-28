package com.cbsinc.cms.dto.pages.description;

import java.util.List;

import com.cbsinc.cms.dto.Admin;
import com.cbsinc.cms.dto.Bottom;
import com.cbsinc.cms.dto.CatalogItem;
import com.cbsinc.cms.dto.CurrenciesItem;
import com.cbsinc.cms.dto.MenuItem;
import com.cbsinc.cms.dto.News;
import com.cbsinc.cms.dto.ParentItem;
import com.cbsinc.cms.dto.Product;
import com.cbsinc.cms.dto.ProductBlog;

public class CMSPageModel {
	
	String version ;
	String name ;
	String title ;
	
	String subject_site;
	String site_name;
	String host;
	String domain;
	
	Admin admin;

	String role_id;
	String user_site_id;
	String internet;
	String login;
	String shoping_url;
	String message;
	String balans;
	String to_account_history;
	String to_login;
	String to_registration;
	String to_order;
	String to_order_hist;
	String to_pay;
	String owner_user_id;
	String site_id;
	String show_blog;
	String show_rating1;
	String show_rating2;
	String show_rating3;

	ProductDescription product ;

	String show_star_1 ;
	String show_star_2 ;
	String show_star_3 ;
	String show_star_4 ;
	String show_star_5 ;
	String show_star_6 ;
	String show_star_7 ;
	String show_star_8 ;
	String show_star_9 ;
	String show_star_10 ;

	
	List<CurrenciesItem> currencies ;

	List<Product> extpolicy_productlist1 ;
	
	List<Product> extpolicy_productlist2 ;

	List<Product> extpolicy_file_list ;
	
	List<Product> extpolicy_list_tabs ;

	List<ProductBlog> product_blog_list;

	List<News>newslist;
	
	List<Bottom>bottomlist;

	String empty_page_ext1 ;

	String empty_page_ext2 ;

	List<CatalogItem>catalog;
	
	List<ParentItem>  parent;

	List<MenuItem>menu;

}
