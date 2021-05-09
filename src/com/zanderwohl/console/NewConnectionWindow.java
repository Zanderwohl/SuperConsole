package com.zanderwohl.console;

import com.zanderwohl.util.network.Host;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class NewConnectionWindow extends JFrame{

    JPanel window;
    
    JTextField nameField;
    JTextField hostField;
    JTextField portField;
    JButton connectButton;

    SuperConsole parent;

    public NewConnectionWindow(SuperConsole parent){
        this.parent = parent;
        this.setMinimumSize(new Dimension(300, 200));
        this.pack();
        this.setTitle("New Connection");
        this.setResizable(false);
        populateWindow();
        this.setVisible(true);
    }
    
    private void populateWindow(){
        window = new JPanel();
        window.setLayout(new GridLayout(4, 2));

        nameField = new JTextField();
        hostField = new JTextField();
        portField = new JTextField();
        connectButton = new JButton("Connect");

        connectButton.addActionListener(e -> {
            String name = nameField.getText();
            String host = hostField.getText();
            if(!Host.validHost(host)){
                new ErrorWindow("Invalid host!");
                return;
            }
            int port = Host.parsePort(portField.getText());
            if(!Host.validPort(portField.getText())){
                new ErrorWindow("Invalid Port!");
                return;
            }
            parent.newConnection(name, host, port);
            this.dispose();
        });

        window.add(new JLabel("Connection Name:"));
        window.add(nameField);
        window.add(new JLabel("Host:"));
        window.add(hostField);
        window.add(new JLabel("Port:"));
        window.add(portField);
        window.add(connectButton);

        this.add(window);
    }
}
