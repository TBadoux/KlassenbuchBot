package klassenbuchbot;

/*
 * @author Thibault Badoux
 */

import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.MessageReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.TextMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.chat.message.ForceReply;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;

import java.util.ArrayList;



public class TelegramListener implements Listener {
	
	private final TelegramBot telegramBot;
	private int step = 1;
	private String cmd;
	private String date;
	private String type;
	private String subject;
	private String art;
	
	private NewEntry newEntry;
	
    public TelegramListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
        newEntry = new NewEntry(telegramBot);
    }
    
    
    @Override
    public void onCommandMessageReceived(CommandMessageReceivedEvent event){
    	
    	
    	cmd = event.getCommand();
    	System.out.println(cmd);
    	
    	switch(cmd){
    		case "homework":
    			
    			newHomework(event);
    			
    		break;
    		
    		case "exam":
    			
    			newExam(event);
    			
    		break;
    		
    		case "check":
    			
    			newCheck(event);
    			
    		break;	
    	}
    }
    
    
    @Override
    public void onTextMessageReceived(TextMessageReceivedEvent event){
    	
    	System.out.println(event.getContent().getContent());
    	
    	switch(step){
    		case 1:
    		
    			date = newEntry.addDate(event);
    			if (date != null){
    				step = 2;
    			}
    		
    		break;	
    	
    		case 2:
    			
    			subject = newEntry.addSubject(event);
    			step = 3;
    		
    		break;
    	
    		case 3:
    		
    			newEntry.addDefinition(event, type, date, subject);
    			step = 1;
    			
    		break;	
    	}
    }
    
    public void newHomework(MessageReceivedEvent event){
    	
    	SendableTextMessage sendableMessage = SendableTextMessage.builder()
    			.message(Messages.msgHomework)
    			.replyTo(event.getMessage())
    			.parseMode(ParseMode.MARKDOWN)
    			.replyMarkup(new ForceReply())
    			.build();
    	
    	event.getChat().sendMessage(sendableMessage, telegramBot);
    	
    	type = "homework";
    	
    }
    
    public void newExam(MessageReceivedEvent event){
    	
    	SendableTextMessage sendableMessage = SendableTextMessage.builder()
    			.message(Messages.msgExam)
    			.replyTo(event.getMessage())
    			.parseMode(ParseMode.MARKDOWN)
    			.replyMarkup(new ForceReply())
    			.build();
    	
    	event.getChat().sendMessage(sendableMessage, telegramBot);
    	
    	type = "exam";
    }
    
    public void newCheck(CommandMessageReceivedEvent event){
    	
    	int c = 0;
    	
    	date = event.getContent().getContent().toString();
    	date = date.replace("/check ", "");
    	
    	if(newEntry.checkDate(date)){
    	
    		ArrayList<Entry> e = JsonHandler.JSONRead(date);
		
    		for(Entry ent : e){
    			
    			
    			if(ent.type.equals("exam")){
    				art = "Prüfung";
    			}
    			else{ art ="Hausaufgabe";}
    			
    			SendableTextMessage sendableMessage2 = SendableTextMessage.builder()
    	    			.message(date + " :\n" + "Typ: " + art + "\nFach: " + ent.subject + "\nDefinition: " + ent.definition)
    	    			.replyTo(event.getMessage())
    	    			.parseMode(ParseMode.MARKDOWN)
    	    			.build();
    	    	
    	    	event.getChat().sendMessage(sendableMessage2, telegramBot);
    	    	
    	    	c++;
    		}
    	}
    	else{
    		
    		SendableTextMessage sendableMessage = SendableTextMessage.builder()
	    			.message(Messages.msgCheck_Exception)
	    			.replyTo(event.getMessage())
	    			.parseMode(ParseMode.MARKDOWN)
	    			.build();
	    	
	    	event.getChat().sendMessage(sendableMessage, telegramBot);
    	}
    	if(c == 0){
    		
    		SendableTextMessage sendableMessage = SendableTextMessage.builder()
	    			.message(Messages.msgCheck_noEntry)
	    			.replyTo(event.getMessage())
	    			.parseMode(ParseMode.MARKDOWN)
	    			.build();
	    	
	    	event.getChat().sendMessage(sendableMessage, telegramBot);
    	}
    }
}