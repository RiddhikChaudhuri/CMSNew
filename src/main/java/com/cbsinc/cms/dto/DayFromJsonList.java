package com.cbsinc.cms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DayFromJsonList{

  @JsonProperty("dayfrom")
  public DayFromName name;

  
  
    public DayFromName getName() {
      return name;
    }

    public void setName(DayFromName name) {
      this.name = name;
    }

    public DayFromJsonList(DayFromName name) {
      super();
      this.name = name;
    }
    


   
   
}