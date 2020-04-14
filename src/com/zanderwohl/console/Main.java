package com.zanderwohl.console;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;

public class Main {

    public static final int PORT = 288;
    public static final int MAX_THREADS = 20;

    public static void main(String[] args) {
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();

        Thread receiver = new Thread(new Receiver(queue));
        Thread consoleModel = new Thread(new ConsoleModel(queue));
        receiver.start();
        consoleModel.start();
    }

    private static class Receiver implements Runnable {

        ConcurrentLinkedQueue<String> queue;

        private static ServerSocket socket;
        private static Scanner input;
        private static PrintWriter output;

        public Receiver(ConcurrentLinkedQueue<String> queue){
            this.queue = queue;
        }

        @Override
        public void run() {
            try (var listener = new ServerSocket(Main.PORT)){
                System.out.println("Receiver is running.");
                var pool = Executors.newFixedThreadPool(MAX_THREADS);
                while(true){
                    pool.execute(new ReceiverThread(listener.accept(), queue));
                }
            } catch (IOException e) {
                System.out.println("IOException");
            }
        }
    }

    private static class ReceiverThread implements Runnable {
        private Socket socket;
        ConcurrentLinkedQueue<String> queue;

        ReceiverThread(Socket socket, ConcurrentLinkedQueue<String> queue){
            this.socket = socket;
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                var input = new Scanner(socket.getInputStream());
                var output = new PrintWriter(socket.getOutputStream(), true);

                String message = "";

                while(input.hasNextLine()){
                    String text = input.nextLine();
                    if(text.equals("EOM")){
                        //System.out.println("New message being sent: " + message);
                        queue.add(message);
                        message = "";
                    } else {
                        message += text + "\n";
                    }
                    output.println(text);
                }
            } catch(UnknownHostException uhe){
                System.out.println("Connection failure: unknown host.");
            } catch(IOException ioe){
                System.out.println("Connection failure: IOException.");
                System.out.println(ioe.toString());
            } finally {
                try{
                    socket.close();
                } catch(IOException e){

                }
                System.out.println("Socket closed.");
            }
        }
    }

    private static class ConsoleModel implements Runnable {
        ConcurrentLinkedQueue<String> queue;

        ArrayList<Message> messages;

        public ConsoleModel(ConcurrentLinkedQueue<String> queue){
            this.queue = queue;
            messages = new ArrayList<>();
        }

        @Override
        public void run() {
            while(true){
                while(!queue.isEmpty()){
                    String message = queue.remove();
                    Message m = new Message(message);
                    messages.add(m);
                    System.out.println(messages.size() + ":::" + m.toString(true));
                }
            }
        }
    }
}
