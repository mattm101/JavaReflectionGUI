package com.malecmateusz.java_reflection_gui.reflection;

import com.malecmateusz.java_reflection_gui.event.ClassEvent;
import com.malecmateusz.java_reflection_gui.event.ConstructorEvent;
import com.malecmateusz.java_reflection_gui.event.ObjectEvent;
import com.malecmateusz.java_reflection_gui.reflection.exception.ClassLoaderException;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ClassLoaderController {
    static FolderClassLoaderSingleton fcl = FolderClassLoaderSingleton.getInstance();
    private static ClassLoaderController instance;
    private List<ClassListener> classListeners = new ArrayList();
    private List<ConstructorListener> constructorListeners  = new ArrayList();
    private List<ObjectListener> objectListeners  = new ArrayList();
    private ClassLoaderController(){ }

    public static ClassLoaderController getInstance(){
        if(instance == null) instance = new ClassLoaderController();
        return instance;
    }

    public void loadClasses(String path){
        try {
            fcl.loadClasses(path);
            fireClassEvent(this, ClassEvent.LOAD_LIST, fcl.getLoadedClasses());
        } catch (ClassLoaderException e) {
            fireClassEvent(this,ClassEvent.ERROR, e.getErrMessage());
        }

    }

    public void loadConstructors(Class clazz){
        fireConstructorEvent(this, fcl.getConstructors(clazz));
    }

    public void createInstanceWithParameters(Constructor constructor, String parameters){
        Object object = null;
        try {
            object = fcl.createInstanceWithParameters(constructor, parameters);
            fireObjectEvent(this, object);
        }catch (ClassLoaderException e){
            fireObjectEvent(this, e.getErrMessage());
        }

    }



    public synchronized void addClassListener( ClassListener l ) {
        classListeners.add( l );
    }

    public synchronized void removeClassListener( ClassListener l ) {
        classListeners.remove( l );
    }

    private synchronized void fireClassEvent(Object source,final int opCode, List<Class> classes) {
        ClassEvent ce = new ClassEvent(source,opCode, classes);
        for(ClassListener l : classListeners) l.classesReceived(ce);
    }

    private synchronized void fireClassEvent(Object source,final int opCode, String errMessage) {
        ClassEvent ce = new ClassEvent(source,opCode, errMessage);
        for(ClassListener l : classListeners) l.classesReceived(ce);
    }

    public synchronized void addConstructorListener( ConstructorListener l ) {
        constructorListeners.add( l );
    }

    public synchronized void removeConstructorListener( ConstructorListener l ) {
        constructorListeners.remove( l );
    }

    private synchronized void fireConstructorEvent(Object source, List<Constructor> constructors) {
        ConstructorEvent ce = new ConstructorEvent(source, constructors);
        for(ConstructorListener l : constructorListeners) l.constructorsReceived(ce);
    }

    public synchronized void addObjectListener( ObjectListener l ) {
        objectListeners.add( l );
    }

    public synchronized void removeObjectListener( ObjectListener l ) {
        objectListeners.remove( l );
    }

    private synchronized void fireObjectEvent(Object source, Object instance) {
        ObjectEvent oe = new ObjectEvent(source, instance);
        for(ObjectListener l : objectListeners) l.objectReceived(oe);
    }

    private synchronized void fireObjectEvent(Object source, String errorMessage) {
        ObjectEvent oe = new ObjectEvent(source, errorMessage);
        for(ObjectListener l : objectListeners) l.objectReceived(oe);
    }
}
