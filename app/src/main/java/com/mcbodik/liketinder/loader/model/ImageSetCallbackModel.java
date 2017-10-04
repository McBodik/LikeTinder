package com.mcbodik.liketinder.loader.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageSetCallbackModel {
	@SerializedName("photoset")
	@Expose
	private ImageSetModel photoset;
	@SerializedName("stat")
	@Expose
	private String stat;

	public ImageSetModel getPhotoset() {
		return photoset;
	}

	public void setPhotoset(ImageSetModel photoset) {
		this.photoset = photoset;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

}
