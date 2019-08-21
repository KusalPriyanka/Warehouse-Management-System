package com.wms.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wms.model.GRN;
import com.wms.service.GoodHandlingServiceImpl;
import com.wms.service.IGoodHandlingService;


@WebServlet("/InsertGRN")
public class InsertGRN extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public InsertGRN() {
        super();
       
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		GRN GRN = new GRN();
		GRN.setGRNNo(request.getParameter("GRNNo"));
		GRN.setDate(request.getParameter("Date"));
		GRN.setVehicleNo(request.getParameter("VehicleNum"));
		GRN.setContainerNo(request.getParameter("ContainerNo"));
		GRN.setTrailerNo(request.getParameter("TrailerNo"));
		GRN.setCusId(request.getParameter("cusId"));
		GRN.setsTime(request.getParameter("STime"));
		GRN.seteTime(request.getParameter("ETime"));
		GRN.setNoOfItems(Integer.parseInt(request.getParameter("NoOfProducts")));
		
		IGoodHandlingService iGoodHandlingService = new GoodHandlingServiceImpl();
		iGoodHandlingService.addGRN(GRN);
		
	}

}