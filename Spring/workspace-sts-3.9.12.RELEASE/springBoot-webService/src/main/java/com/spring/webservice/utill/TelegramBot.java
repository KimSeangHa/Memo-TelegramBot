package com.spring.webservice.utill;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {
	
   	@Autowired
	private SqlSession sqlSession;
   	
    private final String BOT_NAME = "Kim_Scheduler_Bot"; //Bot Name
    private final String AUTH_KEY = "1334891991:AAGkrXOd3oPP92tQzRclNY4nmxwuvMPIUqM"; //Bot Auth-Key
    private final String CHAT_ID = "-381510796";
    //private final String CHAT_ID = "-381510796"; //Chat ID 616043197
    
    @Override 
   public void onUpdateReceived(Update update) {
    	
        // TODO
    	System.out.println(update);
    	System.out.println(update.getMessage().getText());
    	
    	String getText = update.getMessage().getText();
    	
    	SendMessage message = new SendMessage();
    	
    	if ( getText.length() != 8 ) {
        	message.setChatId(CHAT_ID);
        	message.setText("��¥�� �߸��Է��Ͽ����ϴ�. \n20200823 ���·� �Է����ּ���.");
        	
    		try {
    			execute(message);
    		} catch (TelegramApiException e) {
    			e.printStackTrace();
    		}
    	} else {
    		String date = getText.substring(0,4) + "-" + getText.substring(4,6) + "-" + getText.substring(6,8) + "%";
    	
    		System.out.println("###" + date);
    		
	    	/* DB Connection */
	    	String sqlHost = "jdbc:mysql://localhost:3306/webservice?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
	    	String user = "root";
	    	String pw = "root";
	    	
	    	try {
	          String myDriver = "com.mysql.cj.jdbc.Driver";
	          Class.forName(myDriver);
	          Connection conn = DriverManager.getConnection(sqlHost, user, pw);
	          
	          String query = "SELECT "
          						+ "REPLACE(CONTENT, \"%0A\", \"\\n\") AS CONTENT "
          						+ ", ID "
          						+ "FROM MEMO "
          						+ "WHERE REGDATE LIKE " + "\"" + date + "\"";
	
	          Statement st = conn.createStatement();
	          
	          ResultSet rs = st.executeQuery(query);
	          
	          System.out.println("@@@@@");
	          System.out.println(query);
	          
	          if(rs.isBeforeFirst()) {
		          while (rs.next()) {
		            String Content = rs.getString("CONTENT");
		            String id = rs.getString("ID");
		            
		            System.out.format(Content, id);
		    		
		            message.setText(Content);
		    		message.setChatId(CHAT_ID);
		    		
		    		try {
		    			execute(message);
		    		} catch (TelegramApiException e) {
		    			e.printStackTrace();
		    		}
		          }
	          } else {
	            message.setText("��ϵ� �������� �����ϴ�. \n�������� ������ּ���.");
	    		message.setChatId(CHAT_ID);
	    		
	    		try {
	    			execute(message);
	    		} catch (TelegramApiException e) {
	    			e.printStackTrace();
	    		}
	          }
	          
	          st.close();
	        } catch (Exception e) {
	          System.err.println("Got an exception! ");
	          System.err.println(e.getMessage());
	        }
    	}
    	

		
    	/*
    	 * SendMessage message = new SendMessage().enableHtml(true);
		message.setText("AAA");
		message.setChatId("aa");
		
		try {
			execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
    	 */
   }

   @Override 
   public String getBotUsername() {
        // TODO 
       return BOT_NAME;
   }

   @Override 
   public String getBotToken() {
        // TODO 
       return  AUTH_KEY;
   }
}