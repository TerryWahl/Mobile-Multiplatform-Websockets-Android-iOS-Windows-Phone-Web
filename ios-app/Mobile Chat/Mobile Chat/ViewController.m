//
//  ViewController.m
//  Mobile Chat
/*
 Mobile Chat (ff-mobile-chat) is a cross platform
 (Android, iOS, Windows Phone 8, web) chat supported by NodeJS websockets.
 <https://github.com/TerryWahl/ff-mobile-chat>
 
 Copyright (C) 2013-2014 Terry Wahl & Marco Jacobs
 
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU Affero General Public License for more details.
 
 You should have received a copy of the GNU Affero General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

#import "ViewController.h"

@interface ViewController ()

@property (nonatomic, readwrite) SRWebSocket *sockjsSocket;
@property (nonatomic, readwrite) BOOL socketReady;

@end

@implementation ViewController

@synthesize tvStatus;
@synthesize tfOutput;
@synthesize tfInput, tfName;

-(void)viewWillAppear:(BOOL)animated
{
    //Startup websockets
    self.socketReady = NO;
    //This library requires a raw websocket url. It does accept http instead of ws.
    //For NodeJS with SockJS this should be "http://YOUR.URL:PORT/IDENTIFIER/websocket"
    self.sockjsSocket = [[SRWebSocket alloc] initWithURL:[[NSURL alloc] initWithString:@"http://your.site:6975/mobilechat/websocket"]];
    self.sockjsSocket.delegate = self;
    [self.sockjsSocket open];

}

- (void)viewDidLoad
{
    [super viewDidLoad];
    //add delegates to remove keyboard on pressing enter
    self.tfInput.delegate = self;
    self.tfName.delegate = self;
	// Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)btnSend:(id)sender {
    //Check if name field is not empty
    if([tfName.text isEqualToString:@""]){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Name missing" message:@"Fill in a name to chat"
                                                       delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show]; 
    }
    //check if message field is not empty
    else if([tfInput.text isEqualToString:@""]){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Message missing" message:@"Message can't be empty"
                                                       delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
    }
    //Everything is ok, send the message!
    else{
        if(self.socketReady){
            NSString *input = [tfInput text];
            tfInput.text = @"";
             
            NSString *addName = [NSString stringWithFormat:@"%@%@", tfName.text, @": "];
            NSString *combined = [NSString stringWithFormat:@"%@%@", addName, input];
            
            [self.sockjsSocket send:combined];
        }
    }
}

// SRWebSocket handlers
- (void)webSocket:(SRWebSocket *)webSocket didReceiveMessage:(id)message
{
    NSString *output = [tfOutput text];
    
    //Set the textview on bottom focus when it becomes scrollable
    if([tfOutput.text hasSuffix:@"\n"])
    {
        if (tfOutput.contentSize.height - tfOutput.bounds.size.height > -30)
        {
            double delayInSeconds = 0.2;
            dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, (int64_t)(delayInSeconds * NSEC_PER_SEC));
            dispatch_after(popTime, dispatch_get_main_queue(), ^(void)
                           {
                               CGPoint bottomOffset = CGPointMake(0, tfOutput.contentSize.height - tfOutput.bounds.size.height);
                               [tfOutput setContentOffset:bottomOffset animated:YES];
                           });
        }
    }
    
   //Combine old messages with new messages
    NSString *combined = [NSString stringWithFormat:@"%@%@", message, @"\n"];
    NSString *newOutput = [NSString stringWithFormat:@"%@%@", output, combined];
    
    tfOutput.text = newOutput;

}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return NO;
}

- (void) viewWillDisappear:(BOOL)animated
{
    [self.sockjsSocket close];
    self.sockjsSocket = nil;
}

- (void)webSocketDidOpen:(SRWebSocket *)webSocket
{
    self.socketReady = YES;
    tvStatus.text = @"Online";
}

- (void)webSocket:(SRWebSocket *)webSocket didCloseWithCode:(NSInteger)code reason:(NSString *)reason wasClean:(BOOL)wasClean
{
    self.socketReady = NO;
    tvStatus.text = @"Offline";
}
@end
