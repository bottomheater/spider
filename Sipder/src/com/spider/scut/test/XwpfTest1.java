package com.spider.scut.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.Test;

public class XwpfTest1 {  

	static String filename = "C:\\Users\\Administrator\\Desktop\\sample\\"+"b8b13f8e-1343-4e90-9cd3-e6138fddd172.docx";
	
   /** 
    * ͨ��XWPFWordExtractor����XWPFDocument������ 
    * @throws Exception 
    */  
   @Test  
   public void testReadByExtractor() throws Exception {  
      InputStream is = new FileInputStream(filename);  
      XWPFDocument doc = new XWPFDocument(is);  
      XWPFWordExtractor extractor = new XWPFWordExtractor(doc);  
      String text = extractor.getText();  
      System.out.println(text);  
      CoreProperties coreProps = extractor.getCoreProperties();  
      this.printCoreProperties(coreProps);  
      this.close(is);  
   }  
    
   /** 
    * ���CoreProperties��Ϣ 
    * @param coreProps 
    */  
   private void printCoreProperties(CoreProperties coreProps) {  
      System.out.println(coreProps.getCategory());   //����  
      System.out.println(coreProps.getCreator()); //������  
      System.out.println(coreProps.getCreated()); //����ʱ��  
      System.out.println(coreProps.getTitle());   //����  
   }  
    
   /** 
    * �ر������� 
    * @param is 
    */  
   private void close(InputStream is) {  
      if (is != null) {  
         try {  
            is.close();  
         } catch (IOException e) {  
            e.printStackTrace();  
         }  
      }  
   }  
    
}  