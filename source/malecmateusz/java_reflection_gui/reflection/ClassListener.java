package com.malecmateusz.java_reflection_gui.reflection;

import com.malecmateusz.java_reflection_gui.event.ClassEvent;

public interface ClassListener {
    void classesReceived(ClassEvent event);
}
