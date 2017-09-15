
import java.util.ArrayList;
import java.util.HashSet;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection;
import org.jsoup.Jsoup;


public class ParsingDocs 
{
	public void parsing(ArrayList<String> URL_Frontier) 
	{
		
		String url;
		ArrayList<String> URL_List = URL_Frontier;
		HashSet<String> titleArray = new HashSet<String>();
		HashSet<String> URL_Repositry = new HashSet<String>();
		int k=1;
		for(int j=1; j<=100;j++)
		{
			url = URL_List.remove(0);
			if(!URL_Repositry.contains(url))
			{
				
	    		//System.out.println(url);	
	    		try
	    		{
	    			
	    			String outputFile ="";
	    			Connection connection = Jsoup.connect(url);
	    			Document htmlDocument = connection.get();
	    			String htmlString = htmlDocument.html();
	    			String title = htmlDocument.title();
	    			if(title.length()>=20)
	    			{
	    				title= title.substring(0,19);
	    			}
	    			
	    			if(title.contains("|"))
	    					{
	    				      title = title.replace("|", "");
	    					}
	    			
	    			if((URL_Repositry.size()==0) || (!(titleArray.contains(title))))
	    					{
	    				outputFile = "C:\\Users\\shaha\\Desktop\\Crawl\\"+title+".html";
	    				titleArray.add(title);
	    					}
	    			else
	    			{
	    				outputFile = "C:\\Users\\shaha\\Desktop\\Crawl\\"+title+k+".html";
	    				k++;
	    			}
	    			DownloadDocs dd = new DownloadDocs();
	    			
	    			dd.downloadingDocs(outputFile,htmlString);
	    			URL_Repositry.add(url);
	    			
	    			Elements linksOnPage = htmlDocument.select("a[href]");
	    			for(Element a:linksOnPage)
	    			{
	    				String absHref = a.attr("abs:href");
	    				if(!URL_List.contains(absHref))
	    				{
	    					URL_List.add(absHref);
	    				}
	    			}
	    		}
	    		catch(Exception e)
	    		{
	    			System.out.println("Skipping URL"+ url);
	    		}
	    	}
		}
		System.out.println(URL_Repositry);
	    System.out.println("REposirty :" +URL_Repositry.size());
	    System.out.println("Frontier :" +URL_List.size());
	}
}
 

