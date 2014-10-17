//
//  FresnelSmsService.m
//  fres
//
//  Created by Artem Klintsevich on 10/14/14.
//  Copyright (c) 2014 Artem Klintsevich. All rights reserved.
//

#import "FresnelSmsService.h"
#import "FresnelSmsBuilder.h"
#import "FresnelSmsViewController.h"

@interface FresnelSmsService()

-(void)showNextMessagePart;

@end

@implementation FresnelSmsService

static NSUInteger numOfMessages = 0;
static int numMessagesShown = 0;
static NSMutableArray *arr = nil;

-(void)send:(CDVInvokedUrlCommand*)command{
    CDVPluginResult* pluginResult = nil;
    NSString* method = [command methodName];
    //we are being asked to send a message...
    if([method isEqualToString:@"send"]){
        
        NSString *phoneNumber = [[command arguments]objectAtIndex:0];
        NSString *toConvert = [[command arguments]objectAtIndex:1];
        NSString *type = [[command arguments]objectAtIndex:2];
        [self.commandDelegate runInBackground:^(void){
            NSMutableArray *r = [FresnelSmsBuilder createSMS:155 StringToConvert:toConvert AddOrEdit:@"PersonAdd"];
            arr = r;
            numOfMessages = [r count];
            [self showNextMessagePart];
        }];
        
        /*FresnelSmsViewController *controller = [[FresnelSmsViewController alloc]initWith:toConvert addOrEdit:type viewController:[self viewController]];
        [controller sendMessage];
         */
        NSLog(@"Message sent...");
    }
    
}

-(void)showNextMessagePart{
    
    MFMessageComposeViewController *controller = [[MFMessageComposeViewController alloc]init];
    [controller setBody:[arr objectAtIndex:numMessagesShown]];
    [controller setMessageComposeDelegate:self];
    [controller setRecipients:[NSArray arrayWithObjects:@"540-429-7491", nil]];
    numMessagesShown++;
    [[self viewController]presentViewController:controller animated:YES completion:^(void){}];
        
}

-(void)messageComposeViewController:(MFMessageComposeViewController *)controller
                didFinishWithResult:(MessageComposeResult)result
{
    
    if (result == MessageComposeResultCancelled){
        NSLog(@"Message cancelled");
    }
    
    else if (result == MessageComposeResultSent){
        NSLog(@"Message sent");
    }
    
    else{
        NSLog(@"Message failed");
    }
    
    [controller dismissViewControllerAnimated:YES completion:^{
        if(numMessagesShown < numOfMessages)[self showNextMessagePart];
        
        else numMessagesShown= 0;
    }];
    
}
    
@end
    
