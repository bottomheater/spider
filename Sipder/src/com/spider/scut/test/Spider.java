package com.spider.scut.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.jgroups.util.UUID;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.spider.scut.dao.Event;
import com.spider.scut.dao.Person;

public class Spider {  

	private Logger log = Logger.getLogger(Spider.class);
	
	static String filename = "http://www2.scut.edu.cn"
	+"/_upload/article/files/e1/9b/2d756985474d8018c9ebb4b6d4b3/40fe0b05-933d-4d7a-bea9-9101ddc00702.docx";
	//static String filename = "C:\\Users\\Administrator\\Desktop\\sample\\"+"b8b13f8e-1343-4e90-9cd3-e6138fddd172.docx";
	String url = "http://www2.scut.edu.cn/io/_t74/main.htm";
	//String url = "http://www2.scut.edu.cn/io/_t74/2014/1130/c2055a51387/page.htm";
	//String url = "http://www2.scut.edu.cn/io/_t74/2016/1109/c2046a131156/page.htm";
	String baseurl = "http://www2.scut.edu.cn";	
	//String url = "http://www2.scut.edu.cn/io/_t74/2016/0509/c2046a50886/page.htm";
	
	File file ;
	FileWriter out;

	long time=0;
	int docNum=0;
	int tableNum=0;
	
	static List urls;
	static List ready;
	Configuration cfg;
    SessionFactory sf;

	public static void main(String[] args){
		new Spider();
	}
	
	public void init() throws IOException{
		urls = new ArrayList();
		ready = new ArrayList();
		cfg = new Configuration();
        sf = cfg.configure().buildSessionFactory();  
        file = new File("info.txt");
		if (file.exists()) file.delete();
		out = new FileWriter(file);	
		time = System.currentTimeMillis();
		print("==========开始抓取======================================================");
		
	}
	
	public void running() throws Exception{
		//this.filter(url);
		filterWithThought();
		//this.filterWithCrawler(url); //调用开源框架
        //for (Object t: urls)
        	//this.manage((String)t);
        //ReadTable();
        //ReadDoc(filename);
	}
	
	public void filterWithCrawler(String url) throws Exception{
		urls.clear();
		BasicCrawlController.main2(new String[2]);
		urls = BasicCrawler.data;
		print("Crawler过滤后元素有: "+urls.size());
	}
	
	public void filterWithThought() throws Exception{
		//urls.clear();
		long ttime = System.currentTimeMillis();
		String nowPart = "50885";
		String url;
		
		for (int i=50885; i>0; i--) {
			nowPart = String.format("%05d", i);
			
			url = "http://www2.scut.edu.cn/io/_t74/2016/1124/c2046a"
					+ nowPart + "/page.htm";
			Response response;
			try {
				response = Jsoup.connect(url).userAgent("Mozilla")
				.timeout(3000)
				.execute();
			} catch (Exception e) {
				// 页面返回错误，即非200 
				print("当前URL：" + url + "  页面不存在");
				return;
			}
			Document doc = response.parse();
			print("获取：" +doc.title()+"&&"+url);
			if (isNeed(doc.title())){
				//print("获取：" +doc.title()+"&&"+url);
				urls.add(url);
			}
		}
		print("再次过滤后元素有: "+urls.size());
		print("Crawler运行时间: "+ (System.currentTimeMillis()-ttime)/1000 +"s");
	}
	
	public void destory() throws IOException{
		print("==========统计信息===============");
        print("运行时间："+ (System.currentTimeMillis()-time)/1000 +"s");
        print("共处理："+ urls.size() );
        print("doc文档有："+ docNum );
        print("网页表格有："+ tableNum );
        print("已写入到数据库spider中。");
        print("Create By 王家永");
		print("==========结束抓取======================================================");
		print("已保存到目录为 "+file.getAbsolutePath());
		out.close();
        sf.close(); 
	}
	
