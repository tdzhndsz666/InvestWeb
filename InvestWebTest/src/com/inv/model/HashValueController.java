
/**
 * 封装了hashvalue表的操作
 */
package com.inv.model;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.Queue;

import com.inv.merkletree.HashValue;


public class HashValueController {
	private SqlHelper db2Oprate = null;
	
	public HashValueController() {
		
		this.db2Oprate = new SqlHelper();
	}

	/**
	 * 将数据保存到hashvalue表中
	 * @param hashValue
	 * @return  插入结果
	 */
	public int saveHashValue(Queue<HashValue> hashValue) {
		
		int result = 0;
		if(hashValue.size() != 0) {
			
			//生成一条插入语句，不建议每条数据生成一条插入语句
			String insert = "INSERT INTO hashvalue values";
			String sql = "";
			int index = 1;
			if(hashValue.size() == 1) {
				sql = "("+hashValue.peek().getDate()+","
						+hashValue.peek().getNum()+","
						+"'"+hashValue.peek().getHashvalue()+"')";
				insert = insert+sql;
			}else {
				for(HashValue value : hashValue) {
					if(index == 1) {
						sql = "("+value.getDate()+","
								+value.getNum()+","
								+"'"+value.getHashvalue()+"')";
						insert = insert+sql;
						index++;
					}else {
						sql = ",("+value.getDate()+","
								+value.getNum()+","
								+"'"+value.getHashvalue()+"')";
						insert = insert+sql;
					}				
				}
			}
			try {
				
				result = this.db2Oprate.execute_dml(insert);
				
			}catch(Exception e) {
				
				System.out.println(e);
				
			}
			this.db2Oprate.close_db2();
		}
		return result;
	}
	
	/**
	 * @描述 获取某天MerkleTree根节点哈希值
	 * @param cfm_date
	 * @return String
	 */
	public String getMtRootHashValue(long cfm_date){
		
		ResultSet result = null;
		String hashValue = "";
		String sql = "SELECT hashvalue FROM hashvalue WHERE date="+cfm_date+" AND num=1";//根据客户编号进行从低到高的排序
		try {
			result = this.db2Oprate.execute_dql(sql);
			while(result.next()) {
				hashValue = result.getString("hashvalue");
			}
			
		}catch(Exception e) {
			System.out.println(e);
		}
		this.db2Oprate.close_db2();
		return hashValue;
	}
	
	/**
	 * 获取某日的MerkleTree所有节点数据
	 * @param cfm_date:交易日期
	 * @return Queue<HashValue>
	 */
	public Queue<HashValue> getHashValue(long cfm_date){
		
		Queue<HashValue> qHashValue = new LinkedList<HashValue>();
		HashValue hashValue = null;
		ResultSet result = null;		
		String sql = "SELECT *FROM hashvalue WHERE date="+cfm_date;//根据客户编号进行从低到高的排序
		try {
			result = this.db2Oprate.execute_dql(sql);
			while(result.next()) {
				
				hashValue = new HashValue();
				hashValue.setDate(result.getLong("date"));
				hashValue.setHashvalue(result.getString("hashvalue"));
				hashValue.setNum(result.getInt("num"));			
				qHashValue.offer(hashValue);	
				
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		this.db2Oprate.close_db2();
		return qHashValue;
	}
}
