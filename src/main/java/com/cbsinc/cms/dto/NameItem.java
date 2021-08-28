package com.cbsinc.cms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NameItem{
  
    public SubNameItem getSubnameItem() {
    return subnameItem;
  }
  public void setSubnameItem(SubNameItem subnameItem) {
    this.subnameItem = subnameItem;
  }
    public NameItem() {
    super();
  }
    public String selected;
    public String item;
    public String code;
    public String url;
    
    @JsonProperty("subname-item") 
    public SubNameItem subnameItem;
    
    public String getSelected() {
      return selected;
    }
    public void setSelected(String selected) {
      this.selected = selected;
    }
    public String getItem() {
      return item;
    }
    public void setItem(String item) {
      this.item = item;
    }
    public String getCode() {
      return code;
    }
    public void setCode(String code) {
      this.code = code;
    }
    public String getUrl() {
      return url;
    }
    public void setUrl(String url) {
      this.url = url;
    }
    
    public NameItem(String selected, String item, String code, String url, SubNameItem subnameItem) {
      super();
      this.selected = selected;
      this.item = item;
      this.code = code;
      this.url = url;
      this.subnameItem=subnameItem;
    }
    public NameItem(String selected, String item, String code, String url) {
      super();
      this.selected = selected;
      this.item = item;
      this.code = code;
      this.url = url;
    }
    
    
}

