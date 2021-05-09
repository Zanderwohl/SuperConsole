package com.zanderwohl.console;

import javax.swing.*;
import java.awt.*;

public class ErrorWindow extends JFrame{

    public ErrorWindow(String error){
        this.setResizable(false);
        this.setMinimumSize(new Dimension(400, 200));
        this.setResizable(true);
        this.setTitle("Error!");

        JPanel errorArea = new JPanel();
        this.add(errorArea);
        errorArea.setLayout(new GridLayout(2, 1));

        errorArea.add(new JLabel(error));

        JButton close = new JButton("Close");
        close.setMargin(new Insets(10,10,10,10));
        close.addActionListener(e ->{
            this.dispose();
        });
        errorArea.add(close);
        this.setVisible(true);
    }
}
