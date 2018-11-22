
/**
 * a第二层树信息表实例
 * b存储第二层树信息
 */

package com.inv.merkletree;


public class HashValue {
	
	private long date; //日期
	private int num; //编号(从根节点开始编号)
	private String hashvalue; //hash值
	
	public HashValue() {
		this.date = 0;
		this.num = 0;
		this.hashvalue = "";
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getHashvalue() {
		return hashvalue;
	}
	public void setHashvalue(String hashvalue) {
		this.hashvalue = hashvalue;
	}
}
