package com.malecmateusz.java_reflection_gui.gui;

import com.malecmateusz.java_reflection_gui.reflection.ClassLoaderController;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {
    private HeaderPanel header;
    private CenterPanel center;
    private ClassLoaderController clc;
    public AppFrame() {
        super("Obiektowywoływacz 3000");
        clc = ClassLoaderController.getInstance();

        header = new HeaderPanel(clc);
        center = new CenterPanel(clc);
        clc.addClassListener(center);
        clc.addConstructorListener(center);
        clc.addObjectListener(center);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(525,550);
        setMinimumSize(new Dimension(525, 350));
        setLocation(100,100);

        setResizable(true);
        setVisible(true);
        this.add(header, BorderLayout.NORTH);
        this.add(center, BorderLayout.CENTER);
        //this.pack();
    }
}
