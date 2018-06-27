package com.malecmateusz.java_reflection_gui.gui;

import com.malecmateusz.java_reflection_gui.reflection.ClassLoaderController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HeaderPanel extends JPanel implements ActionListener{

    private ClassLoaderController clc;

    private JTextField classPathField;
    private JButton classPathButton;
    private JLabel label;

    public HeaderPanel(ClassLoaderController clc){
        this.clc = clc;

        classPathField = new JTextField(20);
        classPathButton = new JButton("Ustaw ścieżkę");
        label = new JLabel("Podaj ścieżkę do folderu: ");
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setMinimumSize(new Dimension(525,10));
        this.add(label);
        this.add(classPathField);
        this.add(classPathButton);

        classPathButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        clc.loadClasses(classPathField.getText());
    }


}
