package org.apache.cordova.plugin.fresnel.service.sms;

/**
 * FRESNEL SMS STANDARD
 * CHAR 0-8 	= str_delimiter
 * CHAR 9-44 	= UUID of Fresnel Message 
 * CHAR 45		= # messages expected to break up
 * CHAR 46		= current message # index
 * CHAR 47		= add or edit, add = a edit = e
 * CHAR 48-MAX  = message contents
 */

public class FresnelSMS {

	public static final int maxNumOfMessageComponents = 10; 
	private String[] messageComponents = new String[maxNumOfMessageComponents];
	private String UUID;
	private char type; //type can be 'a' == 'personAdd' or 'e' == 'personEdit'
	private int numMessagesExpected;
	private int numMessagesReceived = 0;
	private boolean receivedAllMessages = false;
	
	/**
	 * @description - 
	 * @param messageComponent - Unedited SMS that adheres to the 'FRESNEL SMS STANDARD'
	 */
	FresnelSMS(String messageComponent){
		//Populate object values from message component that is passed into the FresnelSMS object
		this.UUID = messageComponent.substring(9,45);
		this.numMessagesExpected = Integer.valueOf(Character.toString(messageComponent.charAt(45)));
		addMessageComponent(messageComponent);
		this.type = messageComponent.charAt(47);
	}
	
	/**
	 * @description - Use this method to add a received message component to this FresnelSMS object.\nSimply pass
	 * the received SMS, which is formatted to the 'FRESNEL SMS STANDARD', into this method and all of the microtasking 
	 * will be handled inside the class
	 * @param messageComponent - Message component that you would like to add to the FresnelSMS object
	 */
	public void addMessageComponent(String messageComponent) {
		int index = Integer.valueOf(Character.toString(messageComponent.charAt(46)));
		messageComponents[index] = getMessageContent(messageComponent);
		numMessagesReceived++;
		receivedAllMessages = isReceivedAllMessages();
	}
	
	/**
	 * 
	 * @return A String representing the Fresnel data that was sent via
	 * the GSN network over SMS. WARNING, if all of the expected components were not received,
	 * this method will return NULL. You're code should check for null values. 
	 */
	public String getFinalMessage() {
		String str = null;
		if(receivedAllMessages == true) {
			//construct message from components
			str = "";
			for(int i=0;i<numMessagesExpected;i++)str+=messageComponents[i];
			return str;
		}
		else return null;
	}
	
	/**
	 * @description - This is a helper method that checks to see whether this FresnelSMS
	 * object has received all of its expected components
	 * @return boolean 
	 */
	public boolean isReceivedAllMessages() {
		return numMessagesExpected == numMessagesReceived;
	}
	
	/**
	 * @description - 
	 * @param messageComponent - Unedited SMS message which adheres to the FRESNEL SMS STANDARD
	 * @return String
	 */
	private String getMessageContent(String messageComponent) {
		String str = messageComponent.substring(48,messageComponent.length());
		return str;
	}
	
	public char getType() {
		return this.type;
	}

}
