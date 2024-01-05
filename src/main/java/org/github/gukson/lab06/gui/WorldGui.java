package org.github.gukson.lab06.gui;

import org.github.gukson.lab06.model.Field;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WorldGui extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane, machinePanel, panel;
    private JButton[][] buttons;
    private Field[][] fields;

    public WorldGui(Field[][] fields) {
        this.fields = fields;
        this.buttons = new JButton[5][5];
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 5));
        panel.setBounds(50, 25, 500, 500);

        machinePanel = new JPanel();
        machinePanel.setBounds(0, 0, 700, 600);
        machinePanel.setVisible(true);
        machinePanel.setOpaque(false);
        machinePanel.setLayout(null);
        contentPane.add(machinePanel);

        generateFieldButton();

        contentPane.add(panel);
    }

    private JButton generateFieldButton() {
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                JButton temp = new JButton("(" + y + "," + x + ")");
                temp.addActionListener(new ButtonClickListener());
                panel.add(temp);
                buttons[y][x] = temp;
            }
        }
        return null;
    }

    @Override
    public JPanel getContentPane() {
        return contentPane;
    }

    public JPanel getMachinePanel() {
        return machinePanel;
    }

    public JLabel newHarvester(Integer id) {
        JLabel label = new JLabel("");
        label.setBounds(71 + id * 100, 25, 57, 100);
        label.setIcon(new ImageIcon("./src/main/resources/data/harvester-down.png"));
        machinePanel.add(label);
        label.setVisible(true);
        machinePanel.revalidate();
        machinePanel.repaint();
        return label;
    }

    public JLabel newSeeder(Integer id) {
        JLabel label = new JLabel("");
        label.setBounds(50, 50 + id * 100, 100, 57);
        label.setIcon(new ImageIcon("./src/main/resources/data/seeder-right.png"));
        machinePanel.add(label);
        label.setVisible(true);
        machinePanel.revalidate();
        machinePanel.repaint();
        return label;
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            int row = -1;
            int col = -1;

            // Szukanie przycisku w siatce
            for (int i = 0; i < buttons.length; i++) {
                for (int j = 0; j < buttons[i].length; j++) {
                    if (buttons[j][i] == clickedButton) {
                        row = i;
                        col = j;
                        break;
                    }
                }
            }

            if (row != -1 && col != -1) {
                FieldInfoGUI fieldInfoGUI = new FieldInfoGUI(fields[col][row]);
                fieldInfoGUI.setVisible(true);
            }
        }
    }
}
