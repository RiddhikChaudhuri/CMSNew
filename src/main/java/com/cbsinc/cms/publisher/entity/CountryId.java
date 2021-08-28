package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CountryId implements java.io.Serializable {
    
    @Column(name="COUNTRY_ID")
	private Long countryId;
    
    @Column(name="TELCODE")
	private Integer telcode;
    
    @Column(name="NAME",length=50)
	private String name;
    
    @Column(name="FULLNAME",length=100)
	private String fullname;
    
    @Column(name="LANG_ID")
	private Long langId;
    
    @Column(name="LOCALE",length=2)
	private String locale;

	public CountryId() {
	}

	public CountryId(Long countryId, Integer telcode, String name,
			String fullname, Long langId, String locale) {
		this.countryId = countryId;
		this.telcode = telcode;
		this.name = name;
		this.fullname = fullname;
		this.langId = langId;
		this.locale = locale;
	}

	public Long getCountryId() {
		return this.countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Integer getTelcode() {
		return this.telcode;
	}

	public void setTelcode(Integer telcode) {
		this.telcode = telcode;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullname() {
		return this.fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Long getLangId() {
		return this.langId;
	}

	public void setLangId(Long langId) {
		this.langId = langId;
	}

	public String getLocale() {
		return this.locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CountryId))
			return false;
		CountryId castOther = (CountryId) other;

		return ((this.getCountryId() == castOther.getCountryId()) || (this
				.getCountryId() != null && castOther.getCountryId() != null && this
				.getCountryId().equals(castOther.getCountryId())))
				&& ((this.getTelcode() == castOther.getTelcode()) || (this
						.getTelcode() != null && castOther.getTelcode() != null && this
						.getTelcode().equals(castOther.getTelcode())))
				&& ((this.getName() == castOther.getName()) || (this.getName() != null
						&& castOther.getName() != null && this.getName()
						.equals(castOther.getName())))
				&& ((this.getFullname() == castOther.getFullname()) || (this
						.getFullname() != null
						&& castOther.getFullname() != null && this
						.getFullname().equals(castOther.getFullname())))
				&& ((this.getLangId() == castOther.getLangId()) || (this
						.getLangId() != null && castOther.getLangId() != null && this
						.getLangId().equals(castOther.getLangId())))
				&& ((this.getLocale() == castOther.getLocale()) || (this
						.getLocale() != null && castOther.getLocale() != null && this
						.getLocale().equals(castOther.getLocale())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCountryId() == null ? 0 : this.getCountryId().hashCode());
		result = 37 * result
				+ (getTelcode() == null ? 0 : this.getTelcode().hashCode());
		result = 37 * result
				+ (getName() == null ? 0 : this.getName().hashCode());
		result = 37 * result
				+ (getFullname() == null ? 0 : this.getFullname().hashCode());
		result = 37 * result
				+ (getLangId() == null ? 0 : this.getLangId().hashCode());
		result = 37 * result
				+ (getLocale() == null ? 0 : this.getLocale().hashCode());
		return result;
	}

}
