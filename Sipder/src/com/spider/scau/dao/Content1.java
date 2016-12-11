package com.spider.scau.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Content1 entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "content1", catalog = "spider")
public class Content1 implements java.io.Serializable {

	// Fields

	private Integer id;
	private String department;
	private String leader;
	private String position;
	private String days;
	private String teamNumber;
	private String teamPeople;
	private String rule1;
	private String rule2;
	private String rule3;
	private String process;
	private String result;
	private String influence;
	private String suggestion;
	private String agreement;
	private String other;
	private String addition;
	private String url;
	private String additionContent;

	// Constructors

	/** default constructor */
	public Content1() {
	}

	/** full constructor */
	public Content1(String department, String leader, String position,
			String days, String teamNumber, String teamPeople, String rule1,
			String rule2, String rule3, String process, String result,
			String influence, String suggestion, String agreement,
			String other, String addition, String url, String additionContent) {
		this.department = department;
		this.leader = leader;
		this.position = position;
		this.days = days;
		this.teamNumber = teamNumber;
		this.teamPeople = teamPeople;
		this.rule1 = rule1;
		this.rule2 = rule2;
		this.rule3 = rule3;
		this.process = process;
		this.result = result;
		this.influence = influence;
		this.suggestion = suggestion;
		this.agreement = agreement;
		this.other = other;
		this.addition = addition;
		this.url = url;
		this.additionContent = additionContent;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "department", length = 50)
	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Column(name = "leader", length = 50)
	public String getLeader() {
		return this.leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	@Column(name = "position", length = 1500)
	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Column(name = "days", length = 50)
	public String getDays() {
		return this.days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	@Column(name = "teamNumber", length = 50)
	public String getTeamNumber() {
		return this.teamNumber;
	}

	public void setTeamNumber(String teamNumber) {
		this.teamNumber = teamNumber;
	}

	@Column(name = "teamPeople", length = 1500)
	public String getTeamPeople() {
		return this.teamPeople;
	}

	public void setTeamPeople(String teamPeople) {
		this.teamPeople = teamPeople;
	}

	@Column(name = "rule1", length = 1500)
	public String getRule1() {
		return this.rule1;
	}

	public void setRule1(String rule1) {
		this.rule1 = rule1;
	}

	@Column(name = "rule2", length = 1500)
	public String getRule2() {
		return this.rule2;
	}

	public void setRule2(String rule2) {
		this.rule2 = rule2;
	}

	@Column(name = "rule3", length = 1500)
	public String getRule3() {
		return this.rule3;
	}

	public void setRule3(String rule3) {
		this.rule3 = rule3;
	}

	@Column(name = "process", length = 3000)
	public String getProcess() {
		return this.process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	@Column(name = "result", length = 1500)
	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Column(name = "influence", length = 3000)
	public String getInfluence() {
		return this.influence;
	}

	public void setInfluence(String influence) {
		this.influence = influence;
	}

	@Column(name = "suggestion", length = 1500)
	public String getSuggestion() {
		return this.suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	@Column(name = "agreement", length = 1500)
	public String getAgreement() {
		return this.agreement;
	}

	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}

	@Column(name = "other", length = 1500)
	public String getOther() {
		return this.other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	@Column(name = "addition", length = 1500)
	public String getAddition() {
		return this.addition;
	}

	public void setAddition(String addition) {
		this.addition = addition;
	}

	@Column(name = "url", length = 100)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "additionContent")
	public String getAdditionContent() {
		return this.additionContent;
	}

	public void setAdditionContent(String additionContent) {
		this.additionContent = additionContent;
	}

}