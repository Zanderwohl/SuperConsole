package com.zanderwohl.console;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {

    public static final int PORT = 288;

    public static void main(String[] args) {
        ConcurrentLinkedQueue<String> networkQueue = new ConcurrentLinkedQueue<String>();
        ConcurrentLinkedQueue<String> userQueue = new ConcurrentLinkedQueue<String>();

        Thread connector = new Thread(new Connector(networkQueue, userQueue));
        Thread consoleModel = new Thread(new ConsoleModel(networkQueue, userQueue));
        connector.start();
        consoleModel.start();
    }

    private static class Connector implements Runnable {

        ConcurrentLinkedQueue<String> networkQueue;
        ConcurrentLinkedQueue<String> userQueue;

        private static Socket socket;
        private static Scanner input;
        private static PrintWriter output;

        public Connector(ConcurrentLinkedQueue<String> networkQueue, ConcurrentLinkedQueue<String> userQueue){
            this.networkQueue = networkQueue;
            this.userQueue = userQueue;
            try {
                socket = new Socket("localhost", Main.PORT);
                input = new Scanner(socket.getInputStream());
                output = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                System.err.println("Socket in Receiver could not be created!");
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            System.out.println("Receiver is running.");
            while(true){
                String message = "";
                while(input.hasNextLine()){
                    String line = input.nextLine();
                    message += "\n" + line;
                    if(line.equals("EOM")) {
                        networkQueue.add(message);
                        message = "";
                    }
                }
            }
        }
    }

    private static class ConsoleModel implements Runnable {
        ConcurrentLinkedQueue<String> networkQueue;
        ConcurrentLinkedQueue<String> userQueue;

        ArrayList<Message> messages;

        public ConsoleModel(ConcurrentLinkedQueue<String> networkQueue,
                            ConcurrentLinkedQueue<String> userQueue){
            this.networkQueue = networkQueue;
            this.userQueue = userQueue;
            messages = new ArrayList<>();
        }

        @Override
        public void run() {
            while(true){
                while(!networkQueue.isEmpty()){
                    String message = networkQueue.remove();
                    Message m = new Message(message);
                    messages.add(m);
                    System.out.println(messages.size() + ":::" + m.toString(true));
                }
            }
        }
    }
}
