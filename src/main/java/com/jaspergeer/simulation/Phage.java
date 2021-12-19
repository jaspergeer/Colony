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

    private static final int COST_PER_CHILD = 16;

    private static final int GENE_MASK = 0x7;

    private int divideCounter;

    private int resistGenePos;

    public Phage(int genome, int initEnergy, Position initPos) {
        super(genome, initEnergy, initPos);
        divideCounter = 0;
        Random rand = new Random();
        resistGenePos = rand.nextInt(29);
        setEnergy(0);
    }

    public Phage(Phage b) {
        super(b);
        divideCounter = b.divideCounter;
        resistGenePos = b.resistGenePos;
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
    public void mutate() {
        super.mutate();
        /* When phages mutate the gene they are weak to can change position */
        Random rand = new Random();
        if (rand.nextInt(10) == 1) {
            resistGenePos += rand.nextInt(3) - 1;
            resistGenePos = resistGenePos % 29;
        }
    }

    @Override
    public int getCombatPower(Genetic other) {
        if (other.getTypeID() == 0) {
            return 0;
        }
        int otherGenome = other.getGenome();
        int mask = GENE_MASK << resistGenePos;
        int resistGene = getGenome() & mask;
        if ((otherGenome & mask) == resistGene || (~otherGenome & mask) == resistGene) {
            return 0;
        } else {
            Random rand = new Random();
            if (rand.nextInt(SURVIVE_ODDS) == 1) {
                return 0;
            }
            divideCounter = other.getEnergy() / COST_PER_CHILD + 1;
            return 9999;
        }
    }

    @Override
    public Genetic getDivide(int mutateChance) {
        if (divideCounter > 0) {
            Random rand = new Random();
            Phage child = new Phage(this);
            divideCounter--;
            /* The chance of a phage mutating is heavily reduced compared to bacteria */
            if (mutateChance > rand.nextInt(4096)) {
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
        int mask = GENE_MASK << resistGenePos;
        StringBuilder sb = new StringBuilder();
        sb.append("Virus #").append(Objects.hash(this)).append("\n");
        sb.append("===Weakness===").append("\n");
        sb.append("Gene ").append(String.format("%3s", Integer.toBinaryString(getGenome() & mask >>> resistGenePos))
                .replace(' ', '0')).append(" at Position ").append(resistGenePos).append("\n");
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
     * Phages have id 0
     */
    public int getTypeID() {
        return 0;
    }
}
