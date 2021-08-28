package com.cbsinc.cms.dto;

import java.util.Collections;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class SubNameItem {

  String subselected;
  String subitem;
  String subcode;
  String suburl;
  
  @JsonIgnore
  public String name;
  
  @JsonAnyGetter
  public Map<String, Object> any() {
    //add the custom name here
    //use full HashMap if you need more than one property
    return Collections.singletonMap("sub"+name+"-item", this);
  }

  public String getSubselected() {
    return subselected;
  }

  public void setSubselected(String subselected) {
    this.subselected = subselected;
  }

  public String getSubitem() {
    return subitem;
  }

  public void setSubitem(String subitem) {
    this.subitem = subitem;
  }

  public String getSubcode() {
    return subcode;
  }

  public void setSubcode(String subcode) {
    this.subcode = subcode;
  }

  public String getSuburl() {
    return suburl;
  }

  public void setSuburl(String suburl) {
    this.suburl = suburl;
  }

  public SubNameItem(String subselected, String subitem, String subcode, String suburl,String name) {
    super();
    this.subselected = subselected;
    this.subitem = subitem;
    this.subcode = subcode;
    this.suburl = suburl;
    this.name= name;
  }


  
  
}
