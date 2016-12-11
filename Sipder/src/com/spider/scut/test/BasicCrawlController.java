package com.spider.scut.test;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.spider.scau.test.Sipder;

/**
 * @author wnagjiayong
 */


public class BasicCrawlController {

	static String url = "http://www2.scut.edu.cn/io/_t74/main.htm";

	
	public static void main2(String[] args) throws Exception {
		args = new String[2];
		args[0] = "D:\\upload";
		args[1] = "1000";


		//下载目录路径
		String crawlStorageFolder = args[0];

		//线程
		int numberOfCrawlers = Integer.parseInt(args[1]);

		CrawlConfig config = new CrawlConfig();

		config.setCrawlStorageFolder(crawlStorageFolder);
		//设置等待时间
		//config.setPolitenessDelay(1000);

		//爬取深度
		config.setMaxDepthOfCrawling(5);

		//页面抓取的最大数�?		//config.setMaxPagesToFetch(1000);

		//抓取恢复
		//config.setResumableCrawling(false);

		//User-Agent
		//config.setUserAgentString("");
		
		

		 
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		

		 
		//controller.addSeed("http://www.scau.edu.cn/yxbcgbl/gjjlc/");
		controller.addSeed(url);
		
		//�?��爬取
		controller.start(BasicCrawler.class, numberOfCrawlers);
	}
}
