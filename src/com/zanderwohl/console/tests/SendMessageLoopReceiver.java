package com.zanderwohl.console.tests;

import com.zanderwohl.console.Main;
import com.zanderwohl.console.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SendMessageLoopReceiver {

    public static void main(String[] args) throws Exception{
        try (var listener = new ServerSocket(Main.PORT)){
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
                String message = "";
                while(true){
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("Sending packet.");
                    Message m = new Message("source=Test Server\nseverity=WARNING\ncontent=Hello buds!\nEOM");
                    out.println(m);
                    System.out.println(m + "\n");

                    /*while(in.hasNextLine()){
                        String line = in.nextLine();
                        message += "\n" + line;
                        if(line.equals("EOM")) {
                            System.out.println(message + "\n");
                            message = "";
                        }
                    }*/
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
