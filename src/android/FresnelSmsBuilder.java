package org.apache.cordova.plugin.fresnel.service.sms;

import java.util.ArrayList;
import java.util.UUID;

import android.util.Log;



//Class used to construct a Fresnel message
public class FresnelSmsBuilder{

	private static final String LOG = "FresnelSmsBuilder";
	private static final char char_PERSON_ADD = 'a';
	private static final char char_PERSON_EDIT= 'e';
	public static final String str_delimiter = "#Fresnel#";

	protected FresnelSmsBuilder(){

	}

	public static ArrayList<String> createSMS(int maxCharsToSend, String toConvert, String type){
		
		ArrayList<String> toReturn = new ArrayList<String>(); 
		ArrayList<StringBuilder> sBuilder = new ArrayList<StringBuilder>();
		UUID messageUUID = UUID.randomUUID();
		int numCharactersCopied=0;
		int copyUntil = toConvert.length();
		boolean converted = false;
		
		//Sets add or edit
		char addOrEdit = ' ';
		if(type.equals("PersonAdd")) addOrEdit = char_PERSON_ADD;
		if(type.equals("PersonEdit")) addOrEdit = char_PERSON_EDIT;
		
		
		//While we have not converted all of the orig message
		/**
		 * FRESNEL SMS STANDARD
		 * CHAR 0-8 	= str_delimiter
		 * CHAR 9-44 	= UUID of Fresnel Message 
		 * CHAR 45		= # messages expected to break up
		 * CHAR 46		= current message # index
		 * CHAR 47		= add or edit, add = a edit = e
		 * CHAR 48-MAX  = message contents
		 */
		for(int i=0;;i++) {
			
			StringBuilder str = new StringBuilder(maxCharsToSend);
			str.insert(0,str_delimiter);
			str.insert(9,messageUUID.toString());
			str.insert(45,' ');
			str.insert(46,i);
			str.insert(47,addOrEdit);
			
			//copying individuals characters 
			for(int j=48; ((j<maxCharsToSend) && (numCharactersCopied < copyUntil));j++, numCharactersCopied++) {
				str.insert(j,toConvert.charAt(numCharactersCopied));
			}
			
			/**
			 * add recently built StringBuilder to sBuilder
			 * Must go back and add the proper int at CHAR 9
			 */
			sBuilder.add(str);
			Log.d(LOG,"Fresnel SMS: "+str.toString());
			//If we have copied over all of the characters that we needed
			if(numCharactersCopied == copyUntil)converted = true;
			//break after we have converted
			if(converted==true)break;
			 
		}
		
		
		
		Log.d(LOG,"Whole Message: \n");
		for(int num=0;num<sBuilder.size();num++) {
			sBuilder.get(num).replace(45,46,Integer.toString(sBuilder.size()));
			String adding = sBuilder.get(num).toString();
			Log.d(LOG,adding);
			toReturn.add(num,adding);
		}
		
		return toReturn;
	}
	


}