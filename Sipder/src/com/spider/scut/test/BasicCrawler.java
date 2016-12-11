package com.spider.scut.test;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


//import org.apache.http.Header;

/**
 * @author wangjaiyong
 */

public class BasicCrawler extends WebCrawler {

	public static List data = new ArrayList();
	
	private static int i=0;
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	String baseurl = "http://www2.scut.edu.cn/io";	

	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		boolean isVisit =  !FILTERS.matcher(href).matches();
		if (!href.contains(baseurl)) isVisit=false;
		//System.out.println((i++)+"Search: "+isVisit+", "+href);
		return isVisit;
	}

	@Override
	public void visit(Page page) {
		//System.out.println("=============");
		String url = page.getWebURL().getURL();
		
		if (page.getParseData() instanceof HtmlParseData) {
			//System.out.println("HTML");	
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String title = htmlParseData.getTitle();
			if (!isNeed(title)){
				//System.out.println("======不符合=======");
				return;
			}
			
			data.add(url);

			System.out.println("url:" +url);
			System.out.println("Title:" +title);			
			
	
			/*
			List<WebURL> links = htmlParseData.getOutgoingUrls();
			System.out.println("Number of outgoing links: " + links.size());          
			*/
		}

		

		//System.out.println("=============");
	}
	
	   private boolean isNeed(String str){
		   return ( str.contains("公示") || str.contains("关于") || str.contains("出国有关")  ) 
				   && ( str.contains("赴") || str.contains("因公") ) 
				   && !str.contains("通知")	   ;
	   }
}
