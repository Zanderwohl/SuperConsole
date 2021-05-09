package com.zanderwohl.console;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class SuperConsole{

    //TODO: Remove this once all connections are optional.
    public static final int PORT = 288;
    public WindowView window;
    public CopyOnWriteArrayList<Connection> connections;

    public SuperConsole(){
        connections = new CopyOnWriteArrayList<>();
        window = new WindowView(this);
        Thread windowThread = new Thread(window);
        windowThread.start();

        //TODO: Remove this once all connections are optional
        try {
            newConnection("Localhost:" + PORT, "localhost", PORT);
        } catch (IOException e){
            e.printStackTrace();
        }

        Thread connectionManagerThread = new Thread(new ConnectionManager(connections));
        connectionManagerThread.start();
    }

    public static void main(String[] args) {
        SuperConsole sc = new SuperConsole();
    }

    public void newConnection(String name, String host, int port) throws IOException{
        Connection newConnection = new Connection(name, host, port);
        window.addTab(newConnection.tab());
        connections.add(newConnection);
    }

    public void closeConnection(Connection c){
        connections.remove(c);
        c.close();
    }

    public static void close(){
        System.exit(0);
    }

    private class ConnectionManager implements Runnable {

        public CopyOnWriteArrayList<Connection> connections;

        public ConnectionManager(CopyOnWriteArrayList<Connection> connections){
            this.connections = connections;
        }

        @Override
        public void run() {
            while(true) {
                for (Connection c : connections) {
                    c.update();
                }
            }
        }
    }
}
