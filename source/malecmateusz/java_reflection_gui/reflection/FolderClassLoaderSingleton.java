package com.malecmateusz.java_reflection_gui.reflection;

import com.malecmateusz.java_reflection_gui.reflection.exception.ClassLoaderException;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FolderClassLoaderSingleton {
    private static FolderClassLoaderSingleton instance = null;

    private List<String> classNames;
    private List<Class> classes;

    private FolderClassLoaderSingleton(){
        this.classNames = new ArrayList<String>();
        this.classes = new ArrayList<Class>();
    }

    public static FolderClassLoaderSingleton getInstance(){
        if(instance == null) instance = new FolderClassLoaderSingleton();
        return instance;
    }

    private void listFilesForFolder(final File folder, String prefix) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry, prefix + fileEntry.getName() + ".");
                System.out.println("Nazwa folderu " + fileEntry.getName());
            } else if(fileEntry.getName().endsWith(".class")) {
                String entryName = prefix + fileEntry.getName();
                System.out.println("Odnaleziono " + entryName);
                classNames.add(entryName);
            }
        }
    }

    public void loadClasses(String path) throws ClassLoaderException {
        classes.clear();
        classNames.clear();
        try {
            final File folder = new File(path);
            listFilesForFolder(folder, "");
            URL[] urls = {folder.toURI().toURL()};
            URLClassLoader cl = URLClassLoader.newInstance(urls);
            for (String className : classNames) {
                className = className.substring(0, className.length() - 6);
                Class c = cl.loadClass(className);
                classes.add(c);
                System.out.println("Załadowano klasę " + className);
            }
        } catch (MalformedURLException | ClassNotFoundException | NullPointerException e) {
            Logger.getGlobal().severe(e.getMessage());
            throw new ClassLoaderException("Nie udało się załadować plików z podanej ścieżki!");
        }
    }

    public List<Constructor> getConstructors(Class clazz){
        Constructor[] constructors = clazz.getDeclaredConstructors();
        List<Constructor> conList = new ArrayList<Constructor>();
        for(Constructor c : constructors) conList.add(c);
        return conList;
    }

    public List<Class> getLoadedClasses(){
        return classes;
    }

    public List<String> getClassNames(){
        return classNames;
    }

    public List<String> getConstructorsNames(String className){
        className = className.substring(0, className.length() - 6);
        Class foundClass = null;
        for(Class c : classes){
            if(c.getName().equals(className)){
                foundClass = c;
                break;
            }
        }
        List<String> retList = new ArrayList<String>();
        if(foundClass != null){
           Constructor[] constructors = foundClass.getDeclaredConstructors();
           for(Constructor c : constructors){
               String fullDesc = "";
               String name = c.getName();
               String modifiers = Modifier.toString(c.getModifiers());
               if(modifiers.length() > 0) fullDesc += modifiers + " ";
               fullDesc += name + "(";
               Class[] paramTypes = c.getParameterTypes();
               for(int i=0;i<paramTypes.length;i++){
                   if (i > 0) fullDesc += ", ";
                  fullDesc += paramTypes[i].getName();
               }
               fullDesc += ");";
               retList.add(fullDesc);
           }
        }
        return retList;
    }

    public Object createInstanceWithParameters(Constructor constructor, String parameters) throws ClassLoaderException{
        Object newObject = null;
        String[] values = parameters.split(";");
        Class[] paramClasses = constructor.getParameterTypes();
        Object[] objs = new Object[paramClasses.length];
        for(int i=0;i<paramClasses.length;i++){
            try {
                if (paramClasses[i].equals(String.class)) objs[i] = values[i];
                else if (paramClasses[i].equals(int.class) || paramClasses[i].equals(Integer.class)) objs[i] = new Integer(values[i]);
                else if (paramClasses[i].equals(double.class) || paramClasses[i].equals(Double.class)) objs[i] = new Double(values[i]);
            } catch (Exception e2){
                Logger.getGlobal().severe(e2.getMessage());
                throw new ClassLoaderException("Nie udało się utworzyć obiektu!\nBłędny format parametrów.");
            }
        }
        constructor.setAccessible(true);
        try {
            newObject = constructor.newInstance(objs);
        }  catch(Exception e1){
            Logger.getGlobal().severe(e1.getMessage());
            throw new ClassLoaderException("Nie udało się utworzyć obiektu!\nBłąd przy wywoływaniu konstruktora.");
        }
        return newObject;
    }


}
