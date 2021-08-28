package com.cbsinc.cms.dto.pages.request;

public class SupportRequest {

	private String email;
	private String problem;
	private String person;
	private String message;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "SupportRequest [email=" + email + ", problem=" + problem + ", person=" + person + ", message=" + message
				+ "]";
	}

}
