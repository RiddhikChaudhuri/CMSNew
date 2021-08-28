package com.cbsinc.cms.publisher.entity;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name="tuser")
public class Tuser implements java.io.Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="USER_ID")
	private long userId;
    
    @Column(name="LOGIN",length=50)
	private String login;
    
    @Column(name="PASSWD",length=50)
	private String passwd;
    
    @Column(name="FIRST_NAME",length=50)
	private String firstName;
    
    @Column(name="LAST_NAME",length=50)
	private String lastName;
    
    @Column(name="COMPANY",length=50)
	private String company;
    
    @Column(name="E_MAIL",length=50)
	private String EMail;
    
    @Column(name="PHONE",length=50)
	private String phone;
    
    @Column(name="MOBIL_PHONE",length=50)
	private String mobilPhone;
    
    @Column(name="FAX",length=50)
	private String fax;
    
    @Column(name="ICQ",length=50)
	private String icq;
    
    @Column(name="WEBSITE",length=100)
	private String website;
    
    @Column(name="QUESTION",length=200)
	private String question;
    
    @Column(name="ANSWER",length=200)
	private String answer;
    
    @Column(name="IDSESSION",length=50)
	private String idsession;
    
    @Column(name="BIRTHDAY",length=19,nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
	private Date birthday;
    
    @Column(name="REGDATE",length=19,nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
	private Date regdate;
    
    @Column(name="LEVELUP_CD")
	private Long levelupCd;
    
    @Column(name="BANK_CD")
	private Long bankCd;
    
    @Column(name="ACVIVE_SESSION")
	private Boolean acviveSession;
    
    @Column(name="ACTIVE",nullable = false)
	private boolean active;
    
    @Column(name="COUNTRY",length=20)
	private String country;
    
    @Column(name="CITY",length=20)
	private String city;
    
    @Column(name="ZIP",length=10)
	private String zip;
    
    @Column(name="STATE",length=10)
	private String state;
    
    @Column(name="SCOUNTRY",length=50)
	private String scountry;
    
    @Column(name="MIDDLENAME",length=50)
	private String middlename;
    
    @Column(name="CITY_ID")
	private Long cityId;
    
    @Column(name="COUNTRY_ID")
	private Long countryId;
    
    @Column(name="CURRENCY_ID")
	private Long currencyId;
    
    @Column(name="TREE_ID")
	private Long treeId;
    
    @Column(name="SITE_ID",nullable = false)
	private long siteId;

	public Tuser() {
	}

	public Tuser(long userId, Date birthday, Date regdate, boolean active,
			long siteId) {
		this.userId = userId;
		this.birthday = birthday;
		this.regdate = regdate;
		this.active = active;
		this.siteId = siteId;
	}

	public Tuser(long userId, String login, String passwd, String firstName,
			String lastName, String company, String EMail, String phone,
			String mobilPhone, String fax, String icq, String website,
			String question, String answer, String idsession, Date birthday,
			Date regdate, Long levelupCd, Long bankCd, Boolean acviveSession,
			boolean active, String country, String city, String zip,
			String state, String scountry, String middlename, Long cityId,
			Long countryId, Long currencyId, Long treeId, long siteId) {
		this.userId = userId;
		this.login = login;
		this.passwd = passwd;
		this.firstName = firstName;
		this.lastName = lastName;
		this.company = company;
		this.EMail = EMail;
		this.phone = phone;
		this.mobilPhone = mobilPhone;
		this.fax = fax;
		this.icq = icq;
		this.website = website;
		this.question = question;
		this.answer = answer;
		this.idsession = idsession;
		this.birthday = birthday;
		this.regdate = regdate;
		this.levelupCd = levelupCd;
		this.bankCd = bankCd;
		this.acviveSession = acviveSession;
		this.active = active;
		this.country = country;
		this.city = city;
		this.zip = zip;
		this.state = state;
		this.scountry = scountry;
		this.middlename = middlename;
		this.cityId = cityId;
		this.countryId = countryId;
		this.currencyId = currencyId;
		this.treeId = treeId;
		this.siteId = siteId;
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getEMail() {
		return this.EMail;
	}

	public void setEMail(String EMail) {
		this.EMail = EMail;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobilPhone() {
		return this.mobilPhone;
	}

	public void setMobilPhone(String mobilPhone) {
		this.mobilPhone = mobilPhone;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getIcq() {
		return this.icq;
	}

	public void setIcq(String icq) {
		this.icq = icq;
	}

	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getIdsession() {
		return this.idsession;
	}

	public void setIdsession(String idsession) {
		this.idsession = idsession;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getRegdate() {
		return this.regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}

	public Long getLevelupCd() {
		return this.levelupCd;
	}

	public void setLevelupCd(Long levelupCd) {
		this.levelupCd = levelupCd;
	}

	public Long getBankCd() {
		return this.bankCd;
	}

	public void setBankCd(Long bankCd) {
		this.bankCd = bankCd;
	}

	public Boolean getAcviveSession() {
		return this.acviveSession;
	}

	public void setAcviveSession(Boolean acviveSession) {
		this.acviveSession = acviveSession;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getScountry() {
		return this.scountry;
	}

	public void setScountry(String scountry) {
		this.scountry = scountry;
	}

	public String getMiddlename() {
		return this.middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public Long getCityId() {
		return this.cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getCountryId() {
		return this.countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Long getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}

	public Long getTreeId() {
		return this.treeId;
	}

	public void setTreeId(Long treeId) {
		this.treeId = treeId;
	}

	public long getSiteId() {
		return this.siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

}
