/**
 * record表类
 * a对record表数据进行封装

 */

package com.inv.merkletree;
import java.math.*;
public class Record {
	
	private long serial_no; 					//交易流水号
	private long cfm_date;				//确认日期
	private long client_no;					//客户编号
	private long busin_code;					//业务代码
	private String prd_code;					//产品代码
	private BigDecimal cfm_amt;					//确认金额
	
	public long getSerial_no() {
		return this.serial_no;
	}
	
	public void setSerial_no(long l) {
		this.serial_no = l;
	}
	public long getCfm_date() {
		return this.cfm_date;
	}
	public void setCfm_date(long cfm_date) {
		this.cfm_date = cfm_date;
	}
	public long getClient_no() {
		return this.client_no;
	}
	public void setClient_no(long client_no) {
		this. client_no =  client_no;
	}
	public long getBusin_code() {
		return this.busin_code;
	}
	public void setBusin_code(long busin_code) {
		this.busin_code = busin_code;
	}
	public String getPrd_code() {
		return this.prd_code;
	}
	public void setPrd_code(String prd_code) {
		this.prd_code = prd_code;
	}
	public BigDecimal cfm_amt() {
		return this.cfm_amt;
	}
	public void setCfm_amt(BigDecimal cfm_amt) {
		this.cfm_amt = cfm_amt;
	}
	
}
