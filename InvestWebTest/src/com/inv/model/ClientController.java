package com.inv.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import com.inv.merkletree.Client;

//import entity.Record;

public class ClientController {
	
	private SqlHelper db2Oprate = null;
	
	public ClientController() {
		
		this.db2Oprate = new SqlHelper();
	}
	
	/*
	 * 插入客户号
	 */
	public void insertClient() {
		
		for(int i = 1; i <10000 ;++i) {
			String sql = "insert into client(client_no) values("+i+")";//根据客户编号进行从低到高的排序
			db2Oprate.execute_dml(sql);
		}
		db2Oprate.close_db2();
	}
	
	/**
	 * 获取用户数据集，并按照客户号从低到高进行排序
	 * @return Queue<Client>
	 */
	public Queue<Client> getClientQueue() {
				
		Client client = null;
		Queue<Client> qClient = new LinkedList<Client>();
		ResultSet result = null;
		String sql = "SELECT *FROM client ORDER BY client_no ASC";//根据客户编号进行从低到高的排序
		try {			
			result = db2Oprate.execute_dql(sql);
			while(result.next()) {
				client = new Client();
				client.setClient_no(result.getLong("client_no"));
				qClient.offer(client);
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		db2Oprate.close_db2();
		
		return qClient;
		
	}
	
	/**
	 * 获取用户数据集，并按照客户号从低到高进行排序
	 * @return  ResultSet
	 */
	public ResultSet getClient() {
		
		ResultSet result = null;
		String sql = "SELECT *FROM client ORDER BY client_no ASC";//根据客户编号进行从低到高的排序
		try {
			
			result = db2Oprate.execute_dql(sql);			
			
		}catch(Exception e) {
			
			System.out.println(e);
			
		}		
		
		return result;
		
	}
	
	/**
	 * 根据客户号获取客户序列号
	 * @param client_no
	 * @return
	 * @throws SQLException
	 */
	public long getClientOrdinal(long client_no) throws SQLException {
		
		ResultSet result = null;
		long ordinal = 0;
		String sql = "SELECT client_no,ROW_NUMBER() OVER (ORDER BY client_no ASC) FROM client WHERE client_no<="+client_no;//根据客户编号进行从低到高的排序		

		result = db2Oprate.execute_dql(sql);
		
		while(result.next()) {
			if(result.getLong(1) == client_no) {
				ordinal = result.getLong(2);
			}			
		}
		db2Oprate.close_db2();
		
		return ordinal;
		
	}
}
