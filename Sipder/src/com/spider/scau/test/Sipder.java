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
	
	ArrayList<String> urlList;//�������
	ArrayList<String> readyList;//��������
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
     * ��ʼ��ϵͳ����
     * 	������к;�������
     * 	��־�ļ�
     * 	��ʼʱ��
     * 	hibernate����
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
		//print("���ļ�Ŀ¼Ϊ "+file.getAbsolutePath());
        print("==========Create Logs======================================================");
        print("==========��ʼץȡ======================================================");
	}
	
    /*
     * 
     * ����ϵͳ����
     * 	��־�ļ�
     * 	��ʼʱ��
     * 	hibernate����
     */
	public void destory() throws IOException{
		long nowTime = System.currentTimeMillis();
		print("==========ͳ����Ϣ===============");
		print("��ץȡ "+totalUrls+"�����ӣ�\n" +
				"���г�����ʾ���� "+manageUrls+"����\n" +
				"��Ч������ "+errorUrls+"����\n" +
				"��д�뵽���ݿ�spider�С�" +
				"��ʱ��"+( (nowTime-time)/1000 )+"s\n"+
				"Create By ������");
		print("==========����ץȡ======================================================");
		print("�ѱ��浽Ŀ¼Ϊ "+file.getAbsolutePath());
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
	 * �����޹�����
	 */
	public void filter(String url) throws IOException{
		print("���ڹ��� _"+url);
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
		//print("Ҫ������������£�");
		for (Element link : list){
			str = link.text();
			totalUrls++;
			if (this.isNeed(str)){
				manageUrls++;
				urlList.add(url.substring(0,i)+link.attr("href"));
				//out.append("�ݴ棺"+link.text()+" ="+url.substring(0,i)+link.attr("href"));
				print("�ݴ棺"+link.text()+" ="+url.substring(0,i)+link.attr("href"));
			}
			else  readyList.add(url.substring(0,i)+link.attr("href"));
		}
		print("====================================================\n\n\n");
	}

	/**
	 * ��ȡ�ؼ���Ϣ
	 */
	public void manage(String url) throws IOException{
		
		Response response;
		try {
			response = Jsoup.connect(url)
					   .userAgent("Mozilla")
					   //.timeout(10000)
					   .execute();
		} catch (Exception e) {
			/* ҳ�淵�ش��󣬼���200 */
			print("��ǰURL��"+url+"  ҳ�治����");
			errorUrls++;
			return;
		}
		//print("״̬ ��"+response.statusCode()+" , "+url);
		print("��ǰURL��"+url);
		
		Document doc = response.parse();
		
		//File file = new File(url);
		//Document doc = Jsoup.parse(file, "GBK");
		//Elements list = doc.select("table[width=880]");
		
		/*��ȡҳ����Ϣ*/
		print("��ǰURL��"+url);
		Elements list = doc.select("table.MsoNormalTable tbody tr td");
		if (list==null || list.size() <1){
			print("No manage");
			out.append("No manage");
			return;
		}
		/*��ȡ��������*/
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
		
		
		/*���ݱ����ʽ��ȡ�����Ϣ*/
		//print("%"+list.get(0).text());
		if (list.get(0).text().contains("��")) this.manage1(list, addition, url);
		else if (list.get(0).text().contains("����")) this.manage2(list, addition, url);
		else return;
		/*for (Element e : list){
			//print((i++)+e.text());
		}
		this.manage1(list);
		*/
	}
	
	/**
	 * �����Ŷӱ��
	 * @param list
	 * @param addition
	 * @throws IOException
	 */
	private void manage1(Elements list, String addition, String url) throws IOException{
		String department = this.getReadString(list.get(2));
		String leader 	  = this.getReadString(list.get(4));
		String position   = this.getReadString(list.get(6));
		String days 	  = this.getReadString(list.get(8)).replaceAll("��", "");
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
		String format = String.format("���ڲ���:%s\n ���鳤:%s\n ���ù���/����:%s\n ��������:%s\n " +
				"��������:%s\n ������Ա����:%s\n ����δ����׼�����ӳ�����ͣ��ʱ�䡢�����г̡����ӳ��ù��ң�������:%s\n �Ƿ����г��þ��������������:%s\n �Ƿ��ύ���ñ���:%s\n " +
				"��������Ŀ�ġ�����ʵ���г�:%s\n �������γ�����ȡ�õ�ֱ�ӹ����ɹ�:%s\n �������γ����������Ǳ��Ӱ��ͳ�Ч:%s\n ��һ����ʵ���ú����ɹ����������������ĶԲ߽����������:%s\n " +
				"��������:%s\n ��ע:%s\n", 
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
	 * ������˱��
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
		
		String format = String.format("������Ա����:%s\n ������Ա��λ:%s\n ������Աְ��:%s\n ���ù���/����:%s\n " +
				"������Դ�����ѿ��ţ���Ԥ���ܷ���:%s\n ��������:%s\n �ճ̰���:%s\n ��������:%s\n ���뵥λ:%s\n ", 
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
	 * ��������Ϣ
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
	 * ���浽���ݿ���
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
	 * ����Ƿ��ڵ���Ϣ
	 * @param e
	 * @return
	 */
	private String getReadString(Element e){
		if (e == null) return "��";
		
		String str = e.text();
		//print(str);
		if (str==null || "".equals(str)) return "��";

		if ("??".equals(str)) return "��";
		
		//str = str.replaceAll("?", "");
		
		
/*		int index = str.indexOf("��");
		if (index > -1) str = str.substring(index);
		index = str.indexOf(":");
		if (index > -1) str = str.substring(index);
*/
	
		return this.replace( str.trim() );
		
	}
	
	/**
	 * ��������
	 * @param str
	 * @return
	 */
	private boolean isNeed(String str){
		return str.contains("����") && (str.contains("��ʾ")||str.contains("����")||str.contains("����"));
	}
	
	/**
	 * ��ӡ
	 * @param message
	 * @throws IOException
	 */
	private void print(String message) throws IOException{
		System.out.println(message);
		//log.info(message+"\n");
		out.append(message+"\n");
	}
	
	/**
	 * �淶����
	 * @param str
	 * @return
	 */
	private String replace(String str){
		return str
				.replaceAll("��������Ŀ�ġ�����ʵ���г̣�", "")
				.replaceAll("�������γ�����ȡ�õ�ֱ�ӹ����ɹ���Ҫ�󾡿�������˵������", "")
				.replaceAll("�������γ����������Ǳ��Ӱ��ͳ�Ч����Ա�����������λ�������𡢼ƻ���Ŀ�����ҵ���Ӱ�졢�������ȣ���", "")
				.replaceAll("��һ����ʵ���ú����ɹ����������������ĶԲ߽��飺", "")
				.replaceAll("����������", "")
				//.replaceAll("?", "")
		;
	}
}
