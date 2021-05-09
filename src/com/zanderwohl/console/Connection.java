package com.zanderwohl.console;

import com.zanderwohl.console.tab.ConsoleTab;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Connection {

    /**
     * Messages from the connected program.
     */
    public final ConcurrentLinkedQueue<Message> fromProgram;
    /**
     * Messages from the user, as typed in the console.
     */
    public final ConcurrentLinkedQueue<Message> fromUser;
    /**
     * Messages sent to the program.
     */
    public final ConcurrentLinkedQueue<Message> toProgram;
    /**
     * All the messages associated with this sever-console relationship.
     */
    public final CopyOnWriteArrayList<Message> messageHistory;
    /*
     * Messages from toUser go to toProgram and messageHistory
     * Messages from fromProgram go to messageHistory
     */

    /**
     * The server socket to communicate with the program through.
     */
    private final Socket socket;
    /**
     * Input from the network from the connected program.
     */
    private final Scanner programInput;
    /**
     * Output to the network to the connected program.
     */
    private final PrintWriter programOutput;

    /**
     * The UI tab displaying the messages.
     */
    private ConsoleTab tab = null;
    /**
     * The name of the tab.
     */
    public final String name;

    private final Thread receiverThread;

    private final AtomicBoolean running;

    /**
     * All the model and UI components that a connection needs.
     * Deletion of this object will delete all object associated with it.
     * @param name The name of the connection; will be displayed on its tab.
     * @param host The host the program to connect to is at.
     * @param port The port the program to connect to can be reached at.
     */
    public Connection(String name, String host, int port) throws IOException {
        running = new AtomicBoolean(true);
        this.name = name;

        fromProgram = new ConcurrentLinkedQueue<>();
        fromUser = new ConcurrentLinkedQueue<>();
        messageHistory = new CopyOnWriteArrayList<>();
        toProgram = new ConcurrentLinkedQueue<>();

        socket = new Socket(host, port);
        programInput = new Scanner(socket.getInputStream());
        programOutput = new PrintWriter(socket.getOutputStream(), true);

        receiverThread = new Thread(new Receiver(fromProgram, programInput, running));
        receiverThread.start();
    }

    /**
     * Get the graphical console tab object.
     * ALWAYS get the tab through here for its null-checking.
     * @return The console tab of this connection.
     */
    public ConsoleTab tab(){
        if(tab == null){
            tab = new ConsoleTab(name, this);
        }
        return tab;
    }

    public void acceptUserInput(Message m){
        fromUser.add(m);
    }

    public void update(){
        while(!fromProgram.isEmpty()){
            messageHistory.add(fromProgram.remove());
        }
        while(!fromUser.isEmpty()){
            toProgram.add(fromUser.remove());
        }
        while(!toProgram.isEmpty()){
            Message m = toProgram.remove();
            messageHistory.add(m);
            programOutput.println(m.toString());
        }
    }

    public void close(){
        running.set(false);
        programOutput.close();
        try {
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static class Receiver implements Runnable {

        ConcurrentLinkedQueue<Message> fromProgram;
        Scanner programInput;
        AtomicBoolean running;

        public Receiver(ConcurrentLinkedQueue<Message> fromProgram, Scanner programInput, AtomicBoolean running){
            this.fromProgram = fromProgram;
            this.programInput = programInput;
            this.running = running;
        }

        @Override
        public void run(){
            System.out.println("Receiver is running.");
            StringBuilder message = new StringBuilder();
            while(running.get()){
                try{
                    while(programInput.hasNextLine()){
                        String line = programInput.nextLine();
                        message.append("\n").append(line);
                        if(line.equals("EOM")) {
                            fromProgram.add(new Message(message.toString()));
                            message = new StringBuilder();
                        }
                    }
                } catch (IllegalStateException e) {
                    running.set(false);
                }
            }
            programInput.close();
        }
    }

}
