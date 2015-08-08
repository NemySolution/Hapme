package sg.nemysolutions.hapme;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;
import java.util.List;

@ParseClassName("Command")
public class Command extends ParseObject implements Serializable {
    public String getCommandID() {
        return getString("commandID");
    }

    public void setCommandID(String value) {
        put("commandID", value);
    }

    public String getOpsName() {
        return getString("opsName");
    }

    public void setOpsName(String value) {
        put("opsName", value);
    }

    public String getCommandName() {
        return getString("commandName");
    }

    public void setCommandName(String value) {
        put("commandName", value);
    }

    public List<String> getVibrationSeq() {
        return getList("vibrationSeq");
    }

    public void setVibrationSeq(List<String> value) {
        put("vibrationSeq", value);
    }

    public List<String> getGestureSeq() {
        return getList("gestureSeq");
    }

    public void setGestureSeq(List<String> value) {
        put("gestureSeq", value);
    }
}