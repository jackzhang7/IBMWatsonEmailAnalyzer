package com.social.UI.controller;

import java.io.IOException;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.social.UI.model.JQueryDataTableParamModel;
import com.social.UI.model.MySocialContent;
import com.social.UI.model.MySocialContentRepository;

/**
 * CompanyServlet provides data to the JQuery DataTables
 */
public class MySocialContentGsonObjectsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public MySocialContentGsonObjectsServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		JQueryDataTableParamModel param = DataTablesParamUtility.getParam(request);
		
		String sEcho = param.sEcho;
		int iTotalRecords; // total number of records (unfiltered)
		int iTotalDisplayRecords; //value will be set when code filters mySocialContents by keyword

		iTotalRecords = MySocialContentRepository.GetMySocialContents().size();
		List<MySocialContent> mySocialContents = new LinkedList<MySocialContent>();
		for(MySocialContent c : MySocialContentRepository.GetMySocialContents()){
			if(	c.getContent().toLowerCase().contains(param.sSearch.toLowerCase())
					||
					c.getFromWho().toLowerCase().contains(param.sSearch.toLowerCase())
					||
					c.getKind().toLowerCase().contains(param.sSearch.toLowerCase())
					||
					c.getReceivedTime().toString().contains(param.sSearch.toLowerCase())
					||
					c.getSubject().toString().toLowerCase().contains(param.sSearch.toLowerCase())
	                ||
	                c.getToMe().toString().toLowerCase().contains(param.sSearch.toLowerCase()))
				
			{
				mySocialContents.add(c); // add socialContents that matches given search criterion
			}
		}
		iTotalDisplayRecords = mySocialContents.size();// number of mySocialContents that match search criterion should be returned
		
		final int sortColumnIndex = param.iSortColumnIndex;
		final int sortDirection = param.sSortDirection.equals("asc") ? -1 : 1;
		
		Collections.sort(mySocialContents, new Comparator<MySocialContent>(){
			@Override
			public int compare(MySocialContent c1, MySocialContent c2) {	
				switch(sortColumnIndex){
				case 0:
					return c1.getContent().compareTo(c2.getContent()) * sortDirection;
				case 1:
					return c1.getFromWho().compareTo(c2.getFromWho()) * sortDirection;
				case 2:
					return c1.getKind().compareTo(c2.getKind()) * sortDirection;
				case 3:
					return c1.getReceivedTime().compareTo(c2.getReceivedTime()) * sortDirection;
				case 4:
					return c1.getSubject().compareTo(c2.getSubject()) * sortDirection;
				case 5:
					return c1.getToMe().compareTo(c2.getToMe()) * sortDirection;
				case 6:	
					return c1.getSentiment().compareTo(c2.getSentiment()) * sortDirection;
				}
				return 0;
			}
		});
		
		if(mySocialContents.size()< param.iDisplayStart + param.iDisplayLength) {
			mySocialContents = mySocialContents.subList(param.iDisplayStart, mySocialContents.size());
		} else {
			mySocialContents = mySocialContents.subList(param.iDisplayStart, param.iDisplayStart + param.iDisplayLength);
		}
	
		try {	
			JsonObject jsonResponse = new JsonObject();		
			jsonResponse.addProperty("sEcho", sEcho);
			jsonResponse.addProperty("iTotalRecords", iTotalRecords);
			jsonResponse.addProperty("iTotalDisplayRecords", iTotalDisplayRecords);			
			Gson gson = new Gson();
			jsonResponse.add("aaData", gson.toJsonTree(mySocialContents));
			
			response.setContentType("application/Json");
			response.getWriter().print(jsonResponse.toString());
			
		} catch (JsonIOException e) {
			e.printStackTrace();
			response.setContentType("text/html");
			response.getWriter().print(e.getMessage());
		}

		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	

	
	
	
	
	

}