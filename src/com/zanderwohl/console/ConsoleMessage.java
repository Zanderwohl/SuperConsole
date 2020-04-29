package com.zanderwohl.console;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class ConsoleMessage extends JPanel {

    private static final int height = 40;

    JLabel index;
    JLabel category;
    JLabel message;
    JLabel source;
    JLabel time;
    JLabel severity;

    public ConsoleMessage(Message m, int index){
        super();
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        this.setPreferredSize(new Dimension(400, height));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        this.setMinimumSize(new Dimension(300, height));
        GridLayout layout = new GridLayout(1,6);
        this.setLayout(layout);

        this.index = new JLabel(index + "");
        category = new JLabel(m.getAttribute("category"));
        message = new JLabel(m.getAttribute("message"));
        source = new JLabel(m.getAttribute("source"));
        time = new JLabel(m.getAttribute("time"));
        severity = new JLabel(m.getAttribute("severity"));

        if(index == -1){
            this.index.setText("");
            category.setText("Category");
            message.setText("Message");
            source.setText("Source");
            time.setText("Time");
            severity.setText("Severity");
        }

        setSeverityColor(severity, m);

        this.add(this.index);
        this.add(category);
        this.add(message);
        this.add(source);
        this.add(time);
        this.add(severity);
    }

    private static void setSeverityColor(JLabel label, Message m){
        String severity = m.getAttribute("severity");
        if(severity == Message.severities.NORMAL.toString()){
            label.setForeground(Color.BLACK);
        }
        if(severity == Message.severities.WARNING.toString()){
            label.setForeground(Color.YELLOW);
        }
        if(severity == Message.severities.CRITICAL.toString()){
            label.setForeground(Color.RED);
        }
        if(severity == Message.severities.INFO.toString()){
            label.setForeground(Color.GREEN);
        }
        if(severity == Message.severities.CONSOLE.toString()){
            label.setForeground(Color.orange);
        }
        if(severity == Message.severities.USER.toString()){
            label.setForeground(Color.BLUE);
        }
    }
}
