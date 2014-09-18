var cordova = require('cordova'); 

var FresnelSMS = {
	
	/*
	* successCallBack - function to call when successfully executed
	* errorCallBack - function to call when there is an error
	* phoneNumber - number to send the message to
	* message - the message contents you wish to send
	* type - type of message, types = ["PersonAdd", "PersonEdit"]
	*/
	send : function(successCallback, errorCallBack, phoneNumber, message, type){
		cordova.exec(successCallback, errorCallBack, 'FresnelSmsService', 'send',[phoneNumber,message,type]);
	},


	/*
	* successCallBack - function to call when successfully executed
	* errorCallBack - function to call when there is an error
	* type - 2 types of message String	= ["PersonAdd", "PersonEdit"]
	* options - 2 options String     	= ["Clear","NoClear"]
	*	- 'Clear' = clears the messages that the receiver has already received
		- 'NoClear' = does not clear the messages that the receiver has already received
	*/
	getMessages : function(successCallback, errorCallBack,type, options){
		cordova.exec(successCallback, errorCallBack, 'FresnelSmsService', 'getMessages',[type, options]);

	}

	
};

module.exports = FresnelSMS;