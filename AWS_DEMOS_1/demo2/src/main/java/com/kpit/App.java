package com.kpit;
import java.util.*;
// import java.io.*;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final String queueURL = "https://sqs.eu-west-2.amazonaws.com/891377117529/raj-queue";
    public static void main( String[] args )
    {
        SqsClient client = SqsClient.builder()
                .region(Region.EU_WEST_2)
                .build();
        
        // List all Queues 
        String msg = readItemFromUser();
        String msg2 = readItemFromUser();
        sendMessage(client,msg);
        sendMessage(client,msg2);
        System.out.println("Start reading messages ...");
        receiveMessage(client);
    }
    static String readItemFromUser() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter what do you want??");
        System.out.println("Rice Daal/ Biryani / Chapati");
        String s = sc.next();
        System.out.println("Enter the quantity of " + s + " do you want??");
        int n = sc.nextInt();
        System.out.println("Enter Table Number : ");
        int num = sc.nextInt();
        return "Order " + s + " for " + n + " on table " + num;
    }
    static void sendMessage(SqsClient client, String message){
        SendMessageRequest req = SendMessageRequest.builder()
                                            .queueUrl(queueURL)
                                            .messageBody(message)
                                            .delaySeconds(2)
                                            .build();
        client.sendMessage(req);
        System.out.println("Message sent successfully !");
        
    }

    static void receiveMessage(SqsClient client){
        ReceiveMessageRequest req = ReceiveMessageRequest.builder()
                                            .queueUrl(queueURL) 
                                            .maxNumberOfMessages(5)
                                            .waitTimeSeconds(10)
                                            .build();
        
        while(true) {
            ReceiveMessageResponse resp = client.receiveMessage(req);
            List<Message> messages =  resp.messages();
            System.out.println("Found "+messages.size()+" messages");

            for(Message msg : messages){

                System.out.println("Reading the message....");
                System.out.println("  "+msg.body());
                System.out.println("Mark as delivered (Delete from the Queue)");
                DeleteMessageRequest del = DeleteMessageRequest.builder()
                                            .queueUrl(queueURL)
                                            .receiptHandle(msg.receiptHandle())
                                            .build();
                client.deleteMessage(del);
            }

            if(messages.size() < 1) break;
        }
        
    }
}
