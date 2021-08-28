package com.cbsinc.cms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class YearFromJsonList{

  @JsonProperty("yearfrom")
  public YearFromName name;

  
  
    public YearFromName getName() {
      return name;
    }

    public void setName(YearFromName name) {
      this.name = name;
    }

    public YearFromJsonList(YearFromName name) {
      super();
      this.name = name;
    }
    


   
   
}