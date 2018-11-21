package com.inv.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inv.model.RecordController;
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


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
