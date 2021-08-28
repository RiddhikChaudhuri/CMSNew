package com.cbsinc.cms.publisher.dao.RowMappers;

public class XMLDbCriteriaListRowMapper {

	private String criteriaId;
	private String name;

	public String getCriteriaId() {
		return criteriaId;
	}

	public void setCriteriaId(String criteriaId) {
		this.criteriaId = criteriaId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "XMLDbCriteriaListRowMapper [criteriaId=" + criteriaId + ", name=" + name + "]";
	}

}
