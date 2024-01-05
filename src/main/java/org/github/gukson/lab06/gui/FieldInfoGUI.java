package org.github.gukson.lab06.gui;

import org.github.gukson.lab06.model.Field;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FieldInfoGUI extends JFrame {
    private Field field;
    private JPanel contentPane;
    private JLabel[] quaters, ages, numbs;
    private JComboBox comboBox;
    private JLabel waitingPlantLabel, waintingPLantText;

    public FieldInfoGUI(Field field) {
        this.field = field;
        this.quaters = new JLabel[4];
        this.ages = new JLabel[4];
        this.numbs = new JLabel[4];


        setBounds(100, 100, 376, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        setup();
        refreshPlantsData();

        comboBox = new JComboBox();
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"carrot", "potato", "salad", "tomato"}));
        comboBox.setBounds(281, 26, 89, 27);
        contentPane.add(comboBox);

        JButton addNewPlantButton = new JButton();
        addNewPlantButton.setIcon(new ImageIcon("./src/main/resources/data/add.png"));
        addNewPlantButton.setBounds(320, 120, 40, 40);
        addNewPlantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (field.getSeedsQueue()[0] == null) {
                    field.setPlantInQueue((String) comboBox.getSelectedItem());
                    System.out.println(field.getSeedsQueue()[0]);
                    refreshPlantsData();
                }
            }
        });
        contentPane.add(addNewPlantButton);

        JLabel FieldInfo = new JLabel("Field (" + field.getX() + "," + field.getY() + ")");
        FieldInfo.setBounds(159, 6, 80, 16);
        contentPane.add(FieldInfo);
    }


    private void refreshPlantsData() {
        for (int x = 0; x < field.getPlants().length; x++) {

            if (field.getPlants()[x] == null) {
                ages[x].setText("Age: 0");
                quaters[x].setIcon(new ImageIcon("./src/main/resources/data/empty_quater.png"));
            } else {
                ages[x].setText("Age: " + String.valueOf(field.getPlants()[x].getAge()));
                switch (field.getPlants()[x].getName()) {
                    case "carrot":
                        quaters[x].setIcon(new ImageIcon("./src/main/resources/data/carrot.png"));
                        break;
                    case "potato":
                        quaters[x].setIcon(new ImageIcon("./src/main/resources/data/potato.png"));
                        break;
                    case "salad":
                        quaters[x].setIcon(new ImageIcon("./src/main/resources/data/cabbage.png"));
                        break;
                    case "tomato":
                        quaters[x].setIcon(new ImageIcon("./src/main/resources/data/tomato.png"));
                        break;
                }
            }

            if(field.getSeedsQueue()[0] == null){
                waitingPlantLabel.setIcon(new ImageIcon("./src/main/resources/data/empty_quater.png"));
            }
            else {
                switch (field.getSeedsQueue()[0].getName()){
                    case "carrot":
                        waitingPlantLabel.setIcon(new ImageIcon("./src/main/resources/data/carrot.png"));
                        break;
                    case "potato":
                        waitingPlantLabel.setIcon(new ImageIcon("./src/main/resources/data/potato.png"));
                        break;
                    case "salad":
                        waitingPlantLabel.setIcon(new ImageIcon("./src/main/resources/data/cabbage.png"));
                        break;
                    case "tomato":
                        waitingPlantLabel.setIcon(new ImageIcon("./src/main/resources/data/tomato.png"));
                        break;
                }
            }

        }
    }

    private void setup() {
        for (int x = 0; x < 4; x++) {
            quaters[x] = new JLabel();
            quaters[x].setIcon(new ImageIcon("./src/main/resources/data/empty_quater.png"));
            quaters[x].setBounds(26 + x * 50, 90, 40, 40);
            contentPane.add(quaters[x]);


            ages[x] = new JLabel();
            ages[x].setBounds(26 + x * 50, 130, 40, 15);
            ages[x].setHorizontalAlignment(SwingConstants.CENTER);
            contentPane.add(ages[x]);

            numbs[x] = new JLabel(String.valueOf(x + 1));
            numbs[x].setBounds(26 + x * 50, 75, 40, 15);
            numbs[x].setHorizontalAlignment(SwingConstants.CENTER);
            contentPane.add(numbs[x]);

        }
        waitingPlantLabel = new JLabel();
        waitingPlantLabel.setIcon(new ImageIcon("./src/main/resources/data/empty_quater.png"));
        waitingPlantLabel.setBounds(26 , 26, 40, 40);
        contentPane.add(waitingPlantLabel);

        waintingPLantText = new JLabel("waiting");
        waintingPLantText.setBounds(21 , 11, 50, 15);
        waintingPLantText.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(waintingPLantText);

        contentPane.repaint();
        contentPane.revalidate();
    }

}
