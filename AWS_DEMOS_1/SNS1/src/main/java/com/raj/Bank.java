package com.raj;

import java.util.InputMismatchException;
import java.util.Scanner;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

/**
 * Hello world!
 *
 */
public class Bank
{
    private static final String topic_arn = "arn:aws:sns:eu-west-2:891377117529:MY_BANK";

    private static double amount = 50000;
    private static int otp = 1000;
    public static void main(String[] args)
    {

        SnsClient client = SnsClient.builder().region(Region.EU_WEST_2).build();

        String message1 = withdraw();
        if(message1.length() > 0) {
            SendMessage(client,message1);
        }
         
        // String message2 = deposit();
        // if(message2.length() > 0) {
        //     SendMessage(client,message1);
        // }

        // String message3 = balance();
        // if(message3.length() > 0) {
        //     SendMessage(client,message3);
        // }
    }

    static void SendMessage(SnsClient client, String msg) {
        System.out.println("Sending an OTP to your registered email address !");

        PublishRequest req = PublishRequest.builder()
                                    .targetArn(topic_arn)
                                    .message(msg)
                                    .build();
        
        client.publish(req);
    }

    static String RecieveMessage(SnsClient client) {
        return "";
    }

    static String changeOTP() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the OTP : ");
        int o = sc.nextInt();
        try {
            if(String.valueOf(o).length() != 4) {
                throw new RuntimeException("Please Enter 4 digit OTP");
            } else {
                otp = o;
            }
        }
        catch(InputMismatchException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return "";
    }

       
    static String deposit() {
        Scanner sc = new Scanner(System.in);
        int am, o;
        try {
            System.out.println("Enter Amount to deposit : ");
            am = sc.nextInt();
            System.out.println("Enter OTP : ");
            o = sc.nextInt();
            if(am < 0 || o != otp) {
                throw new RuntimeException("Please Enter a valid amount...");
            } else {
                amount -= am;
                return "Amount Deposited : " + am;
            }
        } 
        catch(InputMismatchException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return "";
    }
    static String withdraw() {
        Scanner sc = new Scanner(System.in);
        int am, o;
        try {
            System.out.println("Enter Amount to Withdraw : ");
            am = sc.nextInt();
            System.out.println("Enter OTP : ");
            o = sc.nextInt();
            if(am < 0 || am > amount || o != otp) {
                throw new RuntimeException("Not Enough Balance...");
            } else {
                amount -= am;
                return "Amount Withdrawn : " + am;
            }
        } catch(InputMismatchException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    static String balance() {
        return "Balance : " + amount;
    }
}
