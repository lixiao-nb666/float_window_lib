package com.newbee.floatwindowlibrary.view.circlemenu.view.bean;

public class CircleItemInfo {

	private int imgId;
	private String text;
	private Object object;

	public CircleItemInfo() {
		super();
	}

	public int getImgId() {
		return imgId;
	}
	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return "CircleItemInfo{" +
				"imgId=" + imgId +
				", text='" + text + '\'' +
				", object=" + object +
				'}';
	}


}
