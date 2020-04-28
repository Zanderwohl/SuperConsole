package com.zanderwohl.console;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class ConsoleTab extends JPanel {

    JPanel messagePanel;
    JScrollBar scrollbar;

    JPanel footer;
    JLabel testLabel;
    JCheckBox autoScrollCheck;

    int messagesCount = 0;

    public ConsoleTab(){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.PAGE_AXIS));
        messagePanel.setBackground(Color.PINK);
        JScrollPane scrollPane = new JScrollPane(messagePanel);
        this.add(scrollPane);

        scrollbar = scrollPane.getVerticalScrollBar();
        scrollbar.setUnitIncrement(16);

        footer = new JPanel();
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        this.add(footer);

        testLabel = new JLabel("");
        footer.add(testLabel);

        autoScrollCheck = new JCheckBox("Auto-Scroll", true);
        autoScrollCheck.setToolTipText("Automatically scroll to bottom as new messages appear.");
        footer.add(autoScrollCheck);
    }

    public void addMessage(Message m){
        messagesCount++;
        messagePanel.add(new ConsoleMessage(m, messagesCount));
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
}
