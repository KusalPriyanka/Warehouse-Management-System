package com.wms.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.wms.model.GRN_Qty;
import com.wms.service.GoodHandlingServiceImpl;
import com.wms.service.IGoodHandlingService;


@WebServlet("/showGRNQty")
public class showGRNQty extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public showGRNQty() {
        super();
       
    }

    

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String GRNNo = request.getParameter("grn");
		IGoodHandlingService goodHandlingService = new GoodHandlingServiceImpl();
		ArrayList<GRN_Qty> grn_Qty = goodHandlingService.getGRNQTYView(GRNNo);
		
		for(GRN_Qty GQ : grn_Qty) {
			GQ.setItemDes(goodHandlingService.getItemName(GQ.getItemId()));
		}
		
		Gson gson = new Gson();
		
		PrintWriter out = response.getWriter();
		out.print(gson.toJson(grn_Qty));
		out.flush();
		out.close();
		
		
	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

	}

}
