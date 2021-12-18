package com.jaspergeer.simulation;

import javafx.scene.paint.Color;

import java.util.Random;

/**
 * TODO - Implement Phage as a 'virus' that can be released by the user
 */

public class Phage extends Genetic {

    public Phage(int genome, int initEnergy, Position initPos) {
        super(genome, initEnergy, initPos);
    }

    @Override
    public void onUpdate(int temperature) {

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
