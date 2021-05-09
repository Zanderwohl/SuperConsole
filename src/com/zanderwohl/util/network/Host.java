package com.zanderwohl.util.network;

public class Host {

    public static boolean validHost(String host){
        return true;
    }

    public static boolean validPort(String port){
        int portAsInt;
        try {
            portAsInt = Integer.parseInt(port);
        } catch (NumberFormatException e){
            return false;
        }
        if(portAsInt > 65535){
            return false;
        }
        return true;
    }

    public static int parsePort(String port){
        int portAsInt;
        try {
            portAsInt = Integer.parseInt(port);
        } catch (NumberFormatException e){
            return 0;
        }
        if(portAsInt > 65535){
            return 0;
        }
        return portAsInt;
    }
}
