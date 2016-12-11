package com.spider.scut.dao;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Event entity. @author wangjiayong
 */
@Entity
@Table(name = "event0", catalog = "spider")
public class Event implements java.io.Serializable {

	// Fields

	private String id;
	private String publishDate;
	private String teamName;
    private String targetCountry;
    private String task;
	private String info;
    private String travel;
    private String fee;
    
	// Constructors

	/** default constructor */
	public Event() {
	}

	/** full constructor */
	public Event(String id, String teamName, String targetCountry, String task,
			String info, String travel, String fee) {
		this.id = id;
		this.teamName = teamName;
		this.targetCountry = targetCountry;
		this.task = task;
		this.info = info;
		this.travel = travel;
		this.fee = fee;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "publishDate", length = 50)
	public String getPublishDate() {
		return this.publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	
	@Column(name = "teamName", length = 100)
	public String getTeamName() {
		return this.teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	@Column(name = "targetCountry", length = 100)
	public String getTargetCountry() {
		return this.targetCountry;
	}

	public void setTargetCountry(String targetCountry) {
		this.targetCountry = targetCountry;
	}

	@Column(name = "task", length = 300)
	public String getTask() {
		return this.task;
	}

	public void setTask(String task) {
		this.task = task;
	}
	
	@Column(name = "info", length = 1500)
	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Column(name = "travel", length = 1500)
	public String getTravel() {
		return this.travel;
	}

	public void setTravel(String travel) {
		this.travel = travel;
	}

	@Column(name = "fee", length = 300)
	public String getFee() {
		return this.fee;
	}

	public void setFee(String fee) {
		this.task = fee;
	}

}