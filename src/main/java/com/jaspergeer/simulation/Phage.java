package com.jaspergeer.simulation;

import javafx.scene.paint.Color;

import java.util.Random;

/**
 * TODO - Implement Phage as a 'virus' that can be released by the user
 */

public class Phage extends Genetic {

    private int divideCounter;

    public Phage(int genome, int initEnergy, Position initPos) {
        super(genome, initEnergy, initPos);
        divideCounter = 0;
    }

    @Override
    public void onUpdate(int temperature) {
        Random rand = new Random();
        Position pos = getPosition();

        int yShift = 1;
        int xShift = 1;

        if (rand.nextBoolean()) {
            yShift = -yShift;
        }
        if (rand.nextBoolean()) {
            xShift = -xShift;
        }

        pos.adjust(yShift, xShift);
        setPosition(pos);
    }

    @Override
    public int getCombatPower(Genetic other) {
        Random rand = new Random();
        return rand.nextInt(999);
    }

    @Override
    public Genetic getDivide(int mutateChance) {
        return null;
    }

    @Override
    public void onEat(int foodType, int amount) {
    }

    @Override
    public int getEatMax() {
        return 0;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public Color getColor() {
        return Color.BEIGE;
    }
}
