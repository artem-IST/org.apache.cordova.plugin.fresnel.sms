//
//  FresnelSmsBuilder.h
//  FresnelSMS
//
//  Created by Artem Klintsevich on 10/10/14.
//  Copyright (c) 2014 Artem Klintsevich. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface FresnelSmsBuilder : NSObject

+(NSMutableArray*)createSMS:(int)maxCharsToSend
                  StringToConvert:(NSString*)str
                       AddOrEdit:(NSString*)type;

@end
