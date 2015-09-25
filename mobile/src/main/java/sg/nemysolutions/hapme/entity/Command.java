package sg.nemysolutions.hapme.entity;

import java.io.Serializable;
import java.util.List;

public class Command implements Serializable {

    private String commandID;
    private String opsName;
    private String commandName;
    private String vibrationSeq;
    private List<String> gestureSeq;
    private String color;

    public Command() {}

    public String getCommandID() {
        return commandID;
    }

    public void setCommandID(String commandID) {
        this.commandID = commandID;
    }

    public String getOpsName() {
        return opsName;
    }

    public void setOpsName(String opsName) {
        this.opsName = opsName;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getVibrationSeq() {
        return vibrationSeq;
    }

    public void setVibrationSeq(String vibrationSeq) {
        this.vibrationSeq = vibrationSeq;
    }

    public List<String> getGestureSeq() {
        return gestureSeq;
    }

    public void setGestureSeq(List<String> gestureSeq) {
        this.gestureSeq = gestureSeq;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}