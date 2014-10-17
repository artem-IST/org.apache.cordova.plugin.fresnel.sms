//
//  FresnelSmsService.h
//  fres
//
//  Created by Artem Klintsevich on 10/14/14.
//  Copyright (c) 2014 Artem Klintsevich. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Cordova/CDVPlugin.h>
#import <MessageUI/MFMessageComposeViewController.h>

@interface FresnelSmsService : CDVPlugin <MFMessageComposeViewControllerDelegate>

-(void)send:(CDVInvokedUrlCommand*)command;



@end
