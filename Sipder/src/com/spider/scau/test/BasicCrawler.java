package com.spider.scau.test;
/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *//*

package com.spider.test;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.Entity;
//import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

*//**
 * @author wangjaiyong
 *//*
@Entity
public class BasicCrawler extends WebCrawler {

	
	
	private static int i=0;
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");


	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		boolean isVisit =  !FILTERS.matcher(href).matches() && href.contains("http://www.scau.edu.cn/yxbcgbl/gjjlc/");
		if(isVisit) System.out.println((i++)+"Search: "+isVisit+", "+href);
		return isVisit;
	}

	@Override
	public void visit(Page page) {
		System.out.println("=============");
		String url = page.getWebURL().getURL();
		//System.out.println("URL: " + url);

		if (url.endsWith("jsp")){
			System.out.println((i++)+"=: " + url);
			try {
				BasicCrawlController.getInstance().manage(url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		
		if (page.getParseData() instanceof HtmlParseData) {
			//System.out.println("HTML");	
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			System.out.println("HTML:" +html);			
			Document doc = Jsoup.parse(html);
			Elements lists = doc.select("option");
			//Elements lists = doc.getElementsByClass("smallblack");
			System.out.println("List:" +lists.size());
			for (Element list : lists){	
				System.out.println(list.text());
				System.out.println(list.data());
				System.out.println(list.attr("value"));
			}

				
			
			List<WebURL> links = htmlParseData.getOutgoingUrls();

			System.out.println("Text length: " + text.length());
			System.out.println("Html length: " + html.length());
			System.out.println("Number of outgoing links: " + links.size());          
			
		}

		

		System.out.println("=============");
	}
}
*/