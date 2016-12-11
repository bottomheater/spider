package com.spider.scau.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.persistence.Entity;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.Blob;
import com.spider.scau.dao.Content1;
import com.spider.scau.dao.Content2;

@Entity
public class Sipder {
	//private static final Logger log = LoggerFactory].getLogger(Sipder.class);
	
	
	//String url = "http://www.scau.edu.cn/yxbcgbl/gjjlc/201609/t20160921_141796.jsp";
	//String url="C:/Users/Administrator/Desktop/0.htm";
	//String url = "http://www.scau.edu.cn/yxbcgbl/gjjlc/201611/t20161102_142681.jsp";
	String url = "http://www.scau.edu.cn/yxbcgbl/gjjlc/index.htm";
	int totalUrls=0;
	int manageUrls=0;
	int errorUrls=0;
	long time=0;
	
	ArrayList<String> urlList;//处理队列
	ArrayList<String> readyList;//就绪队列
	File file ;
	InputStream fis;
	byte[] content = null;
	FileWriter out;
	Configuration cfg;
    SessionFactory sf;

    /*public Sipder() throws IOException{
    	init();
    }*/
    
    /*
     * 
     * 初始化系统参数
     * 	处理队列和就绪队列
     * 	日志文件
     * 	开始时间
     * 	hibernate参数
     */
	public void init() throws IOException{
		urlList = new ArrayList<String>();
		readyList = new ArrayList<String>();
		file = new File("info.txt");
		if (file.exists()) file.delete();
		out = new FileWriter(file);	
		time = System.currentTimeMillis();
		cfg = new Configuration();
        //sf = cfg.configure().buildSessionFactory();
		sf = new AnnotationConfiguration().configure().buildSessionFactory();  
		//print("本文件目录为 "+file.getAbsolutePath());
        print("==========Create Logs======================================================");
        print("==========开始抓取======================================================");
	}
	
    /*
     * 
     * 销毁系统参数
     * 	日志文件
     * 	开始时间
     * 	hibernate参数
     */
	public void destory() throws IOException{
		long nowTime = System.currentTimeMillis();
		print("==========统计信息===============");
		print("共抓取 "+totalUrls+"个链接，\n" +
				"其中出国公示的有 "+manageUrls+"个，\n" +
				"无效链接有 "+errorUrls+"个，\n" +
				"已写入到数据库spider中。" +
				"用时："+( (nowTime-time)/1000 )+"s\n"+
				"Create By 王家永");
		print("==========结束抓取======================================================");
		print("已保存到目录为 "+file.getAbsolutePath());
        sf.close();     
		out.close();
	}
	
