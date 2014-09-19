var cordova = require('cordova'); 

var FresnelSMS = {
	
	/*
	 * Parameters:
	 * successCallBack - function to call when successfully executed
	 * errorCallBack - function to call when there is an error
	 * phoneNumber - number to send the message to
	 * message - the message contents you wish to send
	 * type - type of message, types = ["PersonAdd", "PersonEdit"]
	 *
	 * Description:
	 *	-Use this method in order to send a FresnelSMS message to a specific phone number
	*/
	send : function(successCallback, errorCallBack, phoneNumber, message, type){
		cordova.exec(successCallback, errorCallBack, 'FresnelSmsService', 'send',[phoneNumber,message,type]);
	},


	/* 
	 * Parameters:
	 * successCallBack - function to call when successfully executed
	 * errorCallBack - function to call when there is an error
	 * type - 2 types of message String	= ["PersonAdd", "PersonEdit"]
	 * options - 2 options String     	= ["Clear","NoClear"]
	 *	- 'Clear' = clears the messages that the receiver has already received
	 *	- 'NoClear' = does not clear the messages that the receiver has already received
	 *
	 * Description:
	 *	-Use this method in order to get FrenselSMS messages that might have been intercepted
	 *	 by the application.
	*/
	getMessages : function(successCallback, errorCallBack,type, options){
		cordova.exec(successCallback, errorCallBack, 'FresnelSmsService', 'getMessages',[type, options]);

	},

	/*
	 * Parameters:
	 * bool - boolean value 			= ["true", 'false']
	 *	-Note: make sure that the boolean values are pased as Strings.
	 *
	 * Description: hideNotifications
	 * 	-Use this method in order to hide FrenselSMS message notifications
	 *	 If the app receives a FresnelSMS and hideNotification == true
	 *	 The FresnelReceiver will hide the SMS from the user. The user will
	 *	 not be able to see the SMS in their text messages
	*/
	hideNotifications : function(bool){
		 var success = function(){};
		 var failure = function(){};
		cordova.exec(success,failure,'FresnelSmsService','hideNotifications',[bool]);
	}

	
};

module.exports = FresnelSMS;