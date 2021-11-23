package com.jaspergeer.simulation;

import javafx.scene.paint.Color;

import java.util.Random;

public abstract class Genetic {

    private final static int GENE_MASK = 0x0000000f;
    private final static int GENOME_LENGTH = 32;

    private int genome;
    private boolean isDead;
    private int energy;
    private Position position;

    public Genetic(int genome, int initEnergy, Position initPos) {
        this.genome = genome;
        energy = initEnergy;
        position = initPos;
    }

    /**
     * copy constructor
     */
    public Genetic(Genetic other) {
        this.genome = other.genome;
        this.isDead = other.isDead;
        this.energy = other.energy;
        this.position = other.getPosition();
    }

    /**
     * Get the value of a 4-bit gene at a given index in the genome
     * @param index the index from 0-15 of the gene
     * @return the value of the gene
     */
    public int getGeneVal(int index) {
        int mask = GENE_MASK << (index * 4);
        return (genome & mask) >>> (index * 4);
    }

    public void setGeneVal(int index, int value) {
        int mask = ~(GENE_MASK << (index * 4));
        genome = genome & mask;
        value = value & GENE_MASK;
        genome = genome | (value << (index * 4));
    }

    /**
     * Called by the java.simulation when a Genetic moves into a space occupied by another Genetic. Determines the survivor
     * based on the collision factors of both Genetics
     * @param other the Genetic this Genetic has collided with
     * @return Genetic which survives the engagement
     */
    public Genetic onCollide(Genetic other) {
        Random rand = new Random();
        if (rand.nextInt(other.getCollideFactor(other)) > rand.nextInt(this.getCollideFactor(other))) {
            /* the winner of the engagement absorbs the losers energy */
            other.energy += Utils.clamp(energy, 0, Integer.MAX_VALUE);
            isDead = true;
            return other;
        }
        energy += Utils.clamp(other.energy, 0, Integer.MAX_VALUE);
        other.isDead = true;
        return this;
    }

    /**
     * Make a change to the genome in one of three randomly determined ways
     */
    public void mutate() {
        Random rand = new Random();
        int mask;
        switch (rand.nextInt(3)) {
            /* replacement mutation */
            case 0:
                mask = 0x1 << rand.nextInt(GENOME_LENGTH);
                genome = genome ^ mask;
                break;
            /* inversion mutation */
            case 1:
                mask = 0xf << (rand.nextInt(GENOME_LENGTH / 4) * 4);
                genome = genome ^ mask;
                break;
            /* frame shift mutation */
            case 2:
                if (rand.nextBoolean()) {
                    genome = genome << 1;
                } else {
                    genome = genome >>> 1;
                }
                break;
        }
    }

    /* implemented setters and getters */

    public Position getPosition() { return new Position(position); }

    public void setPosition(Position p) { position = p; }

    public boolean isDead() { return isDead; }

    public void setDead(boolean isDead) { this.isDead = isDead; }

    public int getGenome() { return genome; }

    public int getEnergy() { return energy; }

    public void setEnergy(int amount) { energy = amount; }

    public void adjustEnergy(int amount) {
        energy = Utils.clamp(energy + amount, 0, Integer.MAX_VALUE);
    }

    /* subclasses must implement these */

    /**
     * Update is called every cycle by the java.simulation
     * @param temperature the current temperature of the java.simulation environment
     */
    public abstract void onUpdate(int temperature);

    /**
     * Given a Genetic this Genetic is colliding with, returns a 'collision factor' used to determine which one will
     * survive
     * @param other Genetic this Genetic is colliding with
     * @return Collision factor for this Genetic
     */
    public abstract int getCollideFactor(Genetic other);

    /**
     * Returns an 'offspring' Genetic with genome created from this Genetic
     * @param mutateChance integer from 0-256 representing probability of mutation
     * @return offspring Genetic, null if Genetic cannot currently divide
     */
    public abstract Genetic getDivide(int mutateChance);

    /**
     * 'Feeds' a Genetic a certain quantity and type of food.
     * @param foodType type of food
     * @param amount amount of food
     */
    public abstract void onEat(int foodType, int amount);

    /**
     * @return the maximum amount of food this genetic eats per java.simulation cycle
     */
    public abstract int getEatMax();

    /**
     * @return a string containing information about this Genetic
     */
    public abstract String toString();

    /**
     * @return the color of this Genetic when it is displayed on screen
     */
    public abstract Color getColor();
}
