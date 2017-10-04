package com.mcbodik.liketinder.loader.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageSetModel {
	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("primary")
	@Expose
	private String primary;
	@SerializedName("owner")
	@Expose
	private String owner;
	@SerializedName("ownername")
	@Expose
	private String ownername;
	@SerializedName("photo")
	@Expose
	private List<ImageModel> photo = null;
	@SerializedName("page")
	@Expose
	private Integer page;
	@SerializedName("per_page")
	@Expose
	private String perPage;
	@SerializedName("perpage")
	@Expose
	private String perpage;
	@SerializedName("pages")
	@Expose
	private Integer pages;
	@SerializedName("total")
	@Expose
	private Integer total;
	@SerializedName("title")
	@Expose
	private Integer title;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrimary() {
		return primary;
	}

	public void setPrimary(String primary) {
		this.primary = primary;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnername() {
		return ownername;
	}

	public void setOwnername(String ownername) {
		this.ownername = ownername;
	}

	public List<ImageModel> getPhoto() {
		return photo;
	}

	public void setPhoto(List<ImageModel> photo) {
		this.photo = photo;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getPerPage() {
		return perPage;
	}

	public void setPerPage(String perPage) {
		this.perPage = perPage;
	}

	public String getPerpage() {
		return perpage;
	}

	public void setPerpage(String perpage) {
		this.perpage = perpage;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getTitle() {
		return title;
	}

	public void setTitle(Integer title) {
		this.title = title;
	}
}
