package com.mcbodik.liketinder.loader.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageModel {

	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("secret")
	@Expose
	private String secret;
	@SerializedName("server")
	@Expose
	private String server;
	@SerializedName("farm")
	@Expose
	private Integer farm;
	@SerializedName("title")
	@Expose
	private String title;
	@SerializedName("isprimary")
	@Expose
	private Integer isprimary;
	@SerializedName("ispublic")
	@Expose
	private Integer ispublic;
	@SerializedName("isfriend")
	@Expose
	private Integer isfriend;
	@SerializedName("isfamily")
	@Expose
	private Integer isfamily;
	@SerializedName("dateupload")
	@Expose
	private long dateupload;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public Integer getFarm() {
		return farm;
	}

	public void setFarm(Integer farm) {
		this.farm = farm;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getIsprimary() {
		return isprimary;
	}

	public void setIsprimary(Integer isprimary) {
		this.isprimary = isprimary;
	}

	public Integer getIspublic() {
		return ispublic;
	}

	public void setIspublic(Integer ispublic) {
		this.ispublic = ispublic;
	}

	public Integer getIsfriend() {
		return isfriend;
	}

	public void setIsfriend(Integer isfriend) {
		this.isfriend = isfriend;
	}

	public Integer getIsfamily() {
		return isfamily;
	}

	public void setIsfamily(Integer isfamily) {
		this.isfamily = isfamily;
	}

	public Long getDateupload() {
		return dateupload;
	}

	public void setDateupload(Long dateupload) {
		this.dateupload = dateupload;
	}
}
