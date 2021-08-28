package com.cbsinc.cms.publisher.entity;

// Generated 29.03.2014 21:04:05 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity(name = "soft")
public class Soft implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "SOFT_ID")
  private long softId;

  @Column(name = "VERSION", length = 10)
  private String version;

  @Column(name = "NAME", length = 50)
  private String name;

  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @Column(name = "COST", precision = 22, scale = 0, nullable = false)
  private double cost;

  @Column(name = "CURRENCY", nullable = false)
  private int currency;

  @Column(name = "SERIAL_NUBMER", length = 500)
  private String serialNubmer;

  @Column(name = "FILE_ID")
  private Long fileId;

  @Column(name = "TYPE_ID")
  private Long typeId;

  @Column(name = "LEVELUP")
  private Long levelup;

  @Column(name = "ACTIVE", nullable = false)
  private boolean active;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "PHONETYPE_ID")
  private Integer phonetypeId;

  @Column(name = "PROGNAME_ID")
  private Long prognameId;

  @Column(name = "IMAGE_ID")
  private Long imageId;

  @Column(name = "BIGIMAGE_ID")
  private Long bigimageId;

  @Column(name = "WEIGHT", precision = 22, scale = 0)
  private Double weight;

  @Column(name = "COUNT")
  private Integer count;

  @Column(name = "LANG_ID")
  private Long langId;

  @Column(name = "PHONEMODEL_ID")
  private Integer phonemodelId;


  @Column(name = "LICENCE_ID")
  private Long licenceId;


  private Long catalogId;
  private Long salelogicId;
  private Long treeId;
  private Integer cardNumber;
  private Integer cardCode;
  private Date startDialTime;
  private Date endDialTime;
  private boolean startActivationCard;
  private boolean endActivationCard;
  private Integer typeCardId;
  private Integer productCode;
  private String fulldescription;
  private Long siteId;
  private String search;
  private long portlettypeId;
  private Long creteria1Id;
  private Long creteria2Id;
  private Long creteria3Id;
  private Long creteria4Id;
  private Long creteria5Id;
  private Long creteria6Id;
  private Long creteria7Id;
  private Long creteria8Id;
  private Long creteria9Id;
  private Long creteria10Id;
  

  @Column(name = "STATISTIC_ID")
  @NotNull
  private long statisticId;
  
  
  private Date cdate;
  private Long creteria11Id;
  private Long creteria12Id;
  private Long creteria13Id;
  private Long creteria14Id;
  private Long creteria15Id;
  private Integer square;
  private int ratingSumm1;
  private int ratingSumm2;
  private int ratingSumm3;
  private int countpostRating1;
  private int countpostRating2;
  private int countpostRating3;
  private int midleBal1;
  private int midleBal2;
  private int midleBal3;
  private boolean showRating1;
  private boolean showRating2;
  private boolean showRating3;
  private boolean showBlog;
  private String name2;
  private String search2;
  private double amount1;
  private double amount2;
  private double amount3;
  private String jspUrl;
  private String color;

  public Soft() {}

  public Soft(long softId, double cost, int currency, boolean active, Date startDialTime,
      boolean startActivationCard, boolean endActivationCard, long portlettypeId, long statisticId,
      Date cdate, int ratingSumm1, int ratingSumm2, int ratingSumm3, int countpostRating1,
      int countpostRating2, int countpostRating3, int midleBal1, int midleBal2, int midleBal3,
      boolean showRating1, boolean showRating2, boolean showRating3, boolean showBlog,
      double amount1, double amount2, double amount3) {
    this.softId = softId;
    this.cost = cost;
    this.currency = currency;
    this.active = active;
    this.startDialTime = startDialTime;
    this.startActivationCard = startActivationCard;
    this.endActivationCard = endActivationCard;
    this.portlettypeId = portlettypeId;
    this.statisticId = statisticId;
    this.cdate = cdate;
    this.ratingSumm1 = ratingSumm1;
    this.ratingSumm2 = ratingSumm2;
    this.ratingSumm3 = ratingSumm3;
    this.countpostRating1 = countpostRating1;
    this.countpostRating2 = countpostRating2;
    this.countpostRating3 = countpostRating3;
    this.midleBal1 = midleBal1;
    this.midleBal2 = midleBal2;
    this.midleBal3 = midleBal3;
    this.showRating1 = showRating1;
    this.showRating2 = showRating2;
    this.showRating3 = showRating3;
    this.showBlog = showBlog;
    this.amount1 = amount1;
    this.amount2 = amount2;
    this.amount3 = amount3;
  }

  public Soft(long softId, String name, String description, double cost, int currency,
      String serialNubmer, Long fileId, Long typeId, Long levelup, boolean active, Long userId,
      Integer phonetypeId, Long prognameId, Long imageId, Long bigimageId, Double weight,
      Integer count, Long langId, Integer phonemodelId, Long licenceId, Long catalogId,
      Long salelogicId, Long treeId, Integer cardNumber, Integer cardCode, Date startDialTime,
      Date endDialTime, boolean startActivationCard, boolean endActivationCard, Integer typeCardId,
      Integer productCode, String fulldescription, Long siteId, String search, long portlettypeId,
      Long creteria1Id, Long creteria2Id, Long creteria3Id, Long creteria4Id, Long creteria5Id,
      Long creteria6Id, Long creteria7Id, Long creteria8Id, Long creteria9Id, Long creteria10Id,
      long statisticId, Date cdate, Long creteria11Id, Long creteria12Id, Long creteria13Id,
      Long creteria14Id, Long creteria15Id, Integer square, int ratingSumm1, int ratingSumm2,
      int ratingSumm3, int countpostRating1, int countpostRating2, int countpostRating3,
      int midleBal1, int midleBal2, int midleBal3, boolean showRating1, boolean showRating2,
      boolean showRating3, boolean showBlog, String name2, String search2, double amount1,
      double amount2, double amount3, String jspUrl, String color) {
    this.softId = softId;
    this.name = name;
    this.description = description;
    this.cost = cost;
    this.currency = currency;
    this.serialNubmer = serialNubmer;
    this.fileId = fileId;
    this.typeId = typeId;
    this.levelup = levelup;
    this.active = active;
    this.userId = userId;
    this.phonetypeId = phonetypeId;
    this.prognameId = prognameId;
    this.imageId = imageId;
    this.bigimageId = bigimageId;
    this.weight = weight;
    this.count = count;
    this.langId = langId;
    this.phonemodelId = phonemodelId;
    this.licenceId = licenceId;
    this.catalogId = catalogId;
    this.salelogicId = salelogicId;
    this.treeId = treeId;
    this.cardNumber = cardNumber;
    this.cardCode = cardCode;
    this.startDialTime = startDialTime;
    this.endDialTime = endDialTime;
    this.startActivationCard = startActivationCard;
    this.endActivationCard = endActivationCard;
    this.typeCardId = typeCardId;
    this.productCode = productCode;
    this.fulldescription = fulldescription;
    this.siteId = siteId;
    this.search = search;
    this.portlettypeId = portlettypeId;
    this.creteria1Id = creteria1Id;
    this.creteria2Id = creteria2Id;
    this.creteria3Id = creteria3Id;
    this.creteria4Id = creteria4Id;
    this.creteria5Id = creteria5Id;
    this.creteria6Id = creteria6Id;
    this.creteria7Id = creteria7Id;
    this.creteria8Id = creteria8Id;
    this.creteria9Id = creteria9Id;
    this.creteria10Id = creteria10Id;
    this.statisticId = statisticId;
    this.cdate = cdate;
    this.creteria11Id = creteria11Id;
    this.creteria12Id = creteria12Id;
    this.creteria13Id = creteria13Id;
    this.creteria14Id = creteria14Id;
    this.creteria15Id = creteria15Id;
    this.square = square;
    this.ratingSumm1 = ratingSumm1;
    this.ratingSumm2 = ratingSumm2;
    this.ratingSumm3 = ratingSumm3;
    this.countpostRating1 = countpostRating1;
    this.countpostRating2 = countpostRating2;
    this.countpostRating3 = countpostRating3;
    this.midleBal1 = midleBal1;
    this.midleBal2 = midleBal2;
    this.midleBal3 = midleBal3;
    this.showRating1 = showRating1;
    this.showRating2 = showRating2;
    this.showRating3 = showRating3;
    this.showBlog = showBlog;
    this.name2 = name2;
    this.search2 = search2;
    this.amount1 = amount1;
    this.amount2 = amount2;
    this.amount3 = amount3;
    this.jspUrl = jspUrl;
    this.color = color;
  }

  public long getSoftId() {
    return this.softId;
  }

  public void setSoftId(long softId) {
    this.softId = softId;
  }

  public String getVersion() {
    return this.version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getCost() {
    return this.cost;
  }

  public void setCost(double cost) {
    this.cost = cost;
  }

  public int getCurrency() {
    return this.currency;
  }

  public void setCurrency(int currency) {
    this.currency = currency;
  }

  public String getSerialNubmer() {
    return this.serialNubmer;
  }

  public void setSerialNubmer(String serialNubmer) {
    this.serialNubmer = serialNubmer;
  }

  public Long getFileId() {
    return this.fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public Long getTypeId() {
    return this.typeId;
  }

  public void setTypeId(Long typeId) {
    this.typeId = typeId;
  }

  public Long getLevelup() {
    return this.levelup;
  }

  public void setLevelup(Long levelup) {
    this.levelup = levelup;
  }

  public boolean isActive() {
    return this.active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Long getUserId() {
    return this.userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Integer getPhonetypeId() {
    return this.phonetypeId;
  }

  public void setPhonetypeId(Integer phonetypeId) {
    this.phonetypeId = phonetypeId;
  }

  public Long getPrognameId() {
    return this.prognameId;
  }

  public void setPrognameId(Long prognameId) {
    this.prognameId = prognameId;
  }

  public Long getImageId() {
    return this.imageId;
  }

  public void setImageId(Long imageId) {
    this.imageId = imageId;
  }

  public Long getBigimageId() {
    return this.bigimageId;
  }

  public void setBigimageId(Long bigimageId) {
    this.bigimageId = bigimageId;
  }

  public Double getWeight() {
    return this.weight;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public Integer getCount() {
    return this.count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Long getLangId() {
    return this.langId;
  }

  public void setLangId(Long langId) {
    this.langId = langId;
  }

  public Integer getPhonemodelId() {
    return this.phonemodelId;
  }

  public void setPhonemodelId(Integer phonemodelId) {
    this.phonemodelId = phonemodelId;
  }

  public Long getLicenceId() {
    return this.licenceId;
  }

  public void setLicenceId(Long licenceId) {
    this.licenceId = licenceId;
  }

  public Long getCatalogId() {
    return this.catalogId;
  }

  public void setCatalogId(Long catalogId) {
    this.catalogId = catalogId;
  }

  public Long getSalelogicId() {
    return this.salelogicId;
  }

  public void setSalelogicId(Long salelogicId) {
    this.salelogicId = salelogicId;
  }

  public Long getTreeId() {
    return this.treeId;
  }

  public void setTreeId(Long treeId) {
    this.treeId = treeId;
  }

  public Integer getCardNumber() {
    return this.cardNumber;
  }

  public void setCardNumber(Integer cardNumber) {
    this.cardNumber = cardNumber;
  }

  public Integer getCardCode() {
    return this.cardCode;
  }

  public void setCardCode(Integer cardCode) {
    this.cardCode = cardCode;
  }

  public Date getStartDialTime() {
    return this.startDialTime;
  }

  public void setStartDialTime(Date startDialTime) {
    this.startDialTime = startDialTime;
  }

  public Date getEndDialTime() {
    return this.endDialTime;
  }

  public void setEndDialTime(Date endDialTime) {
    this.endDialTime = endDialTime;
  }

  public boolean isStartActivationCard() {
    return this.startActivationCard;
  }

  public void setStartActivationCard(boolean startActivationCard) {
    this.startActivationCard = startActivationCard;
  }

  public boolean isEndActivationCard() {
    return this.endActivationCard;
  }

  public void setEndActivationCard(boolean endActivationCard) {
    this.endActivationCard = endActivationCard;
  }

  public Integer getTypeCardId() {
    return this.typeCardId;
  }

  public void setTypeCardId(Integer typeCardId) {
    this.typeCardId = typeCardId;
  }

  public Integer getProductCode() {
    return this.productCode;
  }

  public void setProductCode(Integer productCode) {
    this.productCode = productCode;
  }

  public String getFulldescription() {
    return this.fulldescription;
  }

  public void setFulldescription(String fulldescription) {
    this.fulldescription = fulldescription;
  }

  public Long getSiteId() {
    return this.siteId;
  }

  public void setSiteId(Long siteId) {
    this.siteId = siteId;
  }

  public String getSearch() {
    return this.search;
  }

  public void setSearch(String search) {
    this.search = search;
  }

  public long getPortlettypeId() {
    return this.portlettypeId;
  }

  public void setPortlettypeId(long portlettypeId) {
    this.portlettypeId = portlettypeId;
  }

  public Long getCreteria1Id() {
    return this.creteria1Id;
  }

  public void setCreteria1Id(Long creteria1Id) {
    this.creteria1Id = creteria1Id;
  }

  public Long getCreteria2Id() {
    return this.creteria2Id;
  }

  public void setCreteria2Id(Long creteria2Id) {
    this.creteria2Id = creteria2Id;
  }

  public Long getCreteria3Id() {
    return this.creteria3Id;
  }

  public void setCreteria3Id(Long creteria3Id) {
    this.creteria3Id = creteria3Id;
  }

  public Long getCreteria4Id() {
    return this.creteria4Id;
  }

  public void setCreteria4Id(Long creteria4Id) {
    this.creteria4Id = creteria4Id;
  }

  public Long getCreteria5Id() {
    return this.creteria5Id;
  }

  public void setCreteria5Id(Long creteria5Id) {
    this.creteria5Id = creteria5Id;
  }

  public Long getCreteria6Id() {
    return this.creteria6Id;
  }

  public void setCreteria6Id(Long creteria6Id) {
    this.creteria6Id = creteria6Id;
  }

  public Long getCreteria7Id() {
    return this.creteria7Id;
  }

  public void setCreteria7Id(Long creteria7Id) {
    this.creteria7Id = creteria7Id;
  }

  public Long getCreteria8Id() {
    return this.creteria8Id;
  }

  public void setCreteria8Id(Long creteria8Id) {
    this.creteria8Id = creteria8Id;
  }

  public Long getCreteria9Id() {
    return this.creteria9Id;
  }

  public void setCreteria9Id(Long creteria9Id) {
    this.creteria9Id = creteria9Id;
  }

  public Long getCreteria10Id() {
    return this.creteria10Id;
  }

  public void setCreteria10Id(Long creteria10Id) {
    this.creteria10Id = creteria10Id;
  }

  public long getStatisticId() {
    return this.statisticId;
  }

  public void setStatisticId(long statisticId) {
    this.statisticId = statisticId;
  }

  public Date getCdate() {
    return this.cdate;
  }

  public void setCdate(Date cdate) {
    this.cdate = cdate;
  }

  public Long getCreteria11Id() {
    return this.creteria11Id;
  }

  public void setCreteria11Id(Long creteria11Id) {
    this.creteria11Id = creteria11Id;
  }

  public Long getCreteria12Id() {
    return this.creteria12Id;
  }

  public void setCreteria12Id(Long creteria12Id) {
    this.creteria12Id = creteria12Id;
  }

  public Long getCreteria13Id() {
    return this.creteria13Id;
  }

  public void setCreteria13Id(Long creteria13Id) {
    this.creteria13Id = creteria13Id;
  }

  public Long getCreteria14Id() {
    return this.creteria14Id;
  }

  public void setCreteria14Id(Long creteria14Id) {
    this.creteria14Id = creteria14Id;
  }

  public Long getCreteria15Id() {
    return this.creteria15Id;
  }

  public void setCreteria15Id(Long creteria15Id) {
    this.creteria15Id = creteria15Id;
  }

  public Integer getSquare() {
    return this.square;
  }

  public void setSquare(Integer square) {
    this.square = square;
  }

  public int getRatingSumm1() {
    return this.ratingSumm1;
  }

  public void setRatingSumm1(int ratingSumm1) {
    this.ratingSumm1 = ratingSumm1;
  }

  public int getRatingSumm2() {
    return this.ratingSumm2;
  }

  public void setRatingSumm2(int ratingSumm2) {
    this.ratingSumm2 = ratingSumm2;
  }

  public int getRatingSumm3() {
    return this.ratingSumm3;
  }

  public void setRatingSumm3(int ratingSumm3) {
    this.ratingSumm3 = ratingSumm3;
  }

  public int getCountpostRating1() {
    return this.countpostRating1;
  }

  public void setCountpostRating1(int countpostRating1) {
    this.countpostRating1 = countpostRating1;
  }

  public int getCountpostRating2() {
    return this.countpostRating2;
  }

  public void setCountpostRating2(int countpostRating2) {
    this.countpostRating2 = countpostRating2;
  }

  public int getCountpostRating3() {
    return this.countpostRating3;
  }

  public void setCountpostRating3(int countpostRating3) {
    this.countpostRating3 = countpostRating3;
  }

  public int getMidleBal1() {
    return this.midleBal1;
  }

  public void setMidleBal1(int midleBal1) {
    this.midleBal1 = midleBal1;
  }

  public int getMidleBal2() {
    return this.midleBal2;
  }

  public void setMidleBal2(int midleBal2) {
    this.midleBal2 = midleBal2;
  }

  public int getMidleBal3() {
    return this.midleBal3;
  }

  public void setMidleBal3(int midleBal3) {
    this.midleBal3 = midleBal3;
  }

  public boolean isShowRating1() {
    return this.showRating1;
  }

  public void setShowRating1(boolean showRating1) {
    this.showRating1 = showRating1;
  }

  public boolean isShowRating2() {
    return this.showRating2;
  }

  public void setShowRating2(boolean showRating2) {
    this.showRating2 = showRating2;
  }

  public boolean isShowRating3() {
    return this.showRating3;
  }

  public void setShowRating3(boolean showRating3) {
    this.showRating3 = showRating3;
  }

  public boolean isShowBlog() {
    return this.showBlog;
  }

  public void setShowBlog(boolean showBlog) {
    this.showBlog = showBlog;
  }

  public String getName2() {
    return this.name2;
  }

  public void setName2(String name2) {
    this.name2 = name2;
  }

  public String getSearch2() {
    return this.search2;
  }

  public void setSearch2(String search2) {
    this.search2 = search2;
  }

  public double getAmount1() {
    return this.amount1;
  }

  public void setAmount1(double amount1) {
    this.amount1 = amount1;
  }

  public double getAmount2() {
    return this.amount2;
  }

  public void setAmount2(double amount2) {
    this.amount2 = amount2;
  }

  public double getAmount3() {
    return this.amount3;
  }

  public void setAmount3(double amount3) {
    this.amount3 = amount3;
  }

  public String getJspUrl() {
    return this.jspUrl;
  }

  public void setJspUrl(String jspUrl) {
    this.jspUrl = jspUrl;
  }

  public String getColor() {
    return this.color;
  }

  public void setColor(String color) {
    this.color = color;
  }

}
