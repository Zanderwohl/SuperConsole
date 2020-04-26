package com.zanderwohl.console;

import com.zanderwohl.util.Properties;

import java.util.HashMap;

public class Message {

    /**
     * Enumerator of possible message severities, for sorting and formatting purposes.
     * Normal denotes normal-conditions message.
     * Warning denotes error that can be gracefully recovered from by the program.
     * Critical denotes error that cannot be gracefully recovered from by the program.
     * Info denotes emphasized message that is not an error.
     * Console denotes message that was input by the user.
     */
    public enum severities {NORMAL, WARNING, CRITICAL, INFO, CONSOLE, USER}

    private String category = "None";
    private String message = "This is a blank message and has not been initialized.";
    private String source = "This message has no given source.";
    private severities severity = severities.NORMAL;
    private int timestamp = 0;

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
        String timestamp_ = Properties.get(msgMap, "time");
        if(timestamp_ != null){
            int timestamp_int = Integer.parseInt(timestamp_);
            timestamp = timestamp_int;
        }
    }

    /**
     * Override of the toString() methods.
     * @return A .properties format string representation of the message.
     */
    @Override
    public String toString(){
        return toString(true);
    }

    /**
     * A tostring that can return the message, separating the attributes by newlines or commas.
     * @param b If true, newline will be used between each attribute. Otherwise, comma used.
     * @return String representation in .properties format.
     */
    public String toString(Boolean b) {
        return "category=" + category +
                nl(b) + "severity=" + severity.toString() +
                nl(b) + "message=" + message +
                nl(b) + "source=" + source +
                nl(b) + "time=" + timestamp +
                nl(b) + "EOM";
    }

    /**
     * Chooses whether a newline should be added or just a comma.
     * Used for printing messages inline or not.
     * @param b If true, newline will be used between each attribute. Otherwise, comma used.
     * @return The newline or comma.
     */
    private String nl(boolean b){
        if(b){
            return "\n";
        } else {
            return ",";
        }
    }

    /**
     * TODO: Finish this.
     * @param key
     * @return
     */
    public String getAttribute(String key){
        if(key.equalsIgnoreCase("severity")){
            return severity.toString();
        }
        if(key.equalsIgnoreCase("message")){
            return message;
        }
        if(key.equalsIgnoreCase("source")){
            return source;
        }
        if(key.equalsIgnoreCase("time")){
            return Integer.toString(timestamp);
        }
        if(key.equalsIgnoreCase("category")){
            return category;
        }
        return "They key '" + key + "' is not an attribute of Message.";
    }
}
