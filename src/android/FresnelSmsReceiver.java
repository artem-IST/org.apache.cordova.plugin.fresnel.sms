package org.apache.cordova.plugin.fresnel.service.sms;

import java.io.Console;
import java.util.ArrayList;
import java.util.Hashtable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;
import android.webkit.ConsoleMessage;

public class FresnelSmsReceiver extends BroadcastReceiver {

	
	//ArrayList that hold the different message types
	private static ArrayList<String> fresnelAddPersonMessages = new ArrayList<String>();
	private static ArrayList<String> fresnelEditPersonMessages = new ArrayList<String>();
	
	//Queue used to hold messages that are in progress
	private static Hashtable<String, FresnelSMS> fresnelSmsQueue = new Hashtable<String,FresnelSMS>();
	
	protected static final String LOG = "SmsReveiver";

	@Override
	public synchronized void onReceive(Context context, Intent intent) {

		SmsMessage[] msgs;
		String smsMessageBody = "";

		Object[] pdus = (Object[]) intent.getExtras().get("pdus");

		msgs = new SmsMessage[pdus.length];
		for (int i = 0; i < msgs.length; i++) {
			msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			smsMessageBody += msgs[i].getMessageBody().toString();
		}

		Log.i(LOG, "Intercepted SMS: " + smsMessageBody);

		
		
		//Check to see if the Message is > 8 characters, Fresnel String has a 9 character delimiter
		if(smsMessageBody.length()>8) {
			
			//Check characters 0-8 to see if it is a Fresnel SMS
			String delimiter = smsMessageBody.subSequence(0,9).toString();
			Log.d(LOG,"DELIMITER: "+delimiter);
			if(delimiter.equals(FresnelSmsBuilder.str_delimiter)) {
				Log.i(LOG,"FrenselSMS found!");
				FresnelSMS inQuestion;
				/**
				 * If we have a message delimiter that matches a FresnelSMS, we have a messageComponent
				 * that belongs to a Fresnel SMS
				 */
				String messageComponent = smsMessageBody;
				String UUID =  messageComponent.subSequence(9,45).toString();
				boolean exists = fresnelSmsQueue.containsKey(UUID);
				
				Log.d(LOG,"UUID: "+UUID);
				Log.d(LOG,"EXISTS: "+exists);
				
				//Seen this Fresnel SMS before, extract from queue and do necessary work to it...
				if(exists) {
					
					inQuestion = fresnelSmsQueue.get(UUID);
					inQuestion.addMessageComponent(messageComponent);
					
				}
				
				//First time seeing this unique Fresnel SMS, add to queue and process
				else{
					inQuestion = new FresnelSMS(messageComponent);
					fresnelSmsQueue.put(UUID, inQuestion);
				}
				
				//If all of the messages were received, add to arraylist of completed messages...
				if(inQuestion.isReceivedAllMessages()) {
					Log.w(LOG, "Received all messageComponents for: "+UUID);
					String addingMessage = inQuestion.getFinalMessage();
					
					switch(inQuestion.getType()) {
						case 'a':
							fresnelAddPersonMessages.add(addingMessage);
							Log.i(LOG,"Added message to 'Add Person' ");
							break;
						case 'e':
							fresnelEditPersonMessages.add(addingMessage);
							Log.i(LOG,"Added message to 'Edit Person' ");
							break;
					}
					
					Log.i(LOG,"Added: "+addingMessage);
				}
				else{
					Log.w(LOG, "Did not receive all messageComponents for: "+UUID);
					return;
				}
					
			}
			
			
			else return; //do nothing, let SMS pass through
		
			
			
		}
		else return; //do nothing, let SMS pass through

	}


	/**
	 * @description Returns a string representation of all of the FresnelSMS messages
	 *              this class has received...
	 * @return String
	 */
	public static String getMessages(char c) {
		
		String str = "";
		
		switch(c) {
			//Getting messages from addPerson
			case 'a':
				for(int i=0;i<fresnelAddPersonMessages.size();i++)
					str += String.format("%s\n", fresnelAddPersonMessages.get(i));
				break;
				
			//Getting messages from editPerson
			case 'e':
				for(int i=0;i<fresnelEditPersonMessages.size();i++)
					str += String.format("%s\n", fresnelEditPersonMessages.get(i));
				break;
		}
	
		return str;

	}

	public static void clearMessages(char t) {

		switch(t){
			//clearing messages for AddPerson
			case 'a':
				fresnelAddPersonMessages.clear();
				break;
				
			//clearing messages for EditPerson
			case 'e':
				fresnelEditPersonMessages.clear();
				break;

		}

	}
	

}