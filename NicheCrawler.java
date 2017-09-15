import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NicheCrawler {
	public static void main(String args[])throws SocketTimeoutException,IOException,MalformedURLException
	{ 
		ArrayList<String> URL_Frontier= new ArrayList<String>();
		String URL= "http://www.bbcnews.com";
		URL_Frontier.add(URL);
		Connection connection = Jsoup.connect(URL);
		Document htmlDocument = connection.get();
		Elements linksOnPage = htmlDocument.select("a[href]");
		for(Element a:linksOnPage)
		{
			String absHref = a.attr("abs:href");
			URL_Frontier.add(absHref);
			if(URL_Frontier.size()== 10)
			{
				break;
			}
		}
		System.out.println("Frontier Size Niche Crawler"+ URL_Frontier.size());
		ParsingDocs pd = new ParsingDocs();
		pd.parsing(URL_Frontier);
	}
}
