package com.malecmateusz.java_reflection_gui.gui;

import com.malecmateusz.java_reflection_gui.event.ClassEvent;
import com.malecmateusz.java_reflection_gui.event.ConstructorEvent;
import com.malecmateusz.java_reflection_gui.event.ObjectEvent;
import com.malecmateusz.java_reflection_gui.reflection.ClassListener;
import com.malecmateusz.java_reflection_gui.reflection.ClassLoaderController;
import com.malecmateusz.java_reflection_gui.reflection.ConstructorListener;
import com.malecmateusz.java_reflection_gui.reflection.ObjectListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;

public class CenterPanel extends JPanel implements ClassListener, ConstructorListener, ObjectListener {

    class NewInstanceListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            clc.createInstanceWithParameters(constructor, params.getText());
        }
    }

    class ConsListListener implements ListSelectionListener{
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting() == false) {

                if (constructorsList.getSelectedIndex() == -1) {
                    invokeButton.setEnabled(false);
                    System.out.println("Nie zaznaczono zadnego konstruktora");
                    repaint();

                } else {
                    System.out.println("Zaznaczono konstuktor " + constructorsList.getSelectedValue().toString());
                    constructor = (Constructor) constructorsList.getSelectedValue();
                    invokeButton.setEnabled(true);
                    it.repaint();
                }
            }
        }
    }

    class ClassListListener implements ListSelectionListener{
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting() == false) {

                if (classesList.getSelectedIndex() == -1) {
                    System.out.println("Nie zaznaczono zadnej klasy");

                } else {
                    Class clazz = (Class)classesList.getSelectedValue();
                    System.out.println("Zaznaczono klasę " + clazz.toString());
                    clc.loadConstructors(clazz);
                }
            }
        }
    }

    private ClassLoaderController clc;
    private CenterPanel it;
    private JList classesList;
    private DefaultListModel classNamesModel;
    private JScrollPane classesListScroller;
    JScrollPane constructorsListScroller;
    JScrollPane objScroller;
    private JList constructorsList;
    private JButton invokeButton;
    private Constructor constructor;
    private JTextField params;
    private JTextArea objDetails;
    private Object object;

    public CenterPanel(ClassLoaderController clc){
        it = this;
        this.clc = clc;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        objDetails = new JTextArea();
        objDetails.setEditable(false);
        classesList = new JList();
        params = new JTextField();
        constructorsList = new JList();
        invokeButton = new JButton("Wywołaj");
        invokeButton.setEnabled(false);
        classesList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        classesList.setLayoutOrientation(JList.VERTICAL);
        classesList.setVisibleRowCount(-1);
        classesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        classesList.addListSelectionListener(new ClassListListener());
        constructorsList.addListSelectionListener(new ConsListListener());
        constructorsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        constructorsList.setLayoutOrientation(JList.VERTICAL);
        constructorsList.setVisibleRowCount(-1);
        constructorsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        invokeButton.addActionListener(new NewInstanceListener());
        this.classesListScroller = new JScrollPane(classesList);
        objScroller = new JScrollPane(objDetails);
        this.constructorsListScroller = new JScrollPane(constructorsList);
        classesListScroller.setPreferredSize(new Dimension(250, 100));
        constructorsListScroller.setPreferredSize(new Dimension(250, 100));

        TitledBorder classBorder = new TitledBorder(
                new LineBorder(Color.black),
                "Lista klas",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION
        );

        TitledBorder consBorder = new TitledBorder(
                new LineBorder(Color.black),
                "Konstruktory",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION
        );

        TitledBorder paramsBorder = new TitledBorder(
                new LineBorder(Color.black),
                "Parametry rozdzielone średnikiem",
                TitledBorder.CENTER,
                TitledBorder.DEFAULT_POSITION
        );

        TitledBorder objBorder = new TitledBorder(
                new LineBorder(Color.black),
                "Pola utworzonego obiektu",
                TitledBorder.CENTER,
                TitledBorder.BELOW_TOP
        );
        classesListScroller.setBorder(classBorder);
        constructorsListScroller.setBorder(consBorder);
        params.setBorder(paramsBorder);
        objScroller.setBorder(objBorder);

        add(Box.createRigidArea(new Dimension(0,5)));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(classesListScroller);
        add(constructorsListScroller);
        add(Box.createRigidArea(new Dimension(0,5)));
        params.setMaximumSize(new Dimension(600000, 50));
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(params);
        buttonPane.add(invokeButton);
        add(buttonPane);
        add(objScroller);
    }

    public void objectReceived(ObjectEvent event){
        objDetails.setText(event.toString());
        repaint();
    }

    public void constructorsReceived(ConstructorEvent event){
        DefaultListModel constructorNamesModel = new DefaultListModel();
        for (Constructor name : event.getConstructors()) {
            constructorNamesModel.addElement(name);
        }
        constructorsList.setModel(constructorNamesModel);
    }


    public void classesReceived(ClassEvent event) {
        DefaultListModel classNamesModel = new DefaultListModel();
        if(event.getOpCode() == ClassEvent.CLEAR_LIST){
            classesList.setModel(classNamesModel);

        }
        else if(event.getOpCode() == ClassEvent.LOAD_LIST) {
            for (Class clazz : event.getClasses()) {
                classNamesModel.addElement(clazz);
            }
            classesList.setModel(classNamesModel);
            objDetails.setText("");
        }
        else if(event.getOpCode() == ClassEvent.ERROR){
            classesList.setModel(classNamesModel);
            constructorsList.setModel(classNamesModel);
            objDetails.setText(event.getErrMessage());
        }
        repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
    }
}
