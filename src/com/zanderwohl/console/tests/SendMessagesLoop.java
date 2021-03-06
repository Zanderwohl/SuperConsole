package com.zanderwohl.console.tests;

import com.zanderwohl.console.SuperConsole;
import com.zanderwohl.console.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Instant;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SendMessagesLoop {

    private static Socket socket;
    private static Scanner input;
    private static PrintWriter output;

    public static void main(String[] args){
        boolean loopAgain = true;

        try {
            socket = new Socket("localhost", SuperConsole.PORT);
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);

            try{
                while (loopAgain){
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("Sending packet.");
                    output.println(randomSeverityBlankMessage());
                    System.out.println(input.nextLine());
                }
            } catch(InterruptedException e){
                System.out.println("Finished looping due to InterruptedException.");
            }
            System.out.println("Finished looping.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String blankMessage(){
        Message blankMessage = new Message("time=" + Instant.now().getEpochSecond());
        return blankMessage.toString();
    }

    public static String multilineMessage(){
        Message message = new Message("time=" + Instant.now().getEpochSecond() + "\nmessage=This is a multiline message.\\nLine 2\\nLine 3 which is a little bit longer than the last.\\nLine 4");
        return message.toString();
    }

    public static String randomSeverityBlankMessage(){
        Random r = new Random();
        int length = Message.severities.values().length;
        int index = r.nextInt(length);
        String choice = Message.severities.values()[index].toString();
        Message blankMessage = new Message("time=" + Instant.now().getEpochSecond() + "\nseverity=" + choice);
        return blankMessage.toString();
    }

}
