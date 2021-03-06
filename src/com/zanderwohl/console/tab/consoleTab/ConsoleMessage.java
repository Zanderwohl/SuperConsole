package com.zanderwohl.console.tab.consoleTab;

import com.zanderwohl.console.Message;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ConsoleMessage extends JPanel {

    private static final int height = 40;

    public final int indexInt;
    JLabel index;
    JLabel category;
    JLabel message;
    JLabel source;
    protected JLabel time;
    JLabel severity;
    public final Message m;

    public static Color selectedColor = new Color(219, 144, 144);
    public static Color nonSelectedColor = new Color(206, 206, 206);

    public ConsoleMessage(Message m, int index){
        super();
        this.m = m;
        indexInt = index;
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        this.setPreferredSize(new Dimension(400, height));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        this.setMinimumSize(new Dimension(300, height));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        this.setBackground(nonSelectedColor);

        this.index = new JLabel(index + "");
        category = new JLabel(m.getAttribute("category"));
        String[] splitMessage = m.getAttribute("message").split("\\n");
        message = new JLabel(splitMessage[0] + (splitMessage.length > 1 ? " [...]" : ""));
        source = new JLabel(m.getAttribute("source"));

        DateFormat f = new SimpleDateFormat("HH:mm:ss");
        long time_long = Long.parseLong(m.getAttribute("time")) * 1000;
        String time_string = f.format(time_long);
        time = new JLabel(time_string);

        severity = new JLabel(m.getAttribute("severity"));

        JLabel[] set = {this.index, category, source, time, severity};
        Dimension d = new Dimension(70, 40);
        for(JLabel j: set){
            j.setPreferredSize(d);
            j.setMaximumSize(d);
            j.setMinimumSize(d);
        }

        d = new Dimension(100, 40);
        source.setMinimumSize(d);
        source.setMaximumSize(d);
        source.setPreferredSize(d);

        message.setMinimumSize(new Dimension(200, 40));

        if(index == -1){
            this.index.setText("");
            category.setText("Category");
            message.setText("Message");
            source.setText("Source");
            time.setText("Time");
            severity.setText("Severity");
        }

        setSeverityColor(severity, m);

        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridy = 1;
        c.weightx = 0.0;
        c.insets = new Insets(5, 5, 5, 5);
        this.add(this.index, c);
        this.add(category, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.gridwidth = 7;
        this.add(message, c);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        this.add(source, c);
        this.add(time, c);
        this.add(severity, c);
    }

    private static void setSeverityColor(JLabel label, Message m){
        String severity = m.getAttribute("severity");
        if(severity.equals(Message.severities.NORMAL.toString())){
            label.setForeground(Color.BLACK);
        }
        if(severity.equals(Message.severities.WARNING.toString())){
            label.setForeground(Color.YELLOW);
        }
        if(severity.equals(Message.severities.CRITICAL.toString())){
            label.setForeground(Color.RED);
        }
        if(severity.equals(Message.severities.INFO.toString())){
            label.setForeground(Color.GREEN);
        }
        if(severity.equals(Message.severities.CONSOLE.toString())){
            label.setForeground(Color.ORANGE);
        }
        if(severity.equals(Message.severities.USER.toString())){
            label.setForeground(Color.BLUE);
        }
    }

    public void select(){
        this.setBackground(selectedColor);
    }

    public void deselect(){
        this.setBackground(nonSelectedColor);
    }
}
