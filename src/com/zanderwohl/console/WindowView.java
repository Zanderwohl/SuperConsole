package com.zanderwohl.console;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class WindowView implements Runnable {

    CopyOnWriteArrayList<Message> messages;
    ConcurrentLinkedQueue<String> userQueue;

    JFrame frame;
    ArrayList<ConsoleTab> panels = new ArrayList<>();

    JTabbedPane tabbedPane;

    int messagesLengthPrevious = 0;

    public WindowView(CopyOnWriteArrayList<Message> messages, ConcurrentLinkedQueue<String> userQueue){
        this.messages = messages;
        this.userQueue = userQueue;

        this.frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Main.close();
            }
        });

        buildWindow();
    }

    private void buildWindow(){
        frame.setLayout(new BorderLayout());
        JMenuBar menuBar = new JMenuBar();
        addFileMenu(menuBar);
        frame.setJMenuBar(menuBar);

        tabbedPane = new JTabbedPane();

        String consoleName = "Console";
        String blankName = "Blank";

        ConsoleTab firstPanel = new ConsoleTab(this, consoleName);
        ConsoleTab secondPanel = new ConsoleTab(this, blankName);
        panels.add(firstPanel);
        tabbedPane.addTab(consoleName, firstPanel);
        panels.add(secondPanel);
        tabbedPane.addTab(blankName, secondPanel);

        //for(JPanel panel: panels) {
        //    tabbedPane.addTab("Console", panel);
        //}

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(600, 400));
        frame.pack();
    }

    private void addFileMenu(JMenuBar menuBar){
        JMenu menu = new JMenu("Connection");
        menu.setMnemonic(KeyEvent.VK_C);
        menu.getAccessibleContext().setAccessibleDescription(
                "The menu to connect and disconnect from programs.");

        JMenuItem connect = new JMenuItem("New Connection");
        connect.setMnemonic(KeyEvent.VK_N);
        menu.add(connect);

        JMenuItem disconnect = new JMenuItem("Disconnect");
        disconnect.setMnemonic(KeyEvent.VK_D);
        menu.add(disconnect);

        menu.addSeparator();

        JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic(KeyEvent.VK_E);
        menu.add(exit);

        menuBar.add(menu);
    }

    public void updateGUI(){
        int end = messages.size();
        if(messagesLengthPrevious < end){
            for(int i = messagesLengthPrevious; i < end; i++){
                Message m = messages.get(i);
                panels.get(0).addMessage(m);
            }
        }

        messagesLengthPrevious = messages.size();


        panels.get(0).screenUpdate();
    }

    private Container buildMessageHorizontal(Message m, int index){
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        //Random deleteThis = new Random();
        //p.setBackground(new Color(deleteThis.nextInt()));
        int height = 40;
        p.setPreferredSize(new Dimension(400, height));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        p.setMinimumSize(new Dimension(300, height));
        GridLayout layout = new GridLayout(1,6);
        p.setLayout(layout);
        p.add("index", new JLabel(index + ""));
        p.add("category", new JLabel(m.getAttribute("category")));
        p.add("message", new JLabel(m.getAttribute("message")));
        p.add("source", new JLabel(m.getAttribute("source")));
        p.add("time", new JLabel(m.getAttribute("time")));
        p.add("severity", new JLabel(m.getAttribute("severity")));
        return p;
    }

    public void submitUserInput(String input, String source){
        Message m = new Message("severity=user\nmessage=" + input + "\nsource=" + source);
        userQueue.add(m.toString());
    }


    @Override
    public void run() {
        frame.setVisible(true);
        while(true){
            updateGUI();
            try {
                TimeUnit.MILLISECONDS.sleep(20);
                //System.out.println();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //updateGUI(); //down here, we wait for the other threads updating first.
        }
    }
}
