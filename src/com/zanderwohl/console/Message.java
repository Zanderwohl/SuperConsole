package com.zanderwohl.console;

import org.json.JSONException;
import org.json.JSONObject;
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
    private String source = "No Source.";
    private severities severity = severities.NORMAL;
    private int timestamp = 0;
    private String from = "";
    private String to = "";

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
        } else {
            timestamp = (int)System.currentTimeMillis();
        }
        String from_ = Properties.get(msgMap, "source");
        if(from_ != null){
            from = from_;
        }
        String to_ = Properties.get(msgMap, "source");
        if(to_ != null){
            to = to_;
        }
    }

    public Message(JSONObject msg){
        try {
            category = msg.getString("category");
        } catch(JSONException e){

        }
        try {
            severity = severities.valueOf(msg.getString("severity").toUpperCase());
        } catch(JSONException e){

        }
        try {
            message = msg.getString("message");
        } catch(JSONException e){

        }
        try {
            source = msg.getString("source");
        } catch(JSONException e){

        }
        try {
            timestamp = Integer.parseInt(msg.getString("time"));
        } catch(JSONException e){
            timestamp = (int)System.currentTimeMillis();
        }
        try {
            to = msg.getString("to");
        } catch(JSONException e){

        }
        try {
            from = msg.getString("from");
        } catch(JSONException e){

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
                nl(b) + "from=" + from +
                nl(b) + "to=" + to +
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
     * @param key severity, message, source, time, category, from, to
     * @return The value associated with the key.
     */
    public String getAttribute(String key){
        if(key.equalsIgnoreCase("severity")){
            return severity.toString();
        }
        if(key.equalsIgnoreCase("message")){
            return message.replaceAll("\\\\n","\n");
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
        if(key.equalsIgnoreCase("from")){
            return from;
        }
        if(key.equalsIgnoreCase("to")){
            return to;
        }
        return "They key '" + key + "' is not an attribute of Message.";
    }

    /**
     * Sets certain attributes, but only if they're not already defined.
     * @param key from, to
     */
    public void setAttribute(String key, String value){
        if(key.equalsIgnoreCase("from")){
            if(from.equals("")){
                from = value;
            }
        }
        if(key.equalsIgnoreCase("to")){
            if(to.equals("")){
                to = value;
            }
        }
    }
}
