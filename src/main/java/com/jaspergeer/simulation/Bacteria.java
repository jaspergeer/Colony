package com.jaspergeer.simulation;

import javafx.scene.paint.Color;

import java.util.Objects;
import java.util.Random;

public class Bacteria extends Genetic {

    private final int MAX_POSSIBLE_AGE = 128;
    private final int RGB_MASK = 0xff;

    private int speed;
    private int metabolism;
    private int energyToDivide;
    private int eatAmount;
    private int maxAge;
    private int age;
    private int maxTemp;
    private int minTemp;
    private int combatPower;

    /**
     * We give names to each index in the genome for readability and easier access
     */
    private enum Gene {
        FLAGELLA_STRENGTH(0),
        COLD_RESIST(1),
        HOT_RESIST(2),
        DIVIDE_THRESHOLD(3),
        WALL_THICKNESS(4),
        FOOD_TYPE(5),
        EAT_AMOUNT(6),
        COMBAT_ABILITY(7);

        final int value;
        Gene(int value) {
            this.value = value;
        }
    }

    public Bacteria(int genome, int initEnergy, Position initPos) {
        super(genome, initEnergy, initPos);
        age = 0;
        updateTraits();
    }

    public Bacteria(Bacteria b) {
        super(b);
        b.updateTraits();
    }

    @Override
    public void onUpdate(int temperature) {
        if (getEnergy() == 0 || age > maxAge) {
            setDead(true);
            return;
        }
        Random rand = new Random();
        Position pos = getPosition();

        int yShift = rand.nextInt(speed + 1);;
        int xShift = rand.nextInt(speed + 1);;
        if (rand.nextBoolean()) {
            yShift = -yShift;
        }
        if (rand.nextBoolean()) {
            xShift = -xShift;
        }

        pos.adjust(yShift, xShift);
        setPosition(pos);
        adjustEnergy(-metabolism);

        /* lose extra energy if we are outside of our temperature range */
        if (temperature > maxTemp) {
            adjustEnergy(-2 * (temperature - maxTemp));
        } else if (temperature < minTemp) {
            adjustEnergy(2 * (temperature - minTemp));
        }

        age++;
    }

    @Override
    public int getCombatPower(Genetic other) {
        updateTraits();
        return combatPower;
    }

    @Override
    public Genetic getDivide(int mutateChance) {
        updateTraits();
        /* only divide if this Bacteria has sufficient energy */
        if (getEnergy() >= energyToDivide) {
            Bacteria child = new Bacteria(this);
            child.setEnergy(getEnergy() / 2);
            setEnergy(getEnergy() / 2);
            Random rand = new Random();
            if (mutateChance > rand.nextInt(256)) {
                child.mutate();
            }
            return child;
        }
        return null;
    }

    @Override
    public void onEat(int foodType, int amount) {
        updateTraits();
        adjustEnergy(amount / (Math.abs(foodType - getGeneVal(Gene.FOOD_TYPE.value)) + 1));
    }

    @Override
    public int getEatMax() {
        updateTraits();
        return eatAmount;
    }

    @Override
    public String toString() {
        updateTraits();
        StringBuilder sb = new StringBuilder();
        sb.append("Bacteria #").append(Objects.hash(this)).append("\n");
        sb.append(String.format("%32s", Integer.toBinaryString(getGenome())).replace(' ', '0'))
                .append("\n");
        sb.append("===Traits===").append("\n");
        sb.append("Preferred Food: ").append(getGeneVal(Gene.FOOD_TYPE.value)).append("\n");
        sb.append("Metabolic Rate: ").append(metabolism).append("\n");
        sb.append("Combat Power: ").append(combatPower).append("\n");
        sb.append("Speed: ").append(speed).append("\n");
        sb.append("Appetite: ").append(eatAmount).append("\n");
        sb.append("Division Threshold: ").append(energyToDivide).append("\n");
        sb.append("Lifespan: ").append(maxAge).append("\n");
        sb.append("Cold Resistance: ").append(-minTemp).append("\n");
        sb.append("Heat Resistance: ").append(maxTemp).append("\n");
        sb.append("===State===").append("\n");
        sb.append("Energy: ").append(getEnergy()).append("\n");
        sb.append("Age: ").append(age).append("\n");
        return sb.toString();
    }

    @Override
    public Color getColor() {
        updateTraits();
        int red = getGenome() >>> 16 & RGB_MASK;
        int green = getGenome() >>> 8 & RGB_MASK;
        int blue = getGenome() & RGB_MASK;
        double opacity = 0.7 * ((double) combatPower / 450) + 0.3;
        return Color.rgb(red, green, blue, opacity);
    }

    /**
     * Update traits based on genome
     */
    private void updateTraits() {
        speed = getGeneVal(Gene.FLAGELLA_STRENGTH.value) / 4 + 1;
        metabolism = (getGeneVal(Gene.FLAGELLA_STRENGTH.value) +
                getGeneVal(Gene.COLD_RESIST.value) +
                getGeneVal(Gene.HOT_RESIST.value) +
                getGeneVal(Gene.WALL_THICKNESS.value) +
                getGeneVal(Gene.EAT_AMOUNT.value) +
                getGeneVal(Gene.COLD_RESIST.value) +
                getGeneVal(Gene.HOT_RESIST.value) +
                2 * getGeneVal(Gene.COMBAT_ABILITY.value)) / 9 + 1;
        energyToDivide = (getGeneVal(Gene.DIVIDE_THRESHOLD.value) + 1) * 4;
        eatAmount = getGeneVal(Gene.EAT_AMOUNT.value) * 4;
        maxAge = MAX_POSSIBLE_AGE / (metabolism + speed);
        maxTemp = getGeneVal(Gene.HOT_RESIST.value) + 1;
        minTemp = -getGeneVal(Gene.COLD_RESIST.value) - 1;
        combatPower = getGeneVal(Gene.COMBAT_ABILITY.value) *
                (speed * getGeneVal(Gene.WALL_THICKNESS.value)) / 2;
    }

}
