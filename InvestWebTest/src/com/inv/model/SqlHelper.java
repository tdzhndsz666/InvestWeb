/**
 * db2数据库操作类
 * a功能：连接数据库、执行dml/dql语句、关闭数据库连接
 * @author w
 *
 */

package com.inv.model;
import java.sql.*;

public class SqlHelper {
	
	Connection conn ;
	private String url ;
	private String user ;
	private String password ;

	ResultSet results;//结果集
	
	public SqlHelper() {
		//初始化
		this.url = "jdbc:db2://localhost:50000/test";
		this.user = "db2admin";
		this.password = "123456";
		connect_db2();
	}

	public void setUrl(String dbname) {
		this.url = "jdbc:db2://localhost:50000/"+dbname;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 连接数据库
	 */
	public void connect_db2() {
		try {
			
			Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();//加载数据库驱动db2jcc4.jar			
			//连接db2数据库
			conn = DriverManager.getConnection(this.url, this.user, this.password);
			//System.out.println("success");
			}catch (Exception sqle) {
				System.out.println(sqle);
			}
	}
	
	/**
	 * a执行查询语句
	 * @param sql
	 * @return 查询结果集
	 * @throws SQLException 
	 */
	public ResultSet execute_dql(String sql) throws SQLException {
		
		PreparedStatement ps = this.conn.prepareStatement(sql);	
		this.results = ps.executeQuery();		
		return this.results;
		
	}
	
	/**
	 * a增删改操作
	 * @param sql
	 */
	public int execute_dml(String sql) {
		
		int result = 0;
		try {
			
			PreparedStatement ps = this.conn.prepareStatement(sql);
			result = ps.executeUpdate();
			
		}catch(SQLException e) {			
	        System.out.println(e);  
	    }
		return result;
	}
	
	/**
	 * a关闭数据库连接
	 * @throws SQLException
	 */
	public void close_db2() {
		
		try{  
			//释放资源
            if(this.results!=null) {
            	this.results.close();
            }
            //断开数据库连接
            if(this.conn!=null) {
            	this.conn.close();
            }
      }catch(SQLException e){
    	  System.out.println(e);  
      }  
	}
}
