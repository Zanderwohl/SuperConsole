package com.zanderwohl.console;

import com.zanderwohl.console.tab.ConsoleTab;
import com.zanderwohl.console.tab.Tab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class WindowView implements Runnable {

    SuperConsole parent;

    JFrame frame;
    ArrayList<Tab> tabs = new ArrayList<>();

    JTabbedPane tabbedPane;

    public WindowView(SuperConsole parent){

        this.parent = parent;
        this.frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                parent.quit();
            }
        });

        buildWindow();
    }

    private void buildWindow(){
        frame.setLayout(new BorderLayout());
        JMenuBar menuBar = new JMenuBar();
        addFileMenu(menuBar);
        frame.setJMenuBar(menuBar);
        frame.setTitle("SuperConsole");

        tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(e ->{
            Tab tab = (Tab) tabbedPane.getSelectedComponent();
            if(tab == null){
                frame.setTitle("SuperConsole");
            } else {
                this.frame.setTitle("SuperConsole: " + tab.name);
            }
        });

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(600, 400));
        frame.pack();
    }

    public void addTab(Tab tab){
        tabbedPane.addTab(tab.name, tab);
        tabs.add(tab);
        tab.setParent(this);
    }

    public void removeTab(Tab tab){
        tabbedPane.remove(tab);
        tabs.remove(tab);
    }

    private void addFileMenu(JMenuBar menuBar){
        JMenu menu = new JMenu("Connection");
        menu.setMnemonic(KeyEvent.VK_C);
        menu.getAccessibleContext().setAccessibleDescription(
                "The menu to connect and disconnect from programs.");

        JMenuItem connect = new JMenuItem("New Connection");
        connect.setMnemonic(KeyEvent.VK_N);
        connect.getAccessibleContext().setAccessibleDescription("Start a new connection with a server.");
        menu.add(connect);
        connect.addActionListener(e -> {
            new NewConnectionWindow(parent);
        });

        JMenu quickConnect = new JMenu("Quick Connect");
        quickConnect.setMnemonic(KeyEvent.VK_Q);
        menu.add(quickConnect);
        quickConnect.setEnabled(false);

        //TODO: Make this settings-able
        JMenuItem localConnect = new JMenuItem("Local Default");
        localConnect.setMnemonic(KeyEvent.VK_L);
        quickConnect.add(localConnect);
        quickConnect.setEnabled(true);
        localConnect.addActionListener(e -> {
            parent.newConnection("Local Server", "localhost", 288);
        });

        JMenuItem disconnect = new JMenuItem("Disconnect");
        disconnect.setMnemonic(KeyEvent.VK_D);
        connect.getAccessibleContext().setAccessibleDescription("End the current connection.");
        menu.add(disconnect);
        disconnect.addActionListener(e -> {
            Tab activeTab = (Tab) tabbedPane.getSelectedComponent();
            if(activeTab instanceof ConsoleTab){
                ConsoleTab consoleTab = (ConsoleTab) activeTab;
                Connection c = consoleTab.getAssociatedConnection();
                removeTab(activeTab);
                parent.closeConnection(c);
            }
        });



        menu.addSeparator();

        JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic(KeyEvent.VK_E);
        exit.getAccessibleContext().setAccessibleDescription("Exit the console, ending current connection.");
        menu.add(exit);
        exit.addActionListener(e -> {
            parent.quit();
        });

        menuBar.add(menu);
    }

    public void updateGUI(){
        Tab activeTab = (Tab) tabbedPane.getSelectedComponent();
        if(activeTab == null){
            return;
        }
        activeTab.screenUpdate();

        if(activeTab instanceof ConsoleTab){
            ConsoleTab consoleTab = (ConsoleTab) activeTab;
            CopyOnWriteArrayList<Message> messages = consoleTab.getAssociatedConnection().messageHistory;
            if(messages != null) {
                int end = messages.size();
                if (consoleTab.messagesLengthPrevious < end) {
                    for (int i = consoleTab.messagesLengthPrevious; i < end; i++) {
                        Message m = messages.get(i);
                        consoleTab.addMessage(m);
                    }
                }

                consoleTab.messagesLengthPrevious = messages.size();
            }
        }
    }

    public void submitUserInput(String input, String source){
        Message m = new Message("severity=user\nmessage=" + input + "\nsource=" + source +
                "\ntime=" + Instant.now().getEpochSecond());
        Tab activeTab = (Tab) tabbedPane.getSelectedComponent();
        if(activeTab == null){
            return;
        }
        activeTab.acceptUserInput(m);
    }

    @Override
    public void run() {
        frame.setVisible(true);
        while(true){
            updateGUI();
            try {
                TimeUnit.MILLISECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
