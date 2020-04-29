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
        ConcurrentLinkedQueue<Message> networkQueue = new ConcurrentLinkedQueue<Message>();
        ConcurrentLinkedQueue<Message> userQueue = new ConcurrentLinkedQueue<Message>();
        CopyOnWriteArrayList<Message> messages = new CopyOnWriteArrayList<Message>();
        ConcurrentLinkedQueue<Message> modelSendQueue = new ConcurrentLinkedQueue<Message>();

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

        ConcurrentLinkedQueue<Message> networkQueue;

        Scanner input;

        public Receiver(ConcurrentLinkedQueue<Message> networkQueue, Scanner input){
            this.networkQueue = networkQueue;
            this.input = input;

        }

        @Override
        public void run() {
            System.out.println("Receiver is running.");
            while(true){
                StringBuilder message = new StringBuilder();
                while(input.hasNextLine()){
                    String line = input.nextLine();
                    message.append("\n").append(line);
                    if(line.equals("EOM")) {
                        networkQueue.add(new Message(message.toString()));
                        message = new StringBuilder();
                    }
                }
            }
        }
    }

    private static class Sender implements Runnable {

        ConcurrentLinkedQueue<Message> modelSendQueue;
        PrintWriter output;

        public Sender(ConcurrentLinkedQueue<Message> modelSendQueue,
                      PrintWriter output){
            this.modelSendQueue = modelSendQueue;
            this.output = output;
        }

        @Override
        public void run() {
            while(true){
                while(!modelSendQueue.isEmpty()){
                    Message message = modelSendQueue.remove();
                    output.println(message.toString());
                }
            }
        }
    }

    private static class ConsoleModel implements Runnable {
        ConcurrentLinkedQueue<Message> networkQueue;
        ConcurrentLinkedQueue<Message> userQueue;

        ConcurrentLinkedQueue<Message> modelSendQueue;

        CopyOnWriteArrayList<Message> messages;


        public ConsoleModel(ConcurrentLinkedQueue<Message> networkQueue,
                            ConcurrentLinkedQueue<Message> userQueue,
                            CopyOnWriteArrayList<Message> messages,
                            ConcurrentLinkedQueue<Message> modelSendQueue){
            this.networkQueue = networkQueue;
            this.userQueue = userQueue;
            this.messages = messages;
            this.modelSendQueue = modelSendQueue;
        }

        @Override
        public void run() {
            while(true){
                while(!networkQueue.isEmpty()){
                    Message message = networkQueue.remove();
                    messages.add(message);
                }
                while(!userQueue.isEmpty()){
                    Message message = userQueue.remove();
                    modelSendQueue.add(message);
                    messages.add(message);
                }
            }
        }
    }

}
