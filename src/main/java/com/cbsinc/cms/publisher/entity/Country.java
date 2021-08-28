package com.cbsinc.cms.publisher.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="country")
public class Country implements java.io.Serializable {
    
    @EmbeddedId
	private CountryId id;

	public CountryId getId() {
		return id;
	}

	public void setId(CountryId id) {
		this.id = id;
	}

	
}
