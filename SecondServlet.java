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
 * Servlet implementation class SecondServlet
 */
@WebServlet("/SecondServlet")
public class SecondServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("Hello from Post 2 Metohd");
    PrintWriter writer = response.getWriter();
    String select[] = request.getParameterValues("Url");
   
    String query = (String)request.getSession().getAttribute("Query");
    writer.println("Query iss " + query);
 
    if (select != null && select.length != 0) {
    	
  	  for (int j = 0; j < select.length; j++) {
  		 
  	  }
    }else{
    	writer.println("Null");
    }
    String args[] = new String[10];
    System.out.println("Query is " + query );
    args[0] = query;
    int i =1;
    for(String s1:select){
    args[i] = s1;
    i++;
    }
    String index = "no";
    String relevanceFB = "yes";
    MasterFile mf = new MasterFile();
    String function = "Vector";
    ArrayList<HashMap<String, String>> RelFbList = mf.main(args,index,relevanceFB,function);
    request.getSession().setAttribute("Rel fb list", RelFbList);
    response.sendRedirect("RelevanceFb.jsp");
	}
}
