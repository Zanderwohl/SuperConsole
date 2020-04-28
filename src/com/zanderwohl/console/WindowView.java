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
    ArrayList<JPanel> panels = new ArrayList<>();
    ArrayList<JPanel> scrollChildren = new ArrayList<>();
    ArrayList<JScrollBar> panelScrolls = new ArrayList<>(); //scroll behavior for each tab.
    ArrayList<Boolean> autoscroll = new ArrayList<>();
    ArrayList<JLabel> replaceTestLables = new ArrayList<>();
    //JPanel panel;
    JLabel deleteTestLabel;

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
        //frame.add(menuBar, BorderLayout.NORTH);
        frame.setJMenuBar(menuBar);

        JPanel firstPanel = new JPanel();
        panels.add(firstPanel);
        firstPanel.setLayout(new BoxLayout(firstPanel, BoxLayout.PAGE_AXIS));

        JPanel secondPanel = new JPanel();
        panels.add(secondPanel);
        secondPanel.setLayout(new BoxLayout(secondPanel, BoxLayout.PAGE_AXIS));

        tabbedPane = new JTabbedPane();
        for(JPanel panel: panels) {
            tabbedPane.addTab("Console", panel);
            JPanel scrollChild = new JPanel();
            scrollChild.setLayout(new BoxLayout(scrollChild, BoxLayout.PAGE_AXIS));
            scrollChildren.add(scrollChild);
            JScrollPane scrollpane = new JScrollPane(scrollChild);
            panel.add(scrollpane);

            scrollChild.setBackground(Color.PINK);

            JScrollBar sb = scrollpane.getVerticalScrollBar();
            panelScrolls.add(sb);
            autoscroll.add(true);

            JLabel testLabel = new JLabel("");
            replaceTestLables.add(testLabel);
            panel.add(testLabel);
        }
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
        System.out.println(messages.size());

        replaceTestLables.get(0).setText("messages: " + messages.size());
        int end = messages.size();
        if(messagesLengthPrevious < end){
            for(int i = messagesLengthPrevious; i < end; i++){
                Message m = messages.get(i);
                scrollChildren.get(0).add(buildMessageHorizontal(m, i));
            }
        }

        messagesLengthPrevious = messages.size();

        int selectedPane = tabbedPane.getSelectedIndex();
        if(autoscroll.get(selectedPane)){
            JScrollBar bar = panelScrolls.get(selectedPane);
            bar.setValue(bar.getMaximum());
        }
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


    @Override
    public void run() {
        frame.setVisible(true);
        while(true){
            updateGUI();
            try {
                TimeUnit.MILLISECONDS.sleep(20);
                System.out.println();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //updateGUI(); //down here, we wait for the other threads updating first.
        }
    }
}
