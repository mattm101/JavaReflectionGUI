package com.malecmateusz.java_reflection_gui.reflection.exception;

public class ClassLoaderException extends RuntimeException{
    private String errMessage;

    public ClassLoaderException(String errMesage){
        super();
        this.errMessage = errMesage;
    }
    public String getErrMessage(){return errMessage;}
}
