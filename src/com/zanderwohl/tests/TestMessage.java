package com.zanderwohl.tests;

import com.zanderwohl.Message;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestMessage {

    @Test
    public void propertiesConstructor(){
        String defaultCategory = "None";
        String defaultMessage = "This is a blank message and has not been initialized.";
        String defaultSource = "This message has no given source.";
        String defaultSeverity = Message.severities.NORMAL.toString();
        String defaultTime = "0";

        Message blankMessage = new Message("");
        assertEquals("category=" + defaultCategory + "\nseverity=" +  defaultSeverity +
                "\nmessage=" + defaultMessage + "\nsource=" + defaultSource + "\ntime=" + defaultTime,
                blankMessage.toString());

        Message malformedSeverity = new Message("severity=This isn't a valid severity.");
        assertEquals("category=" + defaultCategory + "\nseverity=" +  defaultSeverity +
                "\nmessage=" + defaultMessage + "\nsource=" + defaultSource + "\ntime=" + defaultTime,
                malformedSeverity.toString());

        Message lowercaseSeverity = new Message("severity=critical");
        String critical = Message.severities.CRITICAL.toString();
        assertEquals("category=" + defaultCategory + "\nseverity=" +  critical +
                "\nmessage=" + defaultMessage + "\nsource=" + defaultSource + "\ntime=" + defaultTime,
                lowercaseSeverity.toString());

        String cat = "category=Some Category\n";
        String msg = "message=This message has content which is not the default.\n";
        String src = "source=Test\n";
        String sev = "severity=" + Message.severities.INFO.toString() + "\n";
        String time = "time=5020124";
        Message allFields = new Message(cat + msg + src + sev + time);
        assertEquals(cat + sev + msg + src + time, allFields.toString());
    }
}
