package com.jaspergeer.simulation;

import javafx.scene.paint.Color;

import java.util.Objects;
import java.util.Random;

/**
 * Phages will kill a bacteria 90% of the time unless the bacteria has a specific mutation that the phage is weak to.
 * In this case the phage will lose the encounter.
 */

public class Phage extends Genetic {

    private static final int HEAT_RESIST = 12;

    private static final int DIVIDE_RANGE = 5;

    private static final int RGB_MASK = 0xff;

    private static final double OPACITY = 0.2;

    private static final int SURVIVE_ODDS = 10;

    private static final int COST_PER_CHILD = 2;

    private static final int GENE_MASK = 0x7;

    private static final int GENE_SHIFT_MASK = 0xf8;

    private static final int MAX_RESIST_GENE_POS = 29;

    private int divideCounter;

    public Phage(int genome, int initEnergy, Position initPos) {
        super(genome, initEnergy, initPos);
        divideCounter = 0;
        setEnergy(0);
    }

    public Phage(Phage b) {
        super(b);
        divideCounter = b.divideCounter;
        setEnergy(0);
    }

    /**
     * Phages float around the board 1 square at a time and do not die from lack of energy
     */
    @Override
    public void onUpdate(int temperature) {
        Random rand = new Random();
        if (temperature > HEAT_RESIST) {
            if (rand.nextBoolean()) {
                setDead(true);
                return;
            }
        }

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
        if (other.getTypeID() == 0) {
            return 0;
        }
        int otherGenome = other.getGenome();
        int numAdaptations = 0;
        for (int i = 0; i < 4; i++) {
            int mask = GENE_MASK << getResistGenePos(i);
            if ((otherGenome & mask) == (getResistGene(i) << getResistGenePos(i)) ||
                    (~otherGenome & mask) == (getResistGene(i) << getResistGenePos(i))) {
                numAdaptations++;
            }
        }
        Random rand = new Random();
        if (rand.nextInt(4) < numAdaptations) {
            return 0;
        }
        divideCounter = other.getEnergy() / COST_PER_CHILD + 1;
        return 9999;
    }

    @Override
    public Genetic getDivide(int mutateChance) {
        if (divideCounter > 0) {
            Random rand = new Random();
            Phage child = new Phage(this);
            divideCounter--;
            if (mutateChance > rand.nextInt(256)) {
                child.mutate();
                child.divideCounter = 0;
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
        sb.append("===Genome===").append("\n");
        sb.append(String.format("%32s", Integer.toBinaryString(getGenome()))
                        .replace(' ', '0')).append("\n");
        sb.append("===Weaknesses===").append("\n");
        for (int i = 0; i < 4; i++) {
            sb.append("Gene ").append(String.format("%3s", Integer.toBinaryString(getResistGene(i) & GENE_MASK))
                    .replace(' ', '0')).append(" at Position ").append(getResistGenePos(i))
                    .append("\n");
        }
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
     * Gets the sequence of one of the 4 genes that make a bacteria resistant to this phage
     * @param i index of the gene (0-3)
     * @return the ith resistance gene sequence
     */
    int getResistGene(int i) {
        return (getGenome() & (GENE_MASK << i * 8)) >>> (i * 8);
    }

    /**
     * Gets the position of one of the 4 genes that make a bacteria resistant to this phage
     * @param i index of the gene (0-3)
     * @return the ith resistance gene position
     */
    int getResistGenePos(int i) {
        return ((getGenome() & (GENE_SHIFT_MASK << i * 8)) >>> (3 + i * 8)) % MAX_RESIST_GENE_POS;
    }

    /**
     * Phages have id 0
     */
    public int getTypeID() {
        return 0;
    }
}
