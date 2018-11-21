
/**
 * @describe 数据验证类
 */
package com.inv.verificat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import com.inv.tool.Tool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.inv.model.ClientController;
import com.inv.model.HashValueController;
import com.inv.entity.HashArrayStruct;
import com.inv.entity.HashValue;
import com.inv.entity.VerifStruct;


public class Verification {
	
	private Queue<HashValue> qHashValue = null;
	private ArrayList<VerifStruct> truthHashList = null; //存放发送给用户验证的哈希值
	private ArrayList<HashArrayStruct> fullHashList = null; //存放发送给用户验证的哈希值
	public Verification() {
		this.truthHashList = new ArrayList<VerifStruct>();
		this.fullHashList = new ArrayList<HashArrayStruct>();
		this.qHashValue = new LinkedList<HashValue>();
	}
	
	/**
	 * @describe 真实性验证(此处循环次数过多，可以考虑优化)
	 * @return  ArrayList
	 * @throws SQLException 
	 */
	public ArrayList<VerifStruct> getTruthVerifValue(long cfm_date,long client_no) throws SQLException {

		ClientController client = new ClientController();
		HashValueController hashValue = new HashValueController();
	
		this.qHashValue = hashValue.getHashValue(cfm_date); //获取某日交易MerkleTree的所有节点
		long count = qHashValue.size(); //当天交易MerkleTree的所有节点数
		
		if(count <= 0) {
			return null;
		}
		
		int layer = new Tool().getLayer(count)-1;  //MerkleTree层数（注：此处-1是为计算方便）
		long ordinal = client.getClientOrdinal(client_no); //获取该客户序号
		
		long num = ordinal+(long)Math.pow(2, layer)-1;  //该用户交易记录在MerkleTree中的编号
		
		if((num&1) != 1){//是偶数就取右叶子节点  
						
			addHashValue(num+1);//将该节点记录下来
			
			while(layer>1) {//逐层循环遍历
				layer--;
				num = num/2;				
				//如果编号大于该层第一个节点编号就说明左边还可以继续取值
				if(num > Math.pow(2, layer)) {					   
					if((num&1) != 1){//是偶数就取右叶子节点 
						addHashValue(num+1);
					}else {
						addHashValue(num-1);
					}					
				}else {
					addHashValue(num+1);
				}			
			}
		}else {//是奇数就取左叶子节点
			num--;
			addHashValue(num);
			while(layer>1) {
				layer--;
				num = num/2;
				if(num > Math.pow(2, layer)) {
					if((num&1) != 1){
						addHashValue(num+1);
					}else {
						addHashValue(num-1);
					}				
				}else {
					addHashValue(num+1);
				}	
			}
		}		
		return this.truthHashList;
	}
	
	/**
	 * 得到真实性验证关键节点信息(json格式字符串)
	 * @param date
	 * @param client_no
	 * @return String
	 * @throws SQLException
	 */
	public String getTruthVerificHashCode(long date, long client_no) throws SQLException{
		
		JSONArray jsonarray = new JSONArray();  
		//JSONObject jsonobj = new JSONObject(); 	
		JSONObject jsonobj = null; 
		
		getTruthVerifValue(date, client_no);

		for(int i = 0;i < this.truthHashList.size(); ++i) {
			//System.out.println(list.get(i));
			jsonobj = new JSONObject();
	        jsonobj.put(this.truthHashList.get(i).getChild(), this.truthHashList.get(i).getHashvalue());
	        jsonarray.add(jsonobj);
		}

		return jsonarray.toString();
	}
	
	/**
	 * @describe 遍历数据找到节点哈希值，并将哈希值存入数组
	 * @param num
	 */
	public void addHashValue(long num) {
		VerifStruct verifval = null;
		for(HashValue val : this.qHashValue) {
			if(val.getNum() == num) {
				verifval = new VerifStruct();
				if((num&1) != 1){
					verifval.setChild("left");
				}else{
					verifval.setChild("right");
				}
							
				verifval.setHashvalue(val.getHashvalue());
				//this.truthHashList.add(val.getNum());
				this.truthHashList.add(verifval);
				continue;
			}
		}
	}
	
	/**
	 * @describe 完整性验证
	 * @return ArrayList
	 * @throws SQLException 
	 */
	public ArrayList<HashArrayStruct> integVerificat(ArrayList<Long> dateList,long client_no) throws SQLException {
		
		long date = 0;
		HashArrayStruct struct = null;
		//ArrayList hashList = new ArrayList();
		//ArrayList list = null;
		for(int index = 0; index < dateList.size(); ++index) {
			struct = new HashArrayStruct();
			date = (long) dateList.get(index);
			struct.setDate(date);
			struct.setHashList(getTruthVerifValue(date, client_no));
			this.fullHashList.add(struct);
		}
		return this.fullHashList;
	}
	
	/**
	 * 得到完整性验证数据（json格式字符串）
	 * @param dateList
	 * @param client_no
	 * @return String
	 * @throws SQLException
	 */
	public String integVerificatHashValue(ArrayList<Long> dateList,long client_no) throws SQLException {
		
		//HashArrayStruct struct = null;
		JSONArray jsonarray = new JSONArray();  
		JSONObject jsonobj = null; 
		integVerificat(dateList,client_no);
		
		for(int i = 0;i < this.fullHashList.size(); ++i) {
			//System.out.println(list.get(i));
			jsonobj = new JSONObject();
	        jsonobj.put(this.fullHashList.get(i).getDate(), this.fullHashList.get(i).getHashList());
	        jsonarray.add(jsonobj);
		}

		return jsonarray.toString();
	}
}
