package org.github.gukson.lab06.model.world;

import org.github.gukson.lab06.model.Field;
import org.github.gukson.lab06.model.Plant;

public class Time implements Runnable {

    private Field[][] fields;

    public Time(Field[][] fields) {
        this.fields = fields;
    }

    @Override
    public void run() {
        while (true) {
            for (int y = 0; y < 5; y++) {
                for (int x = 0; x < 5; x++) {
                    for(Plant p : fields[y][x].getPlants()){
                        if(p != null){
                            p.grow();
                        }
                    }
                }
            }
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
