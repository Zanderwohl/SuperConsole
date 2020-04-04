package com.zanderwohl;

import com.zanderwohl.util.Properties;

import java.util.HashMap;

public class Message {

    /**
     * Normal denotes normal-conditions message.
     * Warning denotes error that can be gracefully recovered from by the program.
     * Critical denotes error that cannot be gracefully recovered from by the program.
     * Info denotes emphasized message that is not an error.
     * Console denotes message that was input by the user.
     */
    public enum severities {NORMAL, WARNING, CRITICAL, INFO, CONSOLE}

    private String category = "None";
    private String message = "This is a blank message and has not been initialized.";
    private String source = "This message has no given source.";
    private severities severity = severities.NORMAL;

    /**
     * Constructs a message based on a .properties format of "[key]=[value]" where each property is separated by
     * a newline.
     * @param msg The message in a .properties format.
     */
    public Message(String msg){
        HashMap<String, String> msgMap = Properties.toMap(msg);
        String category_ = Properties.get(msgMap, "category");
        if(category_ != null) {
            category = category_;
        }
        String severity_ = Properties.get(msgMap, "severity");
        if(severity_ != null){
            try {
                severity_ = severity_.toUpperCase();
                severity = severities.valueOf(severity_);
            } catch (IllegalArgumentException e) {
                //do nothing?
            }
        }
        String message_ = Properties.get(msgMap, "message");
        if(message_ != null){
            message = message_;
        }
        String source_ = Properties.get(msgMap, "source");
        if(source_ != null){
            source = source_;
        }
    }

    @Override
    public String toString() {
        return "category=" + category + "\nseverity=" + severity.toString() + "\nmessage=" + message +
                "\nsource=" + source + "\n";
    }
}
