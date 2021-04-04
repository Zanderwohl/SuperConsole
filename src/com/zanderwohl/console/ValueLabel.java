package com.zanderwohl.console;

import javax.swing.*;
import java.awt.*;

public class ValueLabel extends JPanel {

    private JLabel label;
    private JLabel value;

    public ValueLabel(String label){
        super();
        this.label = new JLabel(label + ":");
        value = new JLabel("---");
        this.setLayout(new GridLayout(1, 2));
        this.add(this.label);
        this.add(value);
        this.setBackground(new Color(163, 163, 163));
    }

    public void setValue(String value){
        this.value.setText(value);
    }
}
