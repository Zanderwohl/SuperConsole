package com.zanderwohl.console;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {

    public static final int PORT = 288;

    public static void main(String[] args) {
        ConcurrentLinkedQueue<String> networkQueue = new ConcurrentLinkedQueue<String>();
        ConcurrentLinkedQueue<String> userQueue = new ConcurrentLinkedQueue<String>();
        CopyOnWriteArrayList<Message> messages = new CopyOnWriteArrayList<Message>();

        Thread connector = new Thread(new Connector(networkQueue, userQueue));
        Thread consoleModel = new Thread(new ConsoleModel(networkQueue, userQueue, messages));
        Thread view = new Thread(new WindowView(messages, userQueue));

        connector.start();
        consoleModel.start();
        view.start();
    }

    public static void close(){
        System.exit(0);
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
                String ms = "Socket in Receiver could not be created!";
                Message error = new Message("source=Connector\nseverity=CONSOLE\ncategory=Network\nmessage=" + ms);
                String st = e.toString().replace("\n","->");
                Message trace = new Message("source=Connector\nseverity=CONSOLE\ncategory=Network\nmessage=" + st);
                networkQueue.add(error.toString());
                networkQueue.add(trace.toString());
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

               while(userQueue.size() > 0){
                    System.out.println(userQueue.size());
                    String userInput = userQueue.remove();
                    Message userMessage = new Message("severity=USER\nsource=Console\ncontent=" + userInput);
                    output.write(userMessage.toString());
                }
            }
        }
    }

    private static class ConsoleModel implements Runnable {
        ConcurrentLinkedQueue<String> networkQueue;
        ConcurrentLinkedQueue<String> userQueue;

        CopyOnWriteArrayList<Message> messages;


        public ConsoleModel(ConcurrentLinkedQueue<String> networkQueue,
                            ConcurrentLinkedQueue<String> userQueue,
                            CopyOnWriteArrayList<Message> messages){
            this.networkQueue = networkQueue;
            this.userQueue = userQueue;
            this.messages = messages;
        }

        @Override
        public void run() {
            while(true){
                while(!networkQueue.isEmpty()){
                    String message = networkQueue.remove();
                    Message m = new Message(message);
                    messages.add(m);
                }
            }
        }
    }
}
