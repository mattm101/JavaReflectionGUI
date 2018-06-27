package com.malecmateusz.java_reflection_gui;

import com.malecmateusz.java_reflection_gui.gui.AppFrame;

import java.awt.*;

/**
 *
 *
 */
public class App {

    public static void main( String[] args ) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AppFrame();
            }
        });

    }
}
