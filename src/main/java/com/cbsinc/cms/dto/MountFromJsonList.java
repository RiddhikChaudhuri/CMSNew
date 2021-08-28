package com.cbsinc.cms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MountFromJsonList{

  @JsonProperty("mountfrom")
  public MountFormName name;

  
  
    public MountFormName getName() {
      return name;
    }

    public void setName(MountFormName name) {
      this.name = name;
    }

    public MountFromJsonList(MountFormName name) {
      super();
      this.name = name;
    }
    


   
   
}