package sg.nemysolutions.hapme;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;
import java.util.List;

public class Command implements Serializable {
    private String opsName;
    private String commandName;
    private List<String> vibrationSeq;
    private List<String> gestureSeq;

    public Command() {

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