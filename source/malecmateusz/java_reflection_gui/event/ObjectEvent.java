package com.malecmateusz.java_reflection_gui.event;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.EventObject;
import java.util.logging.Logger;

public class ObjectEvent extends EventObject {
    private String errMessage;
    private Object instance;

    public ObjectEvent(Object source, Object instance) {
        super(source);
       this.instance = instance;
    }
    public ObjectEvent(Object source, String errMessage) {
        super(source);
        this.instance = null;
        this.errMessage = errMessage;
    }

    public Object getInstance(){return instance;}
    public String toString(){
        if(instance == null) return errMessage;
            Class clazz = instance.getClass();
            Field[] fields = clazz.getDeclaredFields();
            StringBuilder sb = new StringBuilder();
            sb.append("Obiekt klasy ").append(clazz.getName()).append("\n");
            for (Field f : fields) {
                f.setAccessible(true);
                sb.append(Modifier.toString(f.getModifiers())).append(" ").append(f.getType().getName()).append(" ");

                sb.append(f.getName()).append(" : ");
                try {
                    Object v = f.get(instance);
                    sb.append(v.toString()).append("\n");
                } catch (IllegalAccessException e1) {
                    Logger.getGlobal().severe(e1.getMessage());
                }
            }
            return sb.toString();
    }
}
