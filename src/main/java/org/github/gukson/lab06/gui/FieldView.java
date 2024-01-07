package org.github.gukson.lab06.gui;

import javax.swing.*;

public class FieldView {
    private JLabel[] labels;

    public FieldView(JLabel one,JLabel two,JLabel three,JLabel four) {
        this.labels = new JLabel[4];
        labels[0] = one;
        labels[1] = two;
        labels[2] = three;
        labels[3] = four;
    }

    public JLabel[] getLabels() {
        return labels;
    }
}
