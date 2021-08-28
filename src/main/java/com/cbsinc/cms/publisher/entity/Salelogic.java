package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="salelogic")
public class Salelogic implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="SALELOGIC_ID")
	private long salelogicId;
    
    @Column(name="NAME",length=50)
	private String name;
    
    @Column(name="Describtion",length=500)
	private String describe;
    
    @Column(name="ACTIVE",nullable=false)
	private boolean active;

	public Salelogic() {
	}

	public Salelogic(long salelogicId, boolean active) {
		this.salelogicId = salelogicId;
		this.active = active;
	}

	public Salelogic(long salelogicId, String name, String describe,
			boolean active) {
		this.salelogicId = salelogicId;
		this.name = name;
		this.describe = describe;
		this.active = active;
	}

	public long getSalelogicId() {
		return this.salelogicId;
	}

	public void setSalelogicId(long salelogicId) {
		this.salelogicId = salelogicId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescribe() {
		return this.describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
