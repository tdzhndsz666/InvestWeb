
/**
 * 描述：该类提供了计算MerkleTree的方法
 * 
 */

package com.inv.merkletree;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;


public class MerkleTree {

	private Queue<String> firstLeafCodeQueue = null;
	private Queue<HashStruct> hashStructFir = null;
	private Queue<HashStruct> hashStructSec = null;
	private Queue<HashValue> hashValue = null;
	
	/**
	 * 数据初始化
	 */
	public MerkleTree() {
		
		this.firstLeafCodeQueue = new LinkedList<String>();	
		this.hashStructFir = new LinkedList<HashStruct>();
		this.hashStructSec = new LinkedList<HashStruct>();
		this.hashValue = new LinkedList<HashValue>();
		
	}
	
	/**
	 * 生成MerkleTree
	 * @param client
	 * @param record
	 * @return Queue<HashValue>
	 * @throws SQLException
	 */
	public Queue<HashValue> createMerkleTree(Queue<Client> client,Queue<Record> record) throws SQLException {
		
		int layer = new Tool().getLayer(client.size()); //计算MerkleTree层数		
		createAllClientMtRootHashValue(record,client);//生成所有用户交易记录的MerkleTree根节点		
		createSencondMtLeafCode(client,layer); //生成第二层MerkleTree的叶子节点
		return createSecondMt(layer); //返回第二层MerkleTree所有节点
		
	}
		
	/**
	 * @describe 生成第二层Merkle树
	 * @param ResultSet类型对象
	 * @return 返回第二层Merkle树节点队列
	 * @throws SQLException 
	 */
	public Queue<HashValue> createSecondMt(int layer) throws SQLException {
		
		int num = 0;
		
		String left = "";//左叶子节点
		String right = ""; //右叶子结点
		int size;//队列大小

		HashValue hValue = null;
		HashValue val = null;
		HashStruct struct = null;
		
		//将叶子节点存入队列
		for(HashStruct str : this.hashStructSec) {
			hValue = new HashValue();
			hValue.setDate(str.getDate());
			hValue.setHashvalue(str.getHashvalue());
			hValue.setNum(str.getNum());
			this.hashValue.offer(hValue);
		}
		
		
		for(int i = 0; i < layer; ++i) {
			size = this.hashStructSec.size(); //记录当前层数节点数
			while(size != 0) {
				struct = new HashStruct();
				val = new HashValue();
				val.setDate(this.hashStructSec.peek().getDate());
				struct.setDate(this.hashStructSec.peek().getDate());
				left = this.hashStructSec.peek().getHashvalue();	
				num = this.hashStructSec.peek().getNum()/2;
				val.setNum(num);
				struct.setNum(num);
				this.hashStructSec.remove();
				size--;
				right = this.hashStructSec.peek().getHashvalue();
			    this.hashStructSec.remove();	
			    size--;
			    val.setHashvalue(createHashCode(left,right));
			    struct.setHashvalue(createHashCode(left,right));
			    
			    struct.setClient_no(0);
			    this.hashStructSec.offer(struct);
			    this.hashValue.offer(val);
			}
					
		}

		return this.hashValue;
	}
	
	/**
	 * 计算某个用户交易记录根节点哈希值
	 * @param record
	 * @return
	 */
	public String createOneClientMtRootHashValue(Queue<Record> record){
		
		//计算叶子结点哈希值
		for(Record r : record) {
			createFirstMtLeafCode(r);
		}
		return getFirstMtRootHashValue();//返回根节点哈希值
		
	}
	/**
	 * @describe 生成所用用户当天交易hashcode
	 * @param record
	 */
	public void createAllClientMtRootHashValue(Queue<Record> record, Queue<Client> client) {
		
		long date = 0;
		boolean isNull = true; //某个客户某日交易记录是否为空
		HashStruct value = null;
		for(Client qClient : client) {			
			for(Record qRecord : record) {
				date = qRecord.getCfm_date();
				if(qClient.getClient_no() == qRecord.getClient_no()) {
					createFirstMtLeafCode(qRecord);
					isNull = false;
				}
			}
			
			//生成第二层树的叶节点
			if(isNull) {  //如果没有记录补0
				value = new HashStruct();
				value.setHashvalue("0000000000000000000000000000000000000000000000000000000000000000");

			}else {
				value = new HashStruct();
				value.setHashvalue(getFirstMtRootHashValue());
			}

			value.setClient_no(qClient.getClient_no());
			value.setDate(date);
			this.hashStructFir.offer(value);
			
			isNull = true;
		}

	}
	
