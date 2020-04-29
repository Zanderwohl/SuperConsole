package com.zanderwohl.console;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class UserInput extends JPanel {

    JTextField input;
    JButton submit;

    ConsoleTab parent;

    Action buttonEnter = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println("Enter in button.");
            submit();
        }
    };

    ActionListener buttonClick = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println("Click in button.");
            submit();
        }
    };

    Action textTab = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Tab in text input.");
        }
    };

    Action textEnter = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println("Enter in text input.");
            submit();
        }
    };

    public UserInput(ConsoleTab parent){
        super();
        this.parent = parent;
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        input = new JTextField();
        input.setMinimumSize(new Dimension(Integer.MAX_VALUE, 30));
        input.setPreferredSize(new Dimension(Integer.MAX_VALUE, 30));
        input.setBorder(BorderFactory.createEtchedBorder());
        InputMap textInputMap = input.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap textActionMap = input.getActionMap();
        String tab = "tab";
        String enter = "enter";
        textInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), tab);
        textActionMap.put(tab, textTab);
        textInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enter);
        textActionMap.put(enter, textEnter);
        input.setFocusTraversalKeysEnabled(false);

        submit = new JButton("Submit");
        submit.addActionListener(buttonClick);
        InputMap buttonInputMap = submit.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap buttonActionMap = submit.getActionMap();
        buttonInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enter);
        buttonActionMap.put(enter, buttonEnter);

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.1;
        c.insets = new Insets(5, 5, 5, 5);
        this.add(input, c);
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.0;
        this.add(submit, c);
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }

    private void submit(){
        String userText = input.getText();
        if(!userText.equals("")){
            input.setText("");
            //System.out.println("User input text:\n\t" + userText);
            parent.submitUserInput(userText);
        }
    }
}
