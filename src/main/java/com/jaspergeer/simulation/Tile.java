package com.jaspergeer.simulation;

public class Tile {

    private int availableFood;
    private final int foodType;
    private final int foodRegenRate;
    private final int maxFood;

    public Tile(int foodType, int regenRate, int maxFood) {
        availableFood = 0;
        this.foodType = foodType;
        foodRegenRate = regenRate;
        this.maxFood = maxFood;
    }

    /**
     * Update is called every cycle by the java.simulation
     */
    public void onUpdate() {
        availableFood = Utils.clamp(availableFood + foodRegenRate, 0, maxFood);
    }

    /**
     * Take a given amount of food from this tile
     *
     * @param amount amount of food to take
     * @return given amount of food if available or rest of available food if not
     */
    public int getFood(int amount) {
        if (amount > availableFood) {
            availableFood = 0;
            return availableFood;
        }
        availableFood -= amount;
        return amount;
    }

    public void setAvailableFood(int amount) {
        availableFood = amount;
    }

    public int getFoodType() {
        return foodType;
    }

    public int getAvailableFood() {
        return availableFood;
    }

    public int getMaxFood() {
        return maxFood;
    }
}