	public Spider(){
		try {
			init();
			
			running();
	        
	        destory();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
   /* 读取页面上doc文档中的表格信息*/  
   public void ReadDoc(String filename, String publishDate) throws Exception {  
	  if (! (filename.endsWith(".docx") || filename.endsWith(".doc")))
		  return;
      //InputStream is = new FileInputStream(filename);
	  InputStream is = getInputStream(filename);
      XWPFDocument doc = new XWPFDocument(is);  
     /* List<XWPFParagraph> paras = doc.getParagraphs();  
      for (XWPFParagraph para : paras) {  
         //当前段落的属性  
//       CTPPr pr = para.getCTP().getPPr();  
         System.out.println(para.getText());  
      }*/  
      
      //获取文档中所有的表格  
      List<XWPFTable> tables = doc.getTables();  
      if (tables.size()<1){
    	  System.out.println("没有获取到表格"+tables.size());
    	  return;
      }
      List<XWPFTableRow> rows;  
      List<XWPFTableCell> cells;  
      
      for (XWPFTable table : tables) {  
         //表格属性  
         //获取表格对应的行  
         rows = table.getRows();  

         //因公出访相关信息 *7
         //rows.get(0)
         String teamName = rows.get(1).getTableCells().get(1).getText();
         String TargetCountry = rows.get(2).getTableCells().get(1).getText();
         String task = rows.get(3).getTableCells().get(1).getText();
         String info = rows.get(4).getTableCells().get(1).getText();
         String travel = rows.get(5).getTableCells().get(1).getText();
         String fee = rows.get(6).getTableCells().get(1).getText();
         
         String id = createId();
         
         //出访团全体成员基本信息 *n
         for (int j=1+8; j<rows.size(); j++) {  
            //获取行对应的单元格  
            cells = rows.get(j).getTableCells();
            
            String name = cells.get(0).getText();
            String department = cells.get(1).getText();
            String job = cells.get(2).getText();
            String date = cells.get(3).getText();
            
            Person person = new Person(name, department, job, date);
            person.setEventId(id);
            print("Person: "+JSONObject.toJSONString(person));
            this.save(person);
            
         }
         
         Event event = new Event(id, teamName, TargetCountry, task,	info, travel, fee);
         event.setPublishDate(publishDate);
         this.save(event);
         print("Event: "+JSONObject.toJSONString(event));
      }  
      
      if (is != null) {  
          try {  
             is.close();
          } catch (IOException e) {  
             e.printStackTrace();  
          }  
       }  
   }  
     
   /*创建Event唯一识别号*/
   private String createId(){
	   return UUID.randomUUID().toString().replace("-", "");
   }

   /*保存到数据库中*/
   private void save(Object c){
	   Session session = sf.openSession();
	   Transaction transaction= session.beginTransaction();
	   try {
		   session.save(c);
	   } catch (RuntimeException re) {
		   throw re;
	   }
	   transaction.commit();
	   session.flush();
	   session.close();
	}
	
   private InputStream getInputStream(String filename) throws Exception{
	   URL url = new URL(filename);
	   return url.openConnection().getInputStream();
	   
   }
   
   /*处理当前页面，默认当前链接一定是出国公示*/
   private void manage(String url) throws Exception{
	   print("正在处理："+url);
	   Document doc = Jsoup.connect(url)
				.userAgent("Mozilla")  
		        .cookie("auth", "token") 
		        .timeout(5000)
		        .get();
		//Document doc = Jsoup.parse(file, "gbk");
		Elements list = doc.select("table[width=568]");	
		Element tmpDate = doc.getElementsByClass("Article_PublishDate").get(0);
		String date = tmpDate.text();
		print("Date"+date);
		//页面没有显示信息，即没有表格元素，有doc文档
		if(list.size()<1){
			print("无法获取表格，读取DOC文档");
			Elements docs = doc.getElementsByClass("Article_Content");
			if (docs.isEmpty()) return;
			Elements as = docs.get(0).getElementsByTag("a");
			if (as.isEmpty()) return;
			for(Element e: as){
				String t = baseurl+e.attr("href");
				print("文档链接： "+t);
				this.ReadDoc(t, date);
				docNum++;
			}
		}else{
			print("获取网页表格，读取");
			tableNum++;
			this.ReadTable(list, date);
		}
		
   }
   
   /*读取页面中的表格（table元素）*/
   public void ReadTable(Elements list, String publishDate) throws IOException{
	    
		Element table = list.get(0);
		Elements tr = table.select("tr");
		String[] tmp = new String[7];
		for(int i=1; i<7; i++){
			//System.out.print(i+tr.get(i).toString());
			tmp[i] = tr.get(i).select("td").get(1).text();
			//System.out.println(tmp[i]);
			
		}
		String id = this.createId();
		Event event = new Event(id, tmp[1], tmp[2], tmp[3], tmp[4], tmp[5], tmp[6]);
		event.setPublishDate(publishDate);
		
		for(int i=9; i<tr.size(); i++){
			//System.out.print(i+tr.get(i).toString());
			Elements e = tr.get(i).select("td");
			
			String name = e.get(0).text();
			String department = e.get(1).text();
			String job = e.get(2).text();
			String date = e.get(3).text();
			
			Person person = new Person(name, department, job, date);
			person.setEventId(id);
			print("Person: "+JSONObject.toJSONString(person));
			this.save(person);
		}
		print("Event: "+JSONObject.toJSONString(event));
		this.save(event);
		
		
   }
   
   
   /*从目录中获取连接*/
   private void filter(String url) throws IOException{
	   urls.clear();
	   
	   Document doc = Jsoup.connect(url)
				.userAgent("Mozilla")  
		        .cookie("auth", "token") 
		        //.timeout(5000)
		        .get();
		//Document doc = Jsoup.parse(file, "gbk");
		Elements list = doc.select("a");
		print("找到元素有: "+list.size());
		for (Element e : list){
			//System.out.println("正在过滤："+e.text()+"&&"+baseurl+e.attr("href"));
			if ( isNeed(e.text()) ){
				urls.add(baseurl+e.attr("href"));
				//System.out.println(e.text()+"&&"+baseurl+e.attr("href"));
			}
			if (ready.contains(url)) ready.remove(url);
			
			if ( isAdd(e.attr("href")) ){
				ready.add(baseurl+e.attr("href"));
			}
				
		}
		print("过滤后元素有: "+urls.size());
   }
   
   private boolean isNeed(String str){
	   return ( str.contains("公示") || str.contains("关于") || str.contains("出国有关")  ) 
			   && ( str.contains("赴") || str.contains("因公") ) 
			   && !(str.contains("通知"))	 ;
   }
   
   private boolean isAdd(String str){
	   return ( str.endsWith("html") || str.endsWith("jsp") 
				|| str.endsWith("htm")	 );
   }
   
	/**
	 * 打印
	 * @param message
	 * @throws IOException
	 */
	private void print(String message) throws IOException{
		log.info(message);
		//System.out.println(message);
		//log.info(message+"\n");
		//out.append(message+"\n");
	}
	
	private String getBreifString(String str){
		return str.replaceAll("来源于", "")
				.replaceAll("总共费用", "")
				.replaceAll("其", "")
				.replaceAll("；", "");
	}
}  