package com.zanderwohl.console.tab;

import com.zanderwohl.console.Message;
import com.zanderwohl.console.WindowView;

import javax.swing.*;

/**
 * All tabs given to SuperConsole should inherit from this.
 */
public abstract class Tab extends JPanel {

    public final String name;

    protected Tab(String name) {
        this.name = name;
    }

    public abstract void screenUpdate();
    public abstract void setParent(WindowView parent);
    public abstract void acceptUserInput(Message m);
    public abstract boolean usesInputPane();
}
