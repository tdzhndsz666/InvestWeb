
/**
 * a结构体
 * b第二层树需要存储的信息
 */

package com.inv.merkletree;

public class HashStruct {
	private long date; //日期
	private int num; //编号(从根节点开始编号)
	private String hashvalue; //hash值
	private long client_no; //客户号
	
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
	public long getClient_no() {
		return client_no;
	}
	public void setClient_no(long client_no) {
		this.client_no = client_no;
	}
}
