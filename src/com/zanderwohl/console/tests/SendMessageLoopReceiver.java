package com.zanderwohl.console.tests;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class SendMessageLoopReceiver {

    public static void main(String[] args) throws Exception{
        try (var listener = new ServerSocket(201)){
            System.out.println("SendMessageLoopReciever is running.");
            var pool = Executors.newFixedThreadPool(20);
            while(true){
                pool.execute(new Paddle(listener.accept()));
            }
        }
    }

    private static class Paddle implements Runnable{
        private Socket socket;

        Paddle(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run(){
            System.out.println("Connected: " + socket);
            try {
                var in = new Scanner(socket.getInputStream());
                var out = new PrintWriter(socket.getOutputStream(), true);
                while(in.hasNextLine()){
                    String text = ":::" + in.nextLine();
                    System.out.println(text);
                    out.println(text);
                }
            } catch (Exception e){
                System.out.println("Error: " + socket + "\n" + e);
            } finally {
                try{
                    socket.close();
                } catch(IOException e){

                }
                System.out.println("Socket closed.");
            }
        }
    }
}