	/**
	 * @describe 生成第二层树叶节点，并存入secondLeafCodeQueue
	 * @param result
	 * @throws SQLException 
	 */
	public void createSencondMtLeafCode(Queue<Client> client,int layer) throws SQLException {
		
		int count = (int) Math.pow(2,layer);//第二层树最左边的叶子节点编号为2的n次方
		//System.out.println(count);
		long date = 0;
		boolean isFind = false;
		//long client_no;
		HashStruct valStr = null;
		
		//将叶子结点按照客户号排序
		for(Client qClient : client){			
			for(HashStruct qHashStruct:this.hashStructFir) {			
				//如果该用户有记录则添加到hashStructSec中并跳出当前循环
				if(qHashStruct.getClient_no() == qClient.getClient_no()){
					
					valStr = new HashStruct();
					valStr.setClient_no(qClient.getClient_no());
					valStr.setHashvalue(qHashStruct.getHashvalue());
					valStr.setNum(count);
					valStr.setDate(qHashStruct.getDate());
					this.hashStructSec.offer(valStr);
					
					count++;
					isFind = true;
					
					date = qHashStruct.getDate();
					continue;
					
				}
			}
			
			//如果该客户当天没有交易，则hashvalue = 0
			if(!isFind) {
				valStr = new HashStruct();
				valStr.setHashvalue("0000000000000000000000000000000000000000000000000000000000000000");
				valStr.setDate(date);
				valStr.setNum(count);
				valStr.setClient_no(qClient.getClient_no());
				this.hashStructSec.offer(valStr);
				count++;
				isFind = false;
			}
		}
		
		//添加补位的节点
		for(int i = count; i < (int) Math.pow(2,layer+1); ++i) {
			valStr = new HashStruct();
			valStr.setClient_no(0);
			valStr.setHashvalue("0000000000000000000000000000000000000000000000000000000000000000");
			valStr.setNum(i);
			valStr.setDate(date);
			this.hashStructSec.offer(valStr);
		}

	}
	
	/**
	 * @describe 计算数据块（交易记录）hash值，并存入firstLeafCodeQueue
	 * @param ResultSet类型对象
	 * @return void
	 */
	public void createFirstMtLeafCode(Record record) {
		
		//格式化日期时间
		//SimpleDateFormat formatter;
	    //String datestamp ; /*yyyy-MM-dd HH:mm:ss*/
		//formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		//格式化日期
		//datestamp = formatter.format(result.getDate("cfm_date"));
		//将交易数据存入HashCode对象
		//System.out.println(record.getSerial_no());	
		
		HashFunction hf = Hashing.sha256();			
		
	    HashCode hc = hf.newHasher()
			     .putLong(record.getSerial_no())
			     .putLong(record.getCfm_date())
			     .putLong(record.getClient_no())
			     .putLong(record.getBusin_code())
			     .putString(record.getPrd_code(), Charsets.UTF_8)
			     .putDouble(record.getCfm_date())
			     .hash();
		//将得到的hashcode存入队列
	    //this.firstLeafCodeQueue.offer(hc.hashCode());	
	    this.firstLeafCodeQueue.offer(hc.toString());

	}
	
	/**
	 * @describe 为每个用户一天的交易记录生成MerkleTree
	 * 
	 * @param record
	 * @return  返回MerkleTree的根节点
	 */
	public String getFirstMtRootHashValue() {
		
		  int index = 0;//计数器
		  int size;//队列大小

		  String left;
		  String right;
		  
		  String hashCodeValue;
		  
		  size = this.firstLeafCodeQueue.size();
		  
		  /*
		   * 计算出每个数据的哈希值，从左到右逐步组成树的左右节点 
		   * 执行循环直到最后一个节点 
		   */
		  if(this.firstLeafCodeQueue.size() == 1) {
			  left = this.firstLeafCodeQueue.peek();
			  right = left;
			  hashCodeValue = getHashCodeValueCopy(left, right);
			  return hashCodeValue;
		  }
		  while (this.firstLeafCodeQueue.size()>1) {
			  
		    // left
		    left = this.firstLeafCodeQueue.peek();
		    this.firstLeafCodeQueue.remove();
		    index++;
		    // right
		    right = "";
		    
		    //如果index == size
		    //说明这一层已经没有节点数据了
		    if(index != size) {
		    	right = this.firstLeafCodeQueue.peek();
		    	this.firstLeafCodeQueue.remove();
		    	index++;
		    }else {
		    	size = this.firstLeafCodeQueue.size()+1;
		    	index = 0;
		    }
		    //计算hash节点
		    hashCodeValue = getHashCodeValueCopy(left, right);
		    //将计算的hashcode存入队列
		    this.firstLeafCodeQueue.offer(hashCodeValue);
		    //System.out.println(index);
		  }
		  return this.firstLeafCodeQueue.peek();//返回根节点
	}
	
	/**
	 * @describe 计算MekleTree节点（复制自身）
	 * @param left
	 * @param right
	 * @return
	 */
	public String getHashCodeValueCopy(String left, String right) {
		
		HashFunction hf = Hashing.sha256();
		HashCode hc;
		//如果出现单个孩子节点,则复制自身左孩子值进行计算
		if(right == "") {		
			hc = hf.newHasher()
			       .putString(left, Charsets.UTF_8)
			       .putString(left,Charsets.UTF_8)
			       .hash();
		}else {
			hc = hf.newHasher()
				.putString(left, Charsets.UTF_8)
				.putString(right,Charsets.UTF_8)
			    .hash();
		}	
		return hc.toString();
	}
	
	/**
	 * @describe 计算MekleTree节点
	 * @param left
	 * @param right
	 * @return
	 */
	public String createHashCode(String left,String right) {
		
		HashFunction hf = Hashing.sha256();
		HashCode hc;

		hc = hf.newHasher()
			   .putString(left, Charsets.UTF_8)
			   .putString(right,Charsets.UTF_8)
		       .hash();
	
		return hc.toString();
	}
}
