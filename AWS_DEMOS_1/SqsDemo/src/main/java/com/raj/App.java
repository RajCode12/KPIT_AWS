package com.raj;

import java.io.File;
// import java.io.FileWriter;
// import java.io.FileWriter;
// import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
/**
 * Hello world!
 *
 */
public class App implements CommandLineRunner
{

	private static final String queueURL = "https://sqs.eu-west-2.amazonaws.com/891377117529/raj-queue";

	private static String bucketName = "raj-assign";
	public void setBucketName(String bucketName) {
		App.bucketName = bucketName;
	}
	
    public static void main( String[] args )
    {
        SqsClient client = SqsClient.builder()
                .region(Region.EU_WEST_2)
                .build();

		String msg = "";
		try {
			SpringApplication.run(App.class, args);
			msg = "File Successfully Uploaded...";
		} catch(Exception e) {
			e.printStackTrace();
		}
		sendMessage(client,msg);
    }

	static String getInputFromUser() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter a text : ");
		String s = sc.nextLine();
		sc.close();
		return s;
	}

	@Override
	public void run(String... args) throws Exception {
		String filepath = null;
		if (args.length < 1) {
            System.out.println("No file path provided as argument");
            filepath = "name.txt";
        } else {
            filepath = args[0];
        }

        String str = getInputFromUser();
        // try {
        //     FileWriter writer = new FileWriter(filepath);
        //     writer.write(str);
        //     writer.close();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
		
		System.out.println("File to be uploaded to bucket : "+filepath);
		S3Client client = S3Client.builder().region(Region.EU_WEST_2).build();
		System.out.println(bucketName);
		File file = new File(filepath);
		PutObjectRequest req = PutObjectRequest.builder()
									.bucket(bucketName)
									.key(file.getName())
									.build();
		
		if(file.isFile()) {
			//client.putObject(req,RequestBody.fromFile(file));
			client.putObject(req,RequestBody.fromString(str));
			System.out.println("File Successfully Uploaded");
		}else {
			System.out.println("Error Occured...");
		}

	}

	static void sendMessage(SqsClient client, String message){
        LocalDateTime now=LocalDateTime.now();
        String key=now.format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));

        Map<String,MessageAttributeValue> attributes=new HashMap<>();
        attributes.put("Object key", MessageAttributeValue.builder().stringValue(key).build());
 
        SendMessageRequest req = SendMessageRequest.builder()
                                            .queueUrl(queueURL)
                                            .messageBody(message+" : "+key)
                                            .delaySeconds(2)
                                            .build();
        client.sendMessage(req);
        System.out.println("Message sent successfully !");        
    }
}
