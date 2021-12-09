package com.example.restservice;

public class Body {

	private long pageNo;
	private Item item;

	public Body() {
	}

	public Body(long pageNo, Item item) {
		this.pageNo = pageNo;
	}

	public long getPageNo() {
		return pageNo;
	}

	public Item getItem() {
		return item;
	}
}
