package com.malecmateusz.java_reflection_gui.event;

import java.lang.reflect.Constructor;
import java.util.EventObject;
import java.util.List;

public class ConstructorEvent extends EventObject {
    private List<Constructor> constructors;
    public ConstructorEvent(Object source, List<Constructor> constructors) {
        super(source);
        this.constructors = constructors;
    }
    public List<Constructor> getConstructors() {
        return constructors;
    }
}
