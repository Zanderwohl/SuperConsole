package com.zanderwohl.console;

import com.sun.jdi.Value;
import org.junit.runners.Parameterized;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ConsoleTab extends JPanel {

    WindowView parent;
    String tabName = "Console";

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
                ((ValueLabel) messageDetailTime).setValue(currentlySelectedMessage.time.getText());
                ((ValueLabel) messageDetailSeverity).setValue(currentlySelectedMessage.m.getAttribute("severity"));
                ((JTextArea) messageDetailMessage).setText(currentlySelectedMessage.m.getAttribute("message"));
            }
        }
    };

    public ConsoleTab(WindowView parent, String name){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.parent = parent;
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
        messageDetailMessage = new JTextArea("THE MESSAGE WHICH CANST");
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
        //JScrollPane detailScrollPane = new JScrollPane();
        //detailScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        //detailScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        //detail.add(detailScrollPane, g);
        //detailScrollPane.add(messageDetailMessage);
        detail.add(messageDetailMessage, g);
        footer.add(detail);
        testLabel = new JLabel("???");
        footer.add(testLabel);

        autoScrollCheck = new JCheckBox("Auto-Scroll", true);
        autoScrollCheck.setToolTipText("Automatically scroll to bottom as new messages appear.");
        footer.add(autoScrollCheck);
    }

    public void addMessage(Message m){
        messagesCount++;
        ConsoleMessage newMessage = new ConsoleMessage(m, messagesCount);
        newMessage.addMouseListener(this.consoleMessageClicked);
        messagePanel.add(newMessage);
        testLabel.setText("Messages: " + messagesCount);
    }

    public void screenUpdate(){
        if(autoScrollCheck.isSelected()){
            scrollbar.setValue(scrollbar.getMaximum());
        }
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
        parent.submitUserInput(input, tabName);
    }
}
