package com.zanderwohl.console;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {

    public static final int PORT = 288;

    public static void main(String[] args) throws IOException {
        ConcurrentLinkedQueue<String> networkQueue = new ConcurrentLinkedQueue<String>();
        ConcurrentLinkedQueue<String> userQueue = new ConcurrentLinkedQueue<String>();
        CopyOnWriteArrayList<Message> messages = new CopyOnWriteArrayList<Message>();
        ConcurrentLinkedQueue<String> modelSendQueue = new ConcurrentLinkedQueue<>();

        Socket socket = new Socket("localhost", Main.PORT);
        Scanner input = new Scanner(socket.getInputStream());
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

        Thread sender = new Thread(new Sender(modelSendQueue, output));
        Thread receiver = new Thread(new Receiver(networkQueue, input));
        Thread consoleModel = new Thread(new ConsoleModel(networkQueue, userQueue, messages, modelSendQueue));
        Thread view = new Thread(new WindowView(messages, userQueue));

        sender.start();
        receiver.start();
        consoleModel.start();
        view.start();
    }

    public static void close(){
        System.exit(0);
    }

    private static class Receiver implements Runnable {

        ConcurrentLinkedQueue<String> networkQueue;

        private static Scanner input;

        public Receiver(ConcurrentLinkedQueue<String> networkQueue, Scanner input){
            this.networkQueue = networkQueue;
            this.input = input;

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

    private static class Sender implements Runnable {

        ConcurrentLinkedQueue<String> modelSendQueue;
        PrintWriter output;

        public Sender(ConcurrentLinkedQueue<String> modelSendQueue,
                      PrintWriter output){
            this.modelSendQueue = modelSendQueue;
            this.output = output;
        }

        @Override
        public void run() {
            while(true){
                //System.out.println(modelSendQueue.size());
                while(!modelSendQueue.isEmpty()){
                    String message = modelSendQueue.remove();
                    output.println(message);
                }
            }
        }
    }

    private static class ConsoleModel implements Runnable {
        ConcurrentLinkedQueue<String> networkQueue;
        ConcurrentLinkedQueue<String> userQueue;

        ConcurrentLinkedQueue<String> modelSendQueue;

        CopyOnWriteArrayList<Message> messages;


        public ConsoleModel(ConcurrentLinkedQueue<String> networkQueue,
                            ConcurrentLinkedQueue<String> userQueue,
                            CopyOnWriteArrayList<Message> messages,
                            ConcurrentLinkedQueue<String> modelSendQueue){
            this.networkQueue = networkQueue;
            this.userQueue = userQueue;
            this.messages = messages;
            this.modelSendQueue = modelSendQueue;
        }

        @Override
        public void run() {
            while(true){
                while(!networkQueue.isEmpty()){
                    String message = networkQueue.remove();
                    Message m = new Message(message);
                    messages.add(m);
                }
                while(!userQueue.isEmpty()){
                    String message = userQueue.remove();

                    System.out.print("User input:\n\t" + message);
                    Message m = new Message(message);
                    modelSendQueue.add(message);
                    messages.add(m);
                }
            }
        }
    }

}
