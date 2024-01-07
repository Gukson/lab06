package org.github.gukson.lab06.model.world;

import org.github.gukson.lab06.gui.FieldView;
import org.github.gukson.lab06.model.Field;
import org.github.gukson.lab06.model.Plant;

import javax.swing.*;

public class Time implements Runnable {

    private Field[][] fields;
    private FieldView[][] fieldInfo;
    private JPanel contantPane;

    public Time(Field[][] fields, FieldView[][] fieldInfo, JPanel contentPane) {
        this.fields = fields;
        this.fieldInfo = fieldInfo;
        this.contantPane = contentPane;
    }

    @Override
    public void run() {
        while (true) {
            for (int y = 0; y < 5; y++) {
                for (int x = 0; x < 5; x++) {
                    for (int p = 0; p < 4; p++) {
                        Plant plant =  fields[y][x].getPlants()[p];
                        if (plant != null) {
                            plant.grow();
                            if(plant.getAge() < 4){
                                fieldInfo[y][x].getLabels()[p].setIcon(new ImageIcon("./src/main/resources/data/vegetables/" + plant.getName() + "1.png"));
                            } else if (plant.getAge() < 7) {
                                fieldInfo[y][x].getLabels()[p].setIcon(new ImageIcon("./src/main/resources/data/vegetables/" + plant.getName() + "2.png"));
                            } else if (plant.getAge() < 10) {
                                fieldInfo[y][x].getLabels()[p].setIcon(new ImageIcon("./src/main/resources/data/vegetables/" + plant.getName() + "3.png"));
                            }else {
                                fieldInfo[y][x].getLabels()[p].setIcon(new ImageIcon("./src/main/resources/data/vegetables/" + plant.getName() + "4.png"));
                            }
                            contantPane.repaint();
                            contantPane.revalidate();
                        }

                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
