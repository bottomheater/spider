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
		print("==========��ʼץȡ======================================================");
		
	}
	
	public void running() throws Exception{
		//this.filter(url);
		filterWithThought();
		//this.filterWithCrawler(url); //���ÿ�Դ���
        //for (Object t: urls)
        	//this.manage((String)t);
        //ReadTable();
        //ReadDoc(filename);
	}
	
	public void filterWithCrawler(String url) throws Exception{
		urls.clear();
		BasicCrawlController.main2(new String[2]);
		urls = BasicCrawler.data;
		print("Crawler���˺�Ԫ����: "+urls.size());
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
				// ҳ�淵�ش��󣬼���200 
				print("��ǰURL��" + url + "  ҳ�治����");
				return;
			}
			Document doc = response.parse();
			print("��ȡ��" +doc.title()+"&&"+url);
			if (isNeed(doc.title())){
				//print("��ȡ��" +doc.title()+"&&"+url);
				urls.add(url);
			}
		}
		print("�ٴι��˺�Ԫ����: "+urls.size());
		print("Crawler����ʱ��: "+ (System.currentTimeMillis()-ttime)/1000 +"s");
	}
	
	public void destory() throws IOException{
		print("==========ͳ����Ϣ===============");
        print("����ʱ�䣺"+ (System.currentTimeMillis()-time)/1000 +"s");
        print("������"+ urls.size() );
        print("doc�ĵ��У�"+ docNum );
        print("��ҳ����У�"+ tableNum );
        print("��д�뵽���ݿ�spider�С�");
        print("Create By ������");
		print("==========����ץȡ======================================================");
		print("�ѱ��浽Ŀ¼Ϊ "+file.getAbsolutePath());
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
	
   /* ��ȡҳ����doc�ĵ��еı����Ϣ*/  
   public void ReadDoc(String filename, String publishDate) throws Exception {  
	  if (! (filename.endsWith(".docx") || filename.endsWith(".doc")))
		  return;
      //InputStream is = new FileInputStream(filename);
	  InputStream is = getInputStream(filename);
      XWPFDocument doc = new XWPFDocument(is);  
     /* List<XWPFParagraph> paras = doc.getParagraphs();  
      for (XWPFParagraph para : paras) {  
         //��ǰ���������  
//       CTPPr pr = para.getCTP().getPPr();  
         System.out.println(para.getText());  
      }*/  
      
      //��ȡ�ĵ������еı��  
      List<XWPFTable> tables = doc.getTables();  
      if (tables.size()<1){
    	  System.out.println("û�л�ȡ�����"+tables.size());
    	  return;
      }
      List<XWPFTableRow> rows;  
      List<XWPFTableCell> cells;  
      
      for (XWPFTable table : tables) {  
         //�������  
         //��ȡ����Ӧ����  
         rows = table.getRows();  

         //�򹫳��������Ϣ *7
         //rows.get(0)
         String teamName = rows.get(1).getTableCells().get(1).getText();
         String TargetCountry = rows.get(2).getTableCells().get(1).getText();
         String task = rows.get(3).getTableCells().get(1).getText();
         String info = rows.get(4).getTableCells().get(1).getText();
         String travel = rows.get(5).getTableCells().get(1).getText();
         String fee = rows.get(6).getTableCells().get(1).getText();
         
         String id = createId();
         
         //������ȫ���Ա������Ϣ *n
         for (int j=1+8; j<rows.size(); j++) {  
            //��ȡ�ж�Ӧ�ĵ�Ԫ��  
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
     
   /*����EventΨһʶ���*/
   private String createId(){
	   return UUID.randomUUID().toString().replace("-", "");
   }

   /*���浽���ݿ���*/
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
   
   /*����ǰҳ�棬Ĭ�ϵ�ǰ����һ���ǳ�����ʾ*/
   private void manage(String url) throws Exception{
	   print("���ڴ���"+url);
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
		//ҳ��û����ʾ��Ϣ����û�б��Ԫ�أ���doc�ĵ�
		if(list.size()<1){
			print("�޷���ȡ��񣬶�ȡDOC�ĵ�");
			Elements docs = doc.getElementsByClass("Article_Content");
			if (docs.isEmpty()) return;
			Elements as = docs.get(0).getElementsByTag("a");
			if (as.isEmpty()) return;
			for(Element e: as){
				String t = baseurl+e.attr("href");
				print("�ĵ����ӣ� "+t);
				this.ReadDoc(t, date);
				docNum++;
			}
		}else{
			print("��ȡ��ҳ��񣬶�ȡ");
			tableNum++;
			this.ReadTable(list, date);
		}
		
   }
   
   /*��ȡҳ���еı��tableԪ�أ�*/
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
   
   
   /*��Ŀ¼�л�ȡ����*/
   private void filter(String url) throws IOException{
	   urls.clear();
	   
	   Document doc = Jsoup.connect(url)
				.userAgent("Mozilla")  
		        .cookie("auth", "token") 
		        //.timeout(5000)
		        .get();
		//Document doc = Jsoup.parse(file, "gbk");
		Elements list = doc.select("a");
		print("�ҵ�Ԫ����: "+list.size());
		for (Element e : list){
			//System.out.println("���ڹ��ˣ�"+e.text()+"&&"+baseurl+e.attr("href"));
			if ( isNeed(e.text()) ){
				urls.add(baseurl+e.attr("href"));
				//System.out.println(e.text()+"&&"+baseurl+e.attr("href"));
			}
			if (ready.contains(url)) ready.remove(url);
			
			if ( isAdd(e.attr("href")) ){
				ready.add(baseurl+e.attr("href"));
			}
				
		}
		print("���˺�Ԫ����: "+urls.size());
   }
   
   private boolean isNeed(String str){
	   return ( str.contains("��ʾ") || str.contains("����") || str.contains("�����й�")  ) 
			   && ( str.contains("��") || str.contains("��") ) 
			   && !(str.contains("֪ͨ"))	 ;
   }
   
   private boolean isAdd(String str){
	   return ( str.endsWith("html") || str.endsWith("jsp") 
				|| str.endsWith("htm")	 );
   }
   
	/**
	 * ��ӡ
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
		return str.replaceAll("��Դ��", "")
				.replaceAll("�ܹ�����", "")
				.replaceAll("��", "")
				.replaceAll("��", "");
	}
}  