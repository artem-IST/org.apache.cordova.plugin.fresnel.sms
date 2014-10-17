//
//  FresnelSmsBuilder.m
//  FresnelSMS
//
//  Created by Artem Klintsevich on 10/10/14.
//  Copyright (c) 2014 Artem Klintsevich. All rights reserved.
//

#import "FresnelSmsBuilder.h"

@implementation FresnelSmsBuilder

static NSString * delimiter = @"#Fresnel#";
static char char_PERSON_ADD = 'a';
static char char_person_EDIT = 'e';

+(NSMutableArray *)createSMS:(int)maxCharsToSend
             StringToConvert:(NSString *)str
                   AddOrEdit:(NSString *)type{
    
    NSMutableArray *toReturn = [[NSMutableArray alloc]init];
    NSUUID *UUID = [[NSUUID alloc]init];
    printf("UUID: %s\n",[[UUID UUIDString]UTF8String]);
    int numCharactersCopied = 0;
    int numOfMessages = 0;
    int copyUntil = [str length];
    char addOrEdit = ' ';
    BOOL convertedAll = false;
    
    if([type isEqual: @"PersonAdd"])
        addOrEdit = char_PERSON_ADD;
    else if([type isEqualToString:@"PersonEdit"])
        addOrEdit = char_person_EDIT;
    /**
     * FRESNEL SMS STANDARD
     * CHAR 0-8 	= str_delimiter
     * CHAR 9-44 	= UUID of Fresnel Message
     * CHAR 45		= # messages expected to break up
     * CHAR 46		= current message # index
     * CHAR 47		= add or edit, add = a edit = e
     * CHAR 48-MAX  = message contents
     */
    
    while(convertedAll == false){
        
        NSMutableString *toCreate = [[NSMutableString alloc]initWithString:delimiter];
        [toCreate appendString: [UUID UUIDString]];
        [toCreate insertString:@" " atIndex:45];
        [toCreate insertString:[NSString stringWithFormat:@"%d",numOfMessages] atIndex:46];
        [toCreate insertString:[NSString stringWithFormat:@"%c",addOrEdit] atIndex:47];
        
        //For each message that we will construct up to maxCharsToSend...
        for(int i=48;(i<maxCharsToSend && numCharactersCopied < copyUntil);i++,numCharactersCopied++){
            NSString *character = [NSString stringWithFormat:@"%c",[str characterAtIndex:numCharactersCopied]];
            [toCreate insertString:character atIndex:i];
        }
        
        //Add message to the array after we finishing constructing it
        [toReturn addObject:toCreate];
       
        //printf("Created Fresnel SMS: %s\n",[toCreate UTF8String]);
        
        
        /*//clear characters in the string for next String SMS
        NSRange rang;
        rang.length=maxCharsToSend-45;
        rang.location = 45;
        [toCreate deleteCharactersInRange:rang];
        */
        
        if(numCharactersCopied == copyUntil)
            convertedAll = true;
        numOfMessages++;
    }
    printf("All Fresnel Messages...\n\n");
    //go back and insert message index into each FresnelSMS
    for(int i=0;i<[toReturn count];i++){
        NSMutableString *str = [toReturn objectAtIndex:i];
        NSRange r;
        r.length = 1;
        r.location = 45;
        
        [str replaceCharactersInRange:r withString:[NSString stringWithFormat:@"%d",numOfMessages]];
        
        printf("%s\n",[[toReturn objectAtIndex:i]UTF8String]);
    }
    
    
   
    return toReturn;
}



@end
