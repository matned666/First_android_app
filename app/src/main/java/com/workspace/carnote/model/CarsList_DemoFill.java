package com.workspace.carnote.model;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.Date;

public class CarsList_DemoFill {
    public static void fill(ArrayList<AutoData> cars, int amountOfCars, int maxAmountOfRecords) {

        for(int i = 0; i < amountOfCars; i++ ) {
            String plates =
                    (RandomStringUtils.random(2, true, false) +
                            " " +
                            RandomStringUtils.random(5, true, true)).toUpperCase();

            Car car = randomCar();
            cars.add(new AutoData.Builder()
                    .brand(car.brandA)
                    .model(car.modelA)
                    .color(randomColor())
                    .plates(plates)
                    .build());
            int mileage = 0;
            int records = (int) (Math.random()*maxAmountOfRecords);
            System.out.println("Number of RECORDS: "+records);
            for(int j= 0; j < records; j++) {
                cars.get(i).getTankUpRecord().add(0,
                        new TankUpRecord.Builder()
                                .tankUpDate(new Date())
                                .mileage(mileage)
                                .costInPLN(180)
                                .tankedUpGasLiters(45)
                                .build());
                mileage += 600;
            }
        }
    }

    private static String randomColor(){
        int chooser = (int) (Math.random()*5);
        switch(chooser){
            case 1: return "Red";
            case 2: return "White";
            case 3: return "Yellow";
            case 4: return "Blue";
            default: return "Black";
        }
    }

private static Car randomCar(){
        int chooser = (int) (Math.random()*15);
        switch(chooser){
            case 1: return new Car("Ford", "Mondeo");
            case 2: return new Car("Ford", "Transit");
            case 3: return new Car("Toyota", "Corolla");
            case 4: return new Car("Suzuki", "Swift");
            case 5: return new Car("Ford", "Mustang");
            case 6: return new Car("Honda", "Civic");
            case 7: return new Car("Honda", "Accord");
            case 8: return new Car("Mini", "Country");
            case 9: return new Car("Jeep", "Wrangler");
            case 10: return new Car("Dodge", "Challenger");
            case 11: return new Car("Renault", "Clio S");
            case 12: return new Car("Ford", "Focus S");
            case 13: return new Car("Dodge", "Viper");
            case 14: return new Car("Lamborghini", "Diablo");
            default: return new Car("Ford", "Turneo");
        }
    }
    private static class Car{
        String brandA;
        String modelA;

        private Car(String brandA, String modelA) {
            this.brandA = brandA;
            this.modelA = modelA;
        }
    }


}
