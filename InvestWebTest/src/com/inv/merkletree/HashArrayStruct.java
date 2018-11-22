
/**
 * 完整性验证需要用到的结构体
 * 
 */

package com.inv.merkletree;

import java.util.ArrayList;

public class HashArrayStruct {
	
	private long date; //交易日期
	private ArrayList<VerifStruct> hashList;  //关键节点哈希值数组
	
	public HashArrayStruct() {
		this.date = 0;
		this.hashList = new ArrayList<VerifStruct>();
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public ArrayList<VerifStruct> getHashList() {
		return hashList;
	}
	public void setHashList(ArrayList<VerifStruct> hashList) {
		this.hashList = hashList;
	}

}
