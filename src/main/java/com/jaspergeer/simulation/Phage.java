package com.jaspergeer.simulation;

import javafx.scene.paint.Color;

import java.util.Objects;
import java.util.Random;

/**
 * Phages contain a
 */

public class Phage extends Genetic {

    private static final int DIVIDE_RANGE = 5;

    private static final int RGB_MASK = 0xff;

    private static final double OPACITY = 0.2;

    private static final int SURVIVE_ODDS = 100;

    private int divideCounter;

    private final int counterGene;

    private final int counterMask;

    public Phage(int genome, int initEnergy, Position initPos) {
        super(genome, initEnergy, initPos);
        divideCounter = 0;
        Random rand = new Random();
        counterMask = 0xf >>> rand.nextInt(29);
        counterGene = genome & counterMask;
    }

    public Phage(Phage b) {
        super(b);
        divideCounter = b.divideCounter;
        counterGene = b.counterGene;
        counterMask = b.counterMask;
    }

    /**
     * Phages float around the board 1 square at a time and do not die from lack of energy
     */
    @Override
    public void onUpdate(int temperature) {
        Random rand = new Random();
        Position pos = getPosition();

        int moveAmount = 1;

        if (rand.nextBoolean()) {
            moveAmount = -moveAmount;
        }
        if (rand.nextBoolean()) {
            pos.adjust(moveAmount, 0);
        } else {
            pos.adjust(0, moveAmount);
        }
        setPosition(pos);
    }

    @Override
    public int getCombatPower(Genetic other) {
        int otherGenome = other.getGenome();
        if ((otherGenome & counterMask) == counterGene || (~otherGenome & counterMask) == counterGene) {
            return 0;
        } else {
            Random rand = new Random();
            if (rand.nextInt(SURVIVE_ODDS) == 1) {
                return 0;
            }
            divideCounter = other.getEnergy();
            return 9999;
        }
    }

    @Override
    public Genetic getDivide(int mutateChance) {
        if (divideCounter > 0) {
            Random rand = new Random();
            Phage child = new Phage(this);
            divideCounter--;
            if (mutateChance > rand.nextInt(256)) {
                child.mutate();
                child.setPosition(child.getPosition().getResultOf(rand.nextInt(DIVIDE_RANGE) - 2,
                        rand.nextInt(DIVIDE_RANGE) - 2));
            }
            return child;
        }
        return null;
    }

    @Override
    public void onEat(int foodType, int amount) { }

    @Override
    public int getEatMax() { return 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Virus #").append(Objects.hash(this)).append("\n");
        sb.append("===Counter Adaptation===").append("\n");
        sb.append(String.format("%32s", Integer.toBinaryString(getGenome())).replace(' ', '0'))
                .append("\n");
        return sb.toString();
    }

    @Override
    public Color getColor() {
        int red = getGenome() >>> 16 & RGB_MASK;
        int green = getGenome() >>> 8 & RGB_MASK;
        int blue = getGenome() & RGB_MASK;
        return Color.rgb(red, green, blue, OPACITY);
    }

    /**
     * Phages do not contribute to population
     */
    public int getPopContribution() {
        return 0;
    }
}