	public Sipder() {
		// TODO Auto-generated constructor stub
		try {
			this.init();
			//urlList.add(url);
			//this.manage(url);
			for(int i=1; i<20; i++){
				this.filter(url);
				url = "http://www.scau.edu.cn/yxbcgbl/gjjlc/index_"+i+".htm";
			}
			for (String e: urlList)
				this.manage(e);
			urlList.clear();
			this.destory();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main2(String[] args) {
			new Sipder();
	}



	/**
	 * 过滤无关链接
	 */
	public void filter(String url) throws IOException{
		print("正在过滤 _"+url);
		//File file = new File(url);
		Document doc = Jsoup.connect(url)
				.userAgent("Mozilla")  
		        .cookie("auth", "token") 
		        //.timeout(5000)
		        .get();
		//Document doc = Jsoup.parse(file, "gbk");
		Elements list = doc.getElementsByTag("a");;
		String str;
		int i = url.lastIndexOf("/")+1;
		//print(url.substring(0,i));
		//print("要保存的链接如下：");
		for (Element link : list){
			str = link.text();
			totalUrls++;
			if (this.isNeed(str)){
				manageUrls++;
				urlList.add(url.substring(0,i)+link.attr("href"));
				//out.append("暂存："+link.text()+" ="+url.substring(0,i)+link.attr("href"));
				print("暂存："+link.text()+" ="+url.substring(0,i)+link.attr("href"));
			}
			else  readyList.add(url.substring(0,i)+link.attr("href"));
		}
		print("====================================================\n\n\n");
	}

	/**
	 * 提取关键信息
	 */
	public void manage(String url) throws IOException{
		
		Response response;
		try {
			response = Jsoup.connect(url)
					   .userAgent("Mozilla")
					   //.timeout(10000)
					   .execute();
		} catch (Exception e) {
			/* 页面返回错误，即非200 */
			print("当前URL："+url+"  页面不存在");
			errorUrls++;
			return;
		}
		//print("状态 ："+response.statusCode()+" , "+url);
		print("当前URL："+url);
		
		Document doc = response.parse();
		
		//File file = new File(url);
		//Document doc = Jsoup.parse(file, "GBK");
		//Elements list = doc.select("table[width=880]");
		
		/*获取页面信息*/
		print("当前URL："+url);
		Elements list = doc.select("table.MsoNormalTable tbody tr td");
		if (list==null || list.size() <1){
			print("No manage");
			out.append("No manage");
			return;
		}
		/*获取附件链接*/
		String addition = this.manageAddition(doc);
		if (addition != null){
			int in = addition.lastIndexOf("&");
			addition = url.substring(0,url.lastIndexOf("/")+1)+ addition.substring(0, in);
			print( addition);
			
			URL addtionurl = new URL(addition);
			fis= addtionurl.openConnection().getInputStream();
			content=new byte[fis.available()];
			fis.read(content);
			//Blob blobContent = new Blob(content,i);
		} 
		
		
		/*根据表格形式获取表格信息*/
		//print("%"+list.get(0).text());
		if (list.get(0).text().contains("基")) this.manage1(list, addition, url);
		else if (list.get(0).text().contains("出访")) this.manage2(list, addition, url);
		else return;
		/*for (Element e : list){
			//print((i++)+e.text());
		}
		this.manage1(list);
		*/
	}
	
	/**
	 * 处理团队表格
	 * @param list
	 * @param addition
	 * @throws IOException
	 */
	private void manage1(Elements list, String addition, String url) throws IOException{
		String department = this.getReadString(list.get(2));
		String leader 	  = this.getReadString(list.get(4));
		String position   = this.getReadString(list.get(6));
		String days 	  = this.getReadString(list.get(8)).replaceAll("天", "");
		String teamNumber = this.getReadString(list.get(10));
		String teamPeople = this.getReadString(list.get(12));
		if ("1".equals(teamNumber)) teamPeople = leader;
		String rule1 	  = this.getReadString(list.get(14));
		String rule2 	  = this.getReadString(list.get(16));
		String rule3 	  = this.getReadString(list.get(18));
		String process 	  = this.getReadString(list.get(19));
		String result 	  = this.getReadString(list.get(20));
		String influence  = this.getReadString(list.get(21));
		String suggestion = this.getReadString(list.get(22));
		String agreement  = this.getReadString(list.get(23));
		String other 	  = list.get(25).text();
		
		if ("".equals(teamPeople)) teamPeople = leader;
		String format = String.format("所在部门:%s\n 团组长:%s\n 出访国家/地区:%s\n 出访天数:%s\n " +
				"团组人数:%s\n 团组人员姓名:%s\n 有无未经批准擅自延长在外停留时间、更改行程、增加出访国家（地区）:%s\n 是否履行出访经费先行审核手续:%s\n 是否提交出访报告:%s\n " +
				"简述出访目的、任务、实际行程:%s\n 简述本次出访所取得的直接工作成果:%s\n 简述本次出访所延伸的潜在影响和成效:%s\n 下一步落实出访合作成果及其他后续工作的对策建议出访天数:%s\n " +
				"团组自评:%s\n 备注:%s\n", 
				department, leader, position, days,
				teamNumber, teamPeople, rule1, rule2, rule3,
				process, result, influence, suggestion,
				agreement, other);
		
		out.append(format);
		out.flush();
		print(format);
/*		print("insert into spider.content2 (leader, department, job, position, waste, target, process, path, from, addition) " +
				"values ("+department+", "+leader+", "+position+", "+days+", " +
						"?, ?, ?, ?, ?, ?)");*/
		
		Content1 c= new Content1();
		c.setDepartment(department);
		c.setLeader(leader);
		c.setPosition(position);
		c.setDays(days);
		c.setTeamNumber(teamNumber);
		c.setTeamPeople(teamPeople);
		c.setRule1(rule1);
		c.setRule2(rule2);
		c.setRule3(rule3);
		c.setProcess(process);
		c.setResult(result);
		c.setInfluence(influence);
		c.setSuggestion(suggestion);
		c.setAgreement(agreement);
		c.setOther(other);
		c.setAddition(addition);
		c.setUrl(url);
		
        this.save(c);
	}
	
	/**
	 * 处理个人表格
	 * @param list
	 * @param addition
	 * @throws IOException
	 */
	private void manage2(Elements list, String addition, String url) throws IOException{
		String leader 		= this.getReadString(list.get(5));
		String department 	= this.getReadString(list.get(6));
		String job 			= this.getReadString(list.get(7));
		String position 	= this.getReadString(list.get(8));
		String waste 		= this.getReadString(list.get(9));
		String target 		= this.getReadString(list.get(11));
		String process 		= this.getReadString(list.get(13));
		String path 		= this.getReadString(list.get(15));
		String from 		= this.getReadString(list.get(17));
		
		String format = String.format("出访人员姓名:%s\n 出访人员单位:%s\n 出访人员职务:%s\n 出访国家/地区:%s\n " +
				"经费来源（经费卡号）和预计总费用:%s\n 出访任务:%s\n 日程安排:%s\n 往返航线:%s\n 邀请单位:%s\n ", 
				leader, department, job, position,
				waste, target, process, path, from);
		out.append(format);
		out.flush();
		print(format);
		/*print("insert into spider.content2 (leader, department, job, position, waste, target, process, path, from, addition) " +
				"values ('"+leader+"', '"+department+"', '"+job+"', '"+position+"', " +
						"'"+waste+"', '"+target+"', '"+process+"', '"+path+"', '"+from+"', '"+addition+"')");
		*/
		Content2 c = new Content2();
		c.setLeader(leader);
		c.setDepartment(department);
		c.setJob(job);
		c.setPosition(position);
		c.setWaste(waste);
		c.setTarget(target);
		c.setProcess(process);
		c.setPath(path);
		c.setFromm(from);
		c.setAddition(addition);
		
		this.save(c);
	}
	
	/**
	 * 处理附件信息
	 * @param doc
	 * @return
	 */
	private String manageAddition(Document doc){
		String type = "pdf|doc|docx";
		String str;
		Elements list = doc.select("a[href]");
		for (Element e : list){
			str = e.attr("href");
			int i = str.lastIndexOf(".");
			if (i> -1){
				if (type.contains( str.substring(i+1) ))
					return str+"&"+e.text();
			}
		}
		return null;
	}
	
	/**
	 * 保存到数据库中
	 * @param c
	 */
	private void save(Object c){
		Session session = sf.openSession();
        session.beginTransaction();
        //print("SQL "+c.toString());
        session.save(c);
        session.getTransaction().commit();
        session.close();
	}
	
	/**
	 * 处理非法节点信息
	 * @param e
	 * @return
	 */
	private String getReadString(Element e){
		if (e == null) return "无";
		
		String str = e.text();
		//print(str);
		if (str==null || "".equals(str)) return "无";

		if ("??".equals(str)) return "无";
		
		//str = str.replaceAll("?", "");
		
		
/*		int index = str.indexOf("：");
		if (index > -1) str = str.substring(index);
		index = str.indexOf(":");
		if (index > -1) str = str.substring(index);
*/
	
		return this.replace( str.trim() );
		
	}
	
	/**
	 * 过滤条件
	 * @param str
	 * @return
	 */
	private boolean isNeed(String str){
		return str.contains("出访") && (str.contains("公示")||str.contains("公布")||str.contains("公告"));
	}
	
	/**
	 * 打印
	 * @param message
	 * @throws IOException
	 */
	private void print(String message) throws IOException{
		System.out.println(message);
		//log.info(message+"\n");
		out.append(message+"\n");
	}
	
	/**
	 * 规范数据
	 * @param str
	 * @return
	 */
	private String replace(String str){
		return str
				.replaceAll("简述出访目的、任务、实际行程：", "")
				.replaceAll("简述本次出访所取得的直接工作成果（要求尽可能量化说明）：", "")
				.replaceAll("简述本次出访所延伸的潜在影响和成效（如对本地区、本单位工作部署、计划项目或关联业务的影响、借鉴意义等）：", "")
				.replaceAll("下一步落实出访合作成果及其他后续工作的对策建议：", "")
				.replaceAll("团组自评：", "")
				//.replaceAll("?", "")
		;
	}
}
