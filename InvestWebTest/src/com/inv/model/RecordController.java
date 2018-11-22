package com.inv.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import com.inv.merkletree.Record;

//import entity.HashValue;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class RecordController {
	
	private Queue<Record> qRecord ;
	SqlHelper db2Oprate = null;
	
	public RecordController(){
		
		this.db2Oprate = new SqlHelper();
	}
	
	/**
	 * 获取某日所有交易记录
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public Queue<Record> getRecord(long date) throws SQLException {
		
		Record record = null;
		this.qRecord = new LinkedList<Record>();
		ResultSet result = null;		
		String sql = "SELECT *FROM record WHERE cfm_date="+date+" ORDER BY client_no AND serial_no ASC";//根据客户编号进行从低到高的排序
		try {
			result = this.db2Oprate.execute_dql(sql);
			while(result.next()) {
				record = new Record();
				//long lDate = (long) Math.floor(result.getLong("serial_no")/10000);
				record.setSerial_no(result.getLong("serial_no"));
				record.setCfm_date(result.getLong("cfm_date"));
				record.setClient_no(result.getLong("client_no"));
				record.setBusin_code(result.getLong("busin_code"));
				record.setPrd_code(result.getString("prd_code"));
				record.setCfm_amt(result.getBigDecimal("cfm_amt"));
				this.qRecord.offer(record);						
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		this.db2Oprate.close_db2();
		return this.qRecord;
		
	}
	
	/**
	 * 得到真实性验证明文数据(JSON格式)
	 * @param date
	 * @param client_no
	 * @return
	 * @throws SQLException
	 */
	public String getJSONRecord(long date,long client_no) throws SQLException {
		
		JSONArray jsonarray = new JSONArray();  
		JSONObject jsonobj =null; 
		ResultSet result = null;		
		String sql = "SELECT *FROM record WHERE cfm_date="+date+" AND client_no="+client_no+" ORDER BY client_no AND serial_no ASC";//根据客户编号进行从低到高的排序
		try {
			
			result = this.db2Oprate.execute_dql(sql);
			while(result.next()) {
				
				jsonobj = new JSONObject(); 
				jsonobj.put("serial_no", result.getLong("serial_no"));
				jsonobj.put("cfm_date", result.getLong("cfm_date"));
				jsonobj.put("client_no", result.getLong("client_no"));
				jsonobj.put("busin_code", result.getLong("busin_code"));
				jsonobj.put("prd_code", result.getString("prd_code"));
				jsonobj.put("cfm_amt", result.getBigDecimal("cfm_amt"));
				jsonarray.add(jsonobj);
				
			}
			
		}catch(Exception e) {
			
			System.out.println(e);
			
		}
		
		this.db2Oprate.close_db2();
		return jsonarray.toString();
		
	}
}
