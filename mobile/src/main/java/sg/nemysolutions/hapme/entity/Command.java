package sg.nemysolutions.hapme.entity;

import java.io.Serializable;
import java.util.List;

public class Command implements Serializable {

    private String commandID;
    private String opsId;
    private String commandName;
    private List<String> vibrationSeq;
    private List<String> gestureSeq;

    public Command() {}

    public String getCommandID() {
        return commandID;
    }

    public void setCommandID(String commandID) {
        this.commandID = commandID;
    }

    public String getOpsId() {
        return opsId;
    }

    public void setOpsId(String opsId) {
        this.opsId = opsId;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public List<String> getVibrationSeq() {
        return vibrationSeq;
    }

    public void setVibrationSeq(List<String> vibrationSeq) {
        this.vibrationSeq = vibrationSeq;
    }

    public List<String> getGestureSeq() {
        return gestureSeq;
    }

    public void setGestureSeq(List<String> gestureSeq) {
        this.gestureSeq = gestureSeq;
    }

}