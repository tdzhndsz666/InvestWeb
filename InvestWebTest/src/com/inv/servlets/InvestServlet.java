package com.inv.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inv.merkletree.*;
import com.inv.model.*;

import com.inv.verificat.Verification;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class InvestServlet
 */
@WebServlet("/InvestServlet")
public class InvestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InvestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("text/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONArray jsonarray = new JSONArray();  
		//JSONObject jsonobj = new JSONObject(); 	
		JSONObject jsonobj = null; 
		
		/*
		 * 真实性验证
		 */
		long date = 20181103;  //交易日期
		long client_no = 10;  //客户号
		/*
		Queue<Record> qRecord = new LinkedList<Record>();//某日所有交易记录
		Queue<Client> qClient = new LinkedList<Client>();//所有客户的客户号
		HashValueController hashvalueContr = new HashValueController();
		//获取所有客户号
		ClientController clientContr = new ClientController();
		qClient = clientContr.getClientQueue();
		//获取某天所有交易记录

		RecordController recordContr = new RecordController();
		try {
			qRecord = recordContr.getRecord(date);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		//生成MerkleTree，并将第二层树存入hashValue队列中
		Queue<HashValue> hashValue = null;
		hashValue = new LinkedList<HashValue>();
		MerkleTree mt = new MerkleTree();
		try {
			hashValue = mt.createMerkleTree(qClient, qRecord);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//将第二层树存入hashvalue数据库
		hashvalueContr.saveHashValue(hashValue);
		*/
		/*
		RecordController jsonRec = new RecordController();
		Verification ver = new Verification();
	
		//输出json数据
		out = response.getWriter();    
		try {
			out.println(ver.getTruthVerificHashCode(date, client_no));
			out.println(jsonRec.getJSONRecord(date, client_no));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
