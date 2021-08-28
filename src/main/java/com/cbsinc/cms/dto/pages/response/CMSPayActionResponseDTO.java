package com.cbsinc.cms.dto.pages.response;

import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.PayBean;

public class CMSPayActionResponseDTO extends PageModel {

  public CMSPayActionResponseDTO(AuthorizationPageBean authorizationPageBeanId, PayBean payBeanId) {

    this.firstname = authorizationPageBeanId.getStrFirstName();
    this.lastname = authorizationPageBeanId.getStrLastName();
    this.company = authorizationPageBeanId.getStrCompany();
    this.email = authorizationPageBeanId.getStrEMail();
    this.phone = authorizationPageBeanId.getStrPhone();
    this.mphone = authorizationPageBeanId.getStrMPhone();
    this.fax = authorizationPageBeanId.getStrFax();
    this.icq = authorizationPageBeanId.getStrIcq();
    this.website = authorizationPageBeanId.getStrWebsite();
    this.question = authorizationPageBeanId.getStrQuestion();
    this.answer = authorizationPageBeanId.getStrAnswer();
    this.country = authorizationPageBeanId.getStrCountry();
    this.city = authorizationPageBeanId.getStrCity();
    this.site = authorizationPageBeanId.getSite_id();
    this.message = authorizationPageBeanId.getStrMessage();
    this.country_id = authorizationPageBeanId.getCountry_id();
    this.city_id = authorizationPageBeanId.getCity_id();
    this.currency_id = authorizationPageBeanId.getCurrency_id();
    this.amount = payBeanId.getAmount();
    this.currency_paysys_id = payBeanId.getCurrency_cd();
    this.account_history_id = payBeanId.getAccount_hist_id();
    this.shop_paysys_id = authorizationPageBeanId.getPaysys_shop_cd();
    this.type_creditcard = payBeanId.getChoosenTypeCreditCard();
    this.card_payment = payBeanId.getCardPayment();
    this.wallet_payment = payBeanId.getWalletPayment();
    this.webmoney_payment = payBeanId.getWebMoneyPayment();
    this.papida_payment = payBeanId.getRapidaPayment();
    this.paycash_payment = payBeanId.getPayCashPayment();
    this.eport_payment = payBeanId.getEPortPayment();
    this.kredit_pilotpayment = payBeanId.getKreditPilotPayment();

    this.description = payBeanId.getDescription();
  }

  String firstname;
  String lastname;
  String email;
  String company;
  String phone;
  String mphone;
  String fax;
  String icq;
  String website;
  String question;
  String answer;
  String country;
  String message;
  String city;
  String site;
  String country_id;
  String city_id;
  String currency_id;
  String amount;
  String currency_paysys_id;
  String account_history_id;
  String shop_paysys_id;
  String type_creditcard;
  String card_payment;
  String wallet_payment;
  String webmoney_payment;
  String papida_payment;
  String paycash_payment;
  String eport_payment;
  String kredit_pilotpayment;
  String description;


}
