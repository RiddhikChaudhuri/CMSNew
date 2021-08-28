package com.cbsinc.cms.dto;

import java.util.List;
import com.cbsinc.cms.dto.pages.request.SubXMLDB;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CatalogNameItem {

  
  private String selected;

  private String item;

  private String code;

  private String url;

  @JsonProperty("subcatalog-item")
  private CatalogSubNameitem subnameitem;
  
  private List<SubXMLDB> subXMLDBList;

  public List<SubXMLDB> getSubXMLDBList() {
    return subXMLDBList;
  }

  public void setSubXMLDBList(List<SubXMLDB> subXMLDBList) {
    this.subXMLDBList = subXMLDBList;
  }

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

  public CatalogSubNameitem getSubnameitem() {
    return subnameitem;
  }

  public void setSubnameitem(CatalogSubNameitem subnameitem) {
    this.subnameitem = subnameitem;
  }

  public CatalogNameItem(String selected, String item, String code, String url,
      CatalogSubNameitem subnameitem) {
    super();
    this.selected = selected;
    this.item = item;
    this.code = code;
    this.url = url;
    this.subnameitem = subnameitem;
  }

  public CatalogNameItem() {
    super();
    // TODO Auto-generated constructor stub
  }
  
  
}
