package klassenbuchbot;

import java.util.ArrayList;

import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.message.ForceReply;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.TextMessageReceivedEvent;

public class NewEntry{
	
	private TelegramBot bot;

	private ArrayList<Entry> entries;
	
	
	public NewEntry(TelegramBot bot){
		this.bot = bot;
		entries = new ArrayList<Entry>();
	}
	
	public String addDate(TextMessageReceivedEvent event){
		
		String date = event.getContent().getContent();
		
		if(checkDate(date)){
		
			SendableTextMessage sendableMessage = SendableTextMessage.builder()
					.message(Messages.msgDate)
					.replyTo(event.getMessage())
					.parseMode(ParseMode.MARKDOWN)
					.replyMarkup(new ForceReply())
					.build();
    	
			event.getChat().sendMessage(sendableMessage, bot);
		
			return date;
		}
		else{
			
			SendableTextMessage sendableMessage = SendableTextMessage.builder()
					.message(Messages.msgDate_Exception)
					.replyTo(event.getMessage())
					.parseMode(ParseMode.MARKDOWN)
					.replyMarkup(new ForceReply())
					.build();
    	
			event.getChat().sendMessage(sendableMessage, bot);
			
			date = null;
			return date;
		}
	}
	
	public String addSubject(TextMessageReceivedEvent event){
		
		String subject = event.getContent().getContent();
		
		SendableTextMessage sendableMessage = SendableTextMessage.builder()
    			.message(Messages.msgSubject)
    			.replyTo(event.getMessage())
    			.parseMode(ParseMode.MARKDOWN)
    			.replyMarkup(new ForceReply())
    			.build();
    	
    	event.getChat().sendMessage(sendableMessage, bot);
		
		return subject;
		
	}
	
	public void addDefinition(TextMessageReceivedEvent event, String type, String date, String subject){
		
		String definition = event.getContent().getContent();
		
		String art;
		
		if(type.equals("exam")){
			art = "Prüfung";
		}
		else{ art ="Hausaufgabe";}
		
		String Msg = art + "am " + date + ": \n Fach: " + subject + "\n Definition: " + definition; 	
		SendableTextMessage sendableMessage = SendableTextMessage.builder()
				.message(Msg)
				.replyTo(event.getMessage())
				.parseMode(ParseMode.MARKDOWN)
					.build();
			
			event.getChat().sendMessage(sendableMessage, bot);
			
			Entry e = new Entry();
			
			e.type = type;
			e.date = date;
			e.subject = subject;
			e.definition = definition;
			
			System.out.println(e.type + e.date + e.subject + e.definition);
			
			entries.add(e);
			
			JsonHandler.JSONWrite(entries);
		
	}
	
	public boolean checkDate(String date){
		
		if(date.matches("^[0-3][0-9]/[0-1][0-9]/(?:[0-9][0-9])?[0-9][0-9]$")){
			
			return true;
		}
		
		else{
			return false;
		}
	}
}