package com.spider.scau.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Content2 entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "content2", catalog = "spider")
public class Content2 implements java.io.Serializable {

	// Fields

	private Integer id;
	private String leader;
	private String department;
	private String job;
	private String position;
	private String waste;
	private String target;
	private String process;
	private String path;
	private String fromm;
	private String addition;
	private String additionContent;
	private String url;

	// Constructors

	/** default constructor */
	public Content2() {
	}

	/** full constructor */
	public Content2(String leader, String department, String job,
			String position, String waste, String target, String process,
			String path, String fromm, String addition, String additionContent,
			String url) {
		this.leader = leader;
		this.department = department;
		this.job = job;
		this.position = position;
		this.waste = waste;
		this.target = target;
		this.process = process;
		this.path = path;
		this.fromm = fromm;
		this.addition = addition;
		this.additionContent = additionContent;
		this.url = url;
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

	@Column(name = "leader", length = 50)
	public String getLeader() {
		return this.leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	@Column(name = "department", length = 100)
	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Column(name = "job", length = 50)
	public String getJob() {
		return this.job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	@Column(name = "position", length = 1000)
	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Column(name = "waste", length = 1000)
	public String getWaste() {
		return this.waste;
	}

	public void setWaste(String waste) {
		this.waste = waste;
	}

	@Column(name = "target", length = 1000)
	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Column(name = "process", length = 10000)
	public String getProcess() {
		return this.process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	@Column(name = "path", length = 1000)
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(name = "fromm", length = 3000)
	public String getFromm() {
		return this.fromm;
	}

	public void setFromm(String fromm) {
		this.fromm = fromm;
	}

	@Column(name = "addition", length = 1000)
	public String getAddition() {
		return this.addition;
	}

	public void setAddition(String addition) {
		this.addition = addition;
	}

	@Column(name = "additionContent")
	public String getAdditionContent() {
		return this.additionContent;
	}

	public void setAdditionContent(String additionContent) {
		this.additionContent = additionContent;
	}

	@Column(name = "url", length = 100)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}