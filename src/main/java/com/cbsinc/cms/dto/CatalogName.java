package com.cbsinc.cms.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("catalog")
public class CatalogName {

      @JsonProperty("catalog-item")
      private List<CatalogNameItem> nameItem;

      public void setNameItem(List<CatalogNameItem> nameItem){
          this.nameItem = nameItem;
      }

      public List<CatalogNameItem> getNameItem() {
        return nameItem;
      }
     
}
