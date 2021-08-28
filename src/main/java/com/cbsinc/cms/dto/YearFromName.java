package com.cbsinc.cms.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class YearFromName{
    
    @JsonProperty("yearfrom-item") 
    public List<NameItem> nameItem;
    
    
    public List<NameItem> getNameItem() {
      return nameItem;
    }

    public void setNameItem(List<NameItem> nameItem) {
      this.nameItem = nameItem;
    }

    public YearFromName(List<NameItem> nameItem) {
      super();
      this.nameItem = nameItem;
    }
    
   
}

