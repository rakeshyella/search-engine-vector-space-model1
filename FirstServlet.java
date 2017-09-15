//package org.package1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FirstServlet
 */
@WebServlet(description = "First Servlet", urlPatterns = { "/FirstServletPath" })
public class FirstServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("Hello from Post Metohd 1");
		PrintWriter writer =  response.getWriter();
		String relevanceFB = "false";
		String query = request.getParameter("Query");
		String index = request.getParameter("Index");
		
		if(! query.equalsIgnoreCase(" ") && query.length()>0 && !index.equalsIgnoreCase(" ") && (index.length()> 0)){
			if(index.equalsIgnoreCase("yes") || index.equalsIgnoreCase("no"))
			{
		MasterFile mf = new MasterFile();
		String[] queryArray = new String[2]; 
		queryArray[0] = query;
		String function = "Vector";
		
		ArrayList<HashMap<String, String>> resultArrayList = mf.main(queryArray,index,relevanceFB,function);
		//HashMap<Integer, Double> 
 		request.getSession().setAttribute("Array List", resultArrayList);
 		request.getSession().setAttribute("Query" ,query);
		response.sendRedirect("SuccessPage.jsp");
		}
		else{
			response.sendRedirect("Nodataa1.jsp");
		}
}
		else{
			response.sendRedirect("NoDataa.jsp");	
			
		}
		
	}

}
