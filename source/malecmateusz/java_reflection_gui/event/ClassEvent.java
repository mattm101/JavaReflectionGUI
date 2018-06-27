package com.malecmateusz.java_reflection_gui.event;

import java.util.EventObject;
import java.util.List;

public class ClassEvent extends EventObject {
    public static final int LOAD_LIST = 0;
    public static final int CLEAR_LIST = 1;
    public static final int ERROR = 2;

    private final int opCode;
    private List<Class> classes;
    private String errMessage = "";

    public ClassEvent(Object source, final int opCode, List<Class> classes) {
        super(source);
        this.opCode = opCode;
        this.classes = classes;
    }

    public ClassEvent(Object source, final int opCode, String errMessage) {
        super(source);
        this.opCode = opCode;
        this.classes = null;
        this.errMessage = errMessage;
    }
    public List<Class> getClasses() {
        return classes;
    }
    public int getOpCode(){return opCode; }
    public String getErrMessage(){return errMessage;}
}
