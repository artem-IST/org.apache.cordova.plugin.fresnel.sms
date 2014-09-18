package org.apache.cordova.plugin.fresnel.service.sms;

import java.util.ArrayList;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.telephony.SmsManager;
import android.util.Log;

public class FresnelSmsService extends CordovaPlugin {

	private static final String LOG = "FresnelSmsService";

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

		Log.i(LOG,"Action:"+action);
		if (action.equals("send")) {
			try {
				send(args);
				return true;
			}

			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
	
		/**
		 * action = getMessages()
		 * args[0] = type    --> type of message you wish to get
		 * 		'PersonAdd'  --> wish to get Add messages
		 * 		'PersonEdit' --> wish to get Edit messages
		 *						
		 * args[1] = options --> options that you wish to implement
		 *		'Clear' 	 --> clears the messages that were already received
 		 *		'NoClear'	 --> does not clear the messages that were already received
		 */	
		if(action.equals("getMessages")) {
			
			String type = args.getString(0);

			char c = 0;
			if(type.equals("PersonAdd"))c = 'a';
			if(type.equals("PersonEdit"))c = 'e';
		
	
			String str = getMessages(c);

			if(str.equals("")) {
				callbackContext.error("No messages received");
				return false;
			}
			
			else if(str == null) {
				callbackContext.error("something went wrong with getMessages()");
				return false; 
			}
			else {
				callbackContext.success(str);
				//if we want to clear, clear after we have gotten the message
				if(args.getString(1).equals("Clear")){
					FresnelSmsReceiver.clearMessages(c);
				}
				
				return true;
			}
			
			
			
		}

		return false;

	}

	private void send(JSONArray args) throws JSONException {
		/**
		 * action = send()
		 * args[0] = phone number you wish to call
		 * args[1] = message contents to convert
		 * args[2] = message type 'personAdd' || 'personEdit'
		 */

		Log.i(LOG,"send() called...");
		
		SmsManager sms = SmsManager.getDefault();
		String phoneNumber = args.getString(0);
		String unformattedMessage = args.getString(1);
		String type = args.getString(2);

		ArrayList<String> message = FresnelSmsBuilder.createSMS(155,unformattedMessage,type);
		

		Log.v(LOG, "SENDING MESSAGE: " + message.toString() + "\tTO: " + phoneNumber);

		for(int i=0;i<message.size();i++) {
			sms.sendTextMessage(phoneNumber, null, message.get(i), null, null);
		}

	}
	
	
	private String getMessages(char c) {
		Log.i(LOG,"getMessages() called...");
		if(c!=0) return FresnelSmsReceiver.getMessages(c);
		else return null ;
	}

}