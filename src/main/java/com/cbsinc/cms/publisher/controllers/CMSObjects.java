package com.cbsinc.cms.publisher.controllers;

import java.util.Map;
import java.util.Optional;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.cbsinc.cms.publisher.models.AccountHistoryBean;
import com.cbsinc.cms.publisher.models.AccountHistoryDetalBean;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.CatalogAddBean;
import com.cbsinc.cms.publisher.models.CatalogEditBean;
import com.cbsinc.cms.publisher.models.CatalogListBean;
import com.cbsinc.cms.publisher.models.OperationAmountBean;
import com.cbsinc.cms.publisher.models.OrderBean;
import com.cbsinc.cms.publisher.models.OrderListBean;
import com.cbsinc.cms.publisher.models.PayBean;
import com.cbsinc.cms.publisher.models.PrePayBean;
import com.cbsinc.cms.publisher.models.ProductlistBean;
import com.cbsinc.cms.publisher.models.PublisherBean;
import com.cbsinc.cms.publisher.models.RequestFilter;



public abstract class CMSObjects  {

	private Optional<PublisherBean> publisherBean = Optional.empty();
	private Optional<CatalogListBean> catalogListBean = Optional.empty();
	private Optional<CatalogEditBean> catalogEditBean = Optional.empty();
	private Optional<CatalogAddBean> catalogAddBean = Optional.empty();
	private Optional<AuthorizationPageBean> authorizationPageBean = Optional.empty();
	private Optional<AccountHistoryDetalBean> accountHistoryDetalBean = Optional.empty();
	private Optional<AccountHistoryBean> accountHistoryBean = Optional.empty();
	private Optional<OrderBean> orderBean = Optional.empty();
	private Optional<OrderListBean> orderListBean = Optional.empty();
	private Optional<PayBean> payBean = Optional.empty();
	private Optional<OperationAmountBean> operationAmountBean = Optional.empty();
	private Optional<PrePayBean> prePayBean = Optional.empty();
	private Optional<Map> messageMail = Optional.empty();
	private Optional<ProductlistBean> productListBean = Optional.empty();
	
	
	
	public CMSObjects() {
		
		if (getHttpSession() == null ) return ;
		publisherBean = Optional.ofNullable((PublisherBean) getHttpSession().getAttribute("publisherBeanId"));
		catalogListBean = Optional.ofNullable((CatalogListBean) getHttpSession().getAttribute("catalogListBeanId"));
		catalogEditBean = Optional.ofNullable((CatalogEditBean) getHttpSession().getAttribute("catalogEditBeanId"));
		catalogAddBean = Optional.ofNullable((CatalogAddBean) getHttpSession().getAttribute("catalogAddBeanId"));
		authorizationPageBean = Optional.ofNullable((AuthorizationPageBean) getHttpSession().getAttribute("authorizationPageBeanId"));
		accountHistoryDetalBean = Optional.ofNullable((AccountHistoryDetalBean) getHttpSession().getAttribute("accountHistoryDetalBeanId"));
		accountHistoryBean = Optional.ofNullable((AccountHistoryBean) getHttpSession().getAttribute("accountHistoryBeanId"));
		orderBean = Optional.ofNullable((OrderBean) getHttpSession().getAttribute("orderBeanId"));
		payBean = Optional.ofNullable((PayBean) getHttpSession().getAttribute("payBeanId"));
		messageMail = Optional.ofNullable((Map) getHttpSession().getAttribute("messageMail"));
		operationAmountBean = Optional.ofNullable((OperationAmountBean) getHttpSession().getAttribute("operationAmountBeanId"));
		prePayBean = Optional.ofNullable((PrePayBean) getHttpSession().getAttribute("prePayBeanId"));
		orderListBean = Optional.ofNullable((OrderListBean) getHttpSession().getAttribute("orderListBeanId"));
		productListBean = Optional.ofNullable((ProductlistBean) getHttpSession().getAttribute("productlistBeanId"));
	}
		
	
	public Optional<ProductlistBean> getProductListBean() {
    return productListBean;
  }


  public void setProductListBean(Optional<ProductlistBean> productListBean) {
    this.productListBean = productListBean;
  }


  public Optional<OrderListBean> getOrderListBean() {
    return orderListBean;
  }


  public void setOrderListBean(Optional<OrderListBean> orderListBean) {
    this.orderListBean = orderListBean;
  }


  public Optional<Map> getMessageMail() {
		return messageMail;
	}

	public void setMessageMail(Optional<Map> messageMail) {
		this.messageMail = messageMail;
	}

	public Optional<PublisherBean> getPublisherBean() {
		return publisherBean;
	}

	public void setPublisherBean(Optional<PublisherBean> publisherBean) {
		this.publisherBean = publisherBean;
	}

	public Optional<CatalogListBean> getCatalogListBean() {
		return catalogListBean;
	}

	public void setCatalogListBean(Optional<CatalogListBean> catalogListBean) {
		this.catalogListBean = catalogListBean;
	}

	public Optional<CatalogEditBean> getCatalogEditBean() {
		return catalogEditBean;
	}

	public void setCatalogEditBean(Optional<CatalogEditBean> catalogEditBean) {
		this.catalogEditBean = catalogEditBean;
	}

	public Optional<CatalogAddBean> getCatalogAddBean() {
		return catalogAddBean;
	}

	public void setCatalogAddBean(Optional<CatalogAddBean> catalogAddBean) {
		this.catalogAddBean = catalogAddBean;
	}

	public Optional<AuthorizationPageBean> getAuthorizationPageBean() {
		return authorizationPageBean;
	}

	public void setAuthorizationPageBean(Optional<AuthorizationPageBean> authorizationPageBean) {
		this.authorizationPageBean = authorizationPageBean;
	}

	public Optional<AccountHistoryDetalBean> getAccountHistoryDetalBean() {
		return accountHistoryDetalBean;
	}

	public void setAccountHistoryDetalBean(Optional<AccountHistoryDetalBean> accountHistoryDetalBean) {
		this.accountHistoryDetalBean = accountHistoryDetalBean;
	}

	public Optional<AccountHistoryBean> getAccountHistoryBean() {
		return accountHistoryBean;
	}

	public void setAccountHistoryBean(Optional<AccountHistoryBean> accountHistoryBean) {
		this.accountHistoryBean = accountHistoryBean;
	}

	public Optional<OrderBean> getOrderBean() {
		return orderBean;
	}

	public void setOrderBean(Optional<OrderBean> orderBean) {
		this.orderBean = orderBean;
	}

	public Optional<PayBean> getPayBean() {
		return payBean;
	}

	public void setPayBean(Optional<PayBean> payBean) {
		this.payBean = payBean;
	}

	public Optional<OperationAmountBean> getOperationAmountBean() {
		return operationAmountBean;
	}

	public void setOperationAmountBean(Optional<OperationAmountBean> operationAmountBean) {
		this.operationAmountBean = operationAmountBean;
	}

	public Optional<PrePayBean> getPrePayBean() {
		return prePayBean;
	}

	public void setPrePayBean(Optional<PrePayBean> prePayBean) {
		this.prePayBean = prePayBean;
	}
	
	
	
	
	public HttpSession  getHttpSession()
	{
		return RequestFilter.getSession() ;
	}
	
	public HttpServletRequest  getHttpServletRequest() 
	{
		return RequestFilter.getRequest() ;
	}
	


	public ServletContext  getServletContext() 
	{
		return RequestFilter.getServletContext() ;
	}
	
	public HttpServletResponse  getHttpServletRsponse() 
    {
        return RequestFilter.getResponse();
    }
    
	
	

}
