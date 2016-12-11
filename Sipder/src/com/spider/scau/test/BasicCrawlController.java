package com.spider.scau.test;
/*package com.spider.test;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

*//**
 * @author wnagjiayong
 *//*
@Entity
public class BasicCrawlController {

	@ManyToOne
	private static Sipder spider;
	
	public static Sipder getInstance(){
		return spider;
	}
	
	public static void main(String[] args) throws Exception {
		args = new String[2];
		args[0] = "D:\\upload";
		args[1] = "1000";
		
		if (args.length != 2) {
			System.out.println("Needed parameters: ");
			System.out.println("\t rootFolder (it will contain intermediate crawl data)");
			System.out.println("\t numberOfCralwers (number of concurrent threads)");
			return;
		}

		//爬虫处理�?		spider = new Sipder();
		
		//下载目录路径
		String crawlStorageFolder = args[0];

		//线程�?		int numberOfCrawlers = Integer.parseInt(args[1]);

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
		
		
		 * Instantiate the controller for this crawl.
		 
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 
		//controller.addSeed("http://www.scau.edu.cn/yxbcgbl/gjjlc/");
		String url = "http://www.scau.edu.cn/yxbcgbl/gjjlc/index.htm";
		for(int i=1; i<20; i++){
			controller.addSeed(url);
			url = "http://www.scau.edu.cn/yxbcgbl/gjjlc/index_"+i+".htm";
		}
		
		//�?��爬取
		controller.start(BasicCrawler.class, numberOfCrawlers);
	}
}
*/