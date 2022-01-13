package com.zanderwohl.console.tab;

import com.zanderwohl.console.*;
import com.zanderwohl.console.tab.consoleTab.ConsoleMessage;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ConsoleTab extends Tab {

    WindowView parent = null;
    private Connection associatedConnection = null;
    String tabName;

    JPanel messagePanel;
    JScrollBar scrollbar;

    UserInput user;

    JPanel header;
    JPanel detail;
    JPanel footer;
    JLabel testLabel;
    JCheckBox autoScrollCheck;

    JComponent messageDetailNumber;
    JComponent messageDetailCategory;
    JComponent messageDetailSource;
    JComponent messageDetailTime;
    JComponent messageDetailSeverity;
    JComponent messageDetailMessage;

    int messagesCount = 0;

    /**
     * For use in windowView.
     */
    public int messagesLengthPrevious = 0;

    ConsoleMessage currentlySelectedMessage = null;
    public MouseAdapter consoleMessageClicked = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if(currentlySelectedMessage != null){
                currentlySelectedMessage.deselect();
            }
            if(e.getSource() instanceof ConsoleMessage){
                currentlySelectedMessage = (ConsoleMessage) e.getSource();
                currentlySelectedMessage.select();
                ((ValueLabel) messageDetailNumber).setValue(currentlySelectedMessage.indexInt + "");
                ((ValueLabel) messageDetailCategory).setValue(currentlySelectedMessage.m.getAttribute("category"));
                ((ValueLabel) messageDetailSource).setValue(currentlySelectedMessage.m.getAttribute("source"));

                DateFormat f = new SimpleDateFormat("HH:mm:ss");
                long time_long = Long.parseLong(currentlySelectedMessage.m.getAttribute("time")) * 1000;
                String time_string = f.format(time_long);
                ((ValueLabel) messageDetailTime).setValue(time_string);

                ((ValueLabel) messageDetailSeverity).setValue(currentlySelectedMessage.m.getAttribute("severity"));
                ((JTextArea) messageDetailMessage).setText(currentlySelectedMessage.m.getAttribute("message"));
            }
        }
    };

    public ConsoleTab(String name, Connection associatedConnection){
        super(name);
        this.associatedConnection = associatedConnection;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.tabName = name;

        header = new ConsoleMessage(new Message(""), -1);
        this.add(header);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.PAGE_AXIS));
        messagePanel.setBackground(Color.PINK);
        JScrollPane scrollPane = new JScrollPane(messagePanel);
        this.add(scrollPane);

        scrollbar = scrollPane.getVerticalScrollBar();
        scrollbar.setUnitIncrement(16);

        user = new UserInput(this);
        this.add(user);

        footer = new JPanel();
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        this.add(footer);

        detail = new JPanel();
        detail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        detail.setPreferredSize(new Dimension(400, 100));
        detail.setBackground(new Color(163, 163, 163));
        detail.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        messageDetailNumber = new ValueLabel("Number");
        messageDetailCategory = new ValueLabel("Category");
        messageDetailSource = new ValueLabel("Source");
        messageDetailTime = new ValueLabel("Time");
        messageDetailSeverity = new ValueLabel("Severity");
        messageDetailMessage = new JTextArea("");
        ((JTextArea) messageDetailMessage).setLineWrap(true);
        ((JTextArea) messageDetailMessage).setWrapStyleWord(true);
        //((JTextArea) messageDetailMessage).setColumns(30);
        g.ipadx = 7;
        g.ipady = 2;
        g.gridx = 0;
        g.gridy = 0;
        g.fill = GridBagConstraints.NONE;
        g.weightx = 1.0;
        g.anchor = GridBagConstraints.LINE_START;
        g.insets = new Insets(0,5,0,2);
        detail.add(messageDetailNumber, g);
        g.gridx = 0;
        g.gridy = 1;
        detail.add(messageDetailCategory, g);
        g.gridx = 0;
        g.gridy = 2;
        detail.add(messageDetailSource, g);
        g.gridx = 0;
        g.gridy = 3;
        detail.add(messageDetailTime, g);
        g.gridx = 0;
        g.gridy = 4;
        detail.add(messageDetailSeverity, g);
        g.gridx = 1;
        g.gridy = 0;
        g.fill = GridBagConstraints.VERTICAL;
        g.weightx = 2.0;
        g.insets = new Insets(2,2,2,5);
        g.gridheight = 5;
        g.anchor = GridBagConstraints.LINE_END;
        detail.add(messageDetailMessage, g);
        footer.add(detail);
        testLabel = new JLabel("???");
        footer.add(testLabel);

        autoScrollCheck = new JCheckBox("Auto-Scroll", true);
        autoScrollCheck.setToolTipText("Automatically scroll to bottom as new messages appear.");
        footer.add(autoScrollCheck);
    }

    public void setParent(WindowView parent){
        this.parent = parent;
    }

    @Override
    public void acceptUserInput(Message m) {
        associatedConnection.acceptUserInput(m);
    }

    @Override
    public boolean usesInputPane() {
        return true;
    }

    public Connection getAssociatedConnection(){
        return associatedConnection;
    }

    public void addMessage(Message m){
        messagesCount++;
        ConsoleMessage newMessage = new ConsoleMessage(m, messagesCount);
        newMessage.addMouseListener(this.consoleMessageClicked);
        messagePanel.add(newMessage);
        testLabel.setText("Messages: " + messagesCount);

        autoscroll();
    }

    public void autoscroll(){
        if(autoScrollCheck.isSelected()){
            scrollbar.setValue(scrollbar.getMaximum());
        }
    }

    public void screenUpdate(){
    }

    private Container buildMessageHorizontal(Message m, int index){
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

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

    public void submitUserInput(String input){
        if(parent != null) {
            parent.submitUserInput(input, tabName);
            autoscroll();
        }
    }
}
