/*!
 * Copyright 2021 Jasper Geer. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for license information.
 */

package com.jaspergeer.simulation;

import java.util.*;

public class Simulation {

    private static final int FREQUENCY_MAX = 256;

    private final int width;
    private final int height;
    private LinkedList<Genetic> entityList;
    private Tile[][] foodMap;
    private int radLevel;
    private int temperature;
    private int maxTileFood;
    private int bactPop;
    private int phagePop;

    /**
     * @param height            number of rows in the map
     * @param width             number of columns in the map
     * @param foodTileFrequency probability (0-256) of each generated tile being a food tile
     * @param initRadLevel      initial radiation level
     * @param initTemp          initial temperature
     * @param regenRateBias     center of food regen rate values
     * @param regenRateVariance range / 2 of food regen rate values
     * @param foodTypeBias      center of food type values
     * @param foodTypeVariance  range / 2 of food type values
     * @param maxFoodBias       center of maximum food values
     * @param maxFoodVariance   range / 2 of maximum food values
     */
    public Simulation(int height, int width, int foodTileFrequency, int initRadLevel,
                      int initTemp,
                      int regenRateBias, int regenRateVariance,
                      int foodTypeBias,
                      int foodTypeVariance, int maxFoodBias,
                      int maxFoodVariance) {

        this.radLevel = initRadLevel;
        Random rand = new Random();

        entityList = new LinkedList<>();

        temperature = initTemp;
        bactPop = 0;
        phagePop = 0;
        this.width = width;
        this.height = height;

        generateFoodMap(regenRateBias, regenRateVariance, foodTileFrequency, foodTypeBias,
                foodTypeVariance, maxFoodBias, maxFoodVariance);
    }

    /**
     * Update each Genetic on our list, looking for and resolving collisions if necessary, feeding
     * based on food Tile at current location, and updating the list with the surviving individuals
     */
    public void update() {
        bactPop = 0;
        phagePop = 0;
        Genetic[][] collisionMap = new Genetic[height][width];
        LinkedList<Genetic> newEntities = new LinkedList<>();

        /* process each Genetic on the list */
        while (!entityList.isEmpty()) {
            Genetic thisEntity = entityList.remove();
            if (!thisEntity.isDead()) {
                /* movement phase */
                thisEntity.onUpdate(temperature);
                Position pos = thisEntity.getPosition();
                /* look for a collision */
                if (collisionMap[pos.getY()][pos.getX()] != null) {
                    Genetic otherEntity = collisionMap[pos.getY()][pos.getX()];
                    if (thisEntity.onCollide(otherEntity) == thisEntity) {
                        newEntities.add(thisEntity);
                        collisionMap[pos.getY()][pos.getX()] = thisEntity;
                    }
                } else {
                    /* division phase */
                    collisionMap[pos.getY()][pos.getX()] = thisEntity;
                    Genetic child = thisEntity.getDivide(radLevel);
                    if (child != null) {
                        /* find a suitable location to place the child if possible */
                        Position dest = new Position(pos);
                        if (collisionMap[dest.getResultOf(1, 0).getY()][pos.getX()] == null) {
                            dest.adjust(1, 0);
                        } else if (collisionMap[dest.getResultOf(-1, 0).getY()][pos.getX()]
                                == null) {
                            dest.adjust(-1, 0);
                        } else if (collisionMap[pos.getY()][dest.getResultOf(0, 1).getX()]
                                == null) {
                            dest.adjust(0, 1);
                        } else if (collisionMap[pos.getY()][dest.getResultOf(0, -1).getX()]
                                == null) {
                            dest.adjust(0, -1);
                        } else {
                            dest = null;
                        }
                        if (dest != null) {
                            child.setPosition(dest);
                            collisionMap[dest.getY()][dest.getX()] = child;
                            newEntities.add(child);
                        }
                    }
                    /* feeding phase */
                    Tile thisTile = foodMap[pos.getY()][pos.getX()];
                    thisEntity.onEat(thisTile.getFoodType(),
                            thisTile.getFood(thisEntity.getEatMax()));
                    /* if this Genetic survived the cycle, enqueue it to be processed next cycle */
                    newEntities.add(thisEntity);
                    if (thisEntity.getTypeID() == 0) {
                        phagePop += 1;
                    } else {
                        bactPop += 1;
                    }
                }
            }
        }

        /* update the food Tiles */
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                foodMap[i][j].onUpdate();
            }
        }
        entityList = newEntities;
    }

    /**
     * Add an individual to the java.simulation
     *
     * @param g Genetic to add to the java.simulation
     */
    public void addEntity(Genetic g) {
        g.setPosition(new Position(g.getPosition(), height, width));
        entityList.add(g);
    }

    /**
     * Generate a new food map based on given constraints
     *
     * @param foodTileFrequency probability (0-256) of each generated tile being a food tile
     * @param regenRateBias     center of food regen rate values
     * @param regenRateVariance range / 2 of food regen rate values
     * @param foodTypeBias      center of food type values
     * @param foodTypeVariance  range / 2 of food type values
     * @param maxFoodBias       center of maximum food values
     * @param maxFoodVariance   range / 2 of maximum food values
     */
    public void generateFoodMap(int foodTileFrequency, int regenRateBias, int regenRateVariance,
                                int foodTypeBias, int foodTypeVariance, int maxFoodBias,
                                int maxFoodVariance) {
        Random rand = new Random();
        maxTileFood = maxFoodBias + maxFoodVariance;

        foodMap = new Tile[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                /* determine whether this is a food tile */
                if (rand.nextInt(FREQUENCY_MAX) < foodTileFrequency) {
                    /* generate new tile based on constraints */
                    int foodType = Utils.clamp(rand.nextInt(2 * foodTypeVariance + 1)
                            - foodTypeVariance + foodTypeBias, 0, 15);
                    int regenRate = Utils.clamp(rand.nextInt(2 * regenRateVariance + 1)
                                    - regenRateVariance + regenRateBias, 0,
                            Integer.MAX_VALUE);
                    int maxFood = Utils.clamp(rand.nextInt(2 * maxFoodVariance + 1)
                                    - maxFoodVariance + maxFoodBias, 0,
                            Integer.MAX_VALUE);
                    foodMap[row][col] = new Tile(foodType, regenRate, maxFood);
                } else {
                    foodMap[row][col] = new Tile(0, 0, 0);
                }
            }
        }
    }

    /* setters and getters */

    public LinkedList<Genetic> getEntities() {
        return new LinkedList<>(entityList);
    }

    public int getBactPop() {
        return bactPop;
    }

    public int getPhagePop() {
        return phagePop;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile[][] getFoodMap() {
        return foodMap;
    }

    public int getMaxTileFood() {
        return maxTileFood;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int newTemp) {
        temperature = newTemp;
    }

    public int getRadLevel() {
        return radLevel;
    }

    public void setRadLevel(int newLevel) {
        radLevel = newLevel;
    }
}
