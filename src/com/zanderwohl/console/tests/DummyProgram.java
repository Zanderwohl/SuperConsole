package com.zanderwohl.console.tests;

import com.zanderwohl.console.Main;
import com.zanderwohl.console.Message;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DummyProgram {

    public static void main(String[] args) throws Exception {
        try (var listener = new ServerSocket(Main.PORT)){
            System.out.println("Dummy Program is running.");
            ConcurrentLinkedQueue<String> loopbackQueue = new ConcurrentLinkedQueue<>();
            Socket socket = listener.accept();
            Thread send = new Thread(new Send(new PrintWriter(socket.getOutputStream(), true), loopbackQueue));
            Thread receive = new Thread(new Receive(new Scanner(socket.getInputStream()), loopbackQueue));
            send.start();
            receive.start();
        }
    }


    private static class Send implements Runnable {

        private PrintWriter output;
        ConcurrentLinkedQueue<String> queue;

        public Send(PrintWriter output, ConcurrentLinkedQueue loopbackQueue){
            this.output = output;
            queue = loopbackQueue;
        }

        @Override
        public void run(){
            try {
                while(true){
                    //TimeUnit.SECONDS.sleep(1);
                    //System.out.println("Sending packet.");
                    //Message m = new Message(SendMessagesLoop.blankMessage());
                    //output.println(m.toString());
                    //System.out.println(m + "\n");
                    while(!queue.isEmpty()){
                        output.println(queue.remove());
                    }
                }
            } catch (Exception e){
                System.out.println("Error:\n" + e);
            }
        }
    }

    private static class Receive implements Runnable {

        Scanner input;
        ConcurrentLinkedQueue queue;

        public Receive(Scanner input, ConcurrentLinkedQueue loopbackQueue){
            this.input = input;
            queue = loopbackQueue;
        }

        @Override
        public void run() {
            String userMessage = "";
            while(input.hasNextLine()){
                String line = input.nextLine();
                if(line.equals("EOM")) {
                    Message m = new Message(userMessage);
                    System.out.println("Got user input:\n\t" + userMessage);
                    System.out.println(m.toString());
                    String returnMessage = "Dummy got the input: " + m.getAttribute("message");
                    queue.add((new Message("source=Dummy\nmessage=" + returnMessage)).toString());
                    userMessage = "";
                } else {
                    userMessage += line + "\n";
                }
            }
        }
    }
}