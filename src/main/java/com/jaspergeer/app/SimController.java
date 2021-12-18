/*!
 * Copyright 2021 Jasper Geer. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for license information.
 */

package com.jaspergeer.app;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import com.jaspergeer.simulation.*;

import java.util.LinkedList;
import java.util.Random;

public class SimController {

    /* color of tile with no food */
    private final Color TILE_EMPTY_COLOR = Color.rgb(135, 135, 135);

    /* color of tile with maximum food */
    private final Color TILE_FULL_COLOR = Color.rgb(63, 87, 9);

    private final int TILE_MAX_FOOD = 256;

    private final long BASE_TICK = 32_000_000;

    private final double INFO_PANE_OPACITY = 0.8;

    private Simulation simulation;

    private AnimationTimer simLoop;

    private boolean isRunning;

    private boolean drawGenetics;

    private double displayScale;

    private Position displayInfoPos;

    private Genetic[][] entityMap;

    /* FXML Nodes */

    @FXML
    ImageView logo;

    @FXML
    Canvas simDisplay;

    @FXML
    AnchorPane displayPane;

    @FXML
    Label popCounter;

    @FXML
    JFXSlider tempSlider;

    @FXML
    JFXSlider speedSlider;

    @FXML
    JFXToggleButton simToggle;

    @FXML
    JFXSlider radSlider;

    /* Environment Settings Nodes */

    @FXML
    JFXSlider simSizeSlider;

    @FXML
    JFXSlider foodTypeSlider;

    @FXML
    JFXSlider foodTypeVarSlider;

    @FXML
    JFXSlider foodRegenSlider;

    @FXML
    JFXSlider foodRegenVarSlider;

    @FXML
    JFXSlider maxTileFoodSlider;

    @FXML
    JFXSlider maxTileFoodVarSlider;

    @FXML
    JFXSlider foodTileFreqSlider;

    /* Info Panel nodes */

    @FXML
    Pane infoPane;

    @FXML
    Label infoLabel;

    /* Populate Panel nodes */

    @FXML
    JFXSlider foodPrefSlider;

    @FXML
    JFXSlider flagellaSlider;

    @FXML
    JFXSlider divThresholdSlider;

    @FXML
    JFXSlider wallThicknessSlider;

    @FXML
    JFXSlider appetiteSlider;

    @FXML
    JFXSlider heatResistSlider;

    @FXML
    JFXSlider coldResistSlider;

    @FXML
    JFXSlider initEnergySlider;

    @FXML
    JFXSlider quantitySlider;

    @FXML
    JFXSlider combatSlider;

    @FXML
    public void initialize() {
        simulation = new Simulation((int) simSizeSlider.getValue(),
                (int) simSizeSlider.getValue(), 0, 0, 0, 0, 0, 0, 0, 0, 0);
        applySettings(simDisplay);
        entityMap = createEntityMap(simulation);

        drawGenetics = true;
        isRunning = false;

        displayInfoPos = new Position(0, 0, 0, 0);
        infoPane.setOpacity(0);

        simLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            /**
             * Update and redraw the java.simulation each tick
             * @param now current time
             */
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= (5 - speedSlider.getValue()) * BASE_TICK) {
                    simulation.update();
                    drawSimulation(simDisplay);
                    simulation.setTemperature((int) tempSlider.getValue());
                    simulation.setRadLevel((int) radSlider.getValue());
                    popCounter.setText("Population: " + simulation.getPopulation());
                    lastUpdate = now;
                }
            }
        };
    }

    /**
     * Start or stop the java.simulation
     *
     * @param event ActionEvent which triggered this
     */
    @FXML
    private void toggleSimulation(ActionEvent event) {
        if (isRunning) {
            simLoop.stop();
            entityMap = createEntityMap(simulation);
        } else {
            logo.setOpacity(0);
            simLoop.start();
        }
        isRunning = !isRunning;
    }

    @FXML
    private void toggleDrawGenetics(ActionEvent event) {
        drawGenetics = !drawGenetics;
    }

    @FXML
    private void populate(ActionEvent event) {
        Random rand = new Random();

        int simDim = simulation.getHeight();
        Bacteria b = new Bacteria(0, (int) initEnergySlider.getValue(),
                new Position(0, 0, simDim, simDim));
        b.setGeneVal(0, (int) flagellaSlider.getValue() - 1);
        b.setGeneVal(1, (int) coldResistSlider.getValue() - 1);
        b.setGeneVal(2, (int) heatResistSlider.getValue() - 1);
        b.setGeneVal(3, (int) divThresholdSlider.getValue() - 1);
        b.setGeneVal(4, (int) wallThicknessSlider.getValue() - 1);
        b.setGeneVal(5, (int) foodTypeSlider.getValue() - 1);
        b.setGeneVal(6, (int) appetiteSlider.getValue() - 1);
        b.setGeneVal(7, (int) combatSlider.getValue() - 1);

        for (int i = 0; i < quantitySlider.getValue(); i++) {
            b.setPosition(new Position(rand.nextInt(simDim), rand.nextInt(simDim),
                    simDim, simDim));
            simulation.addEntity(b);
        }
    }

    @FXML
    private void releaseVirus(ActionEvent event) {
        Random rand = new Random();
        int simDim = simulation.getHeight();
        Phage p = new Phage(rand.nextInt(), 0,
                new Position(rand.nextInt(simDim), rand.nextInt(simDim),simDim, simDim));
        simulation.addEntity(p);
    }

    /**
     * Reset java.simulation and apply Environment Settings
     *
     * @param event ActionEvent that triggered this
     */
    @FXML
    private void applySettingsReset(ActionEvent event) {
        simulation = new Simulation((int) simSizeSlider.getValue(),
                (int) simSizeSlider.getValue(), 0, 0, 0, 0, 0, 0, 0, 0, 0);
        applySettings(simDisplay);
    }

    /**
     * Apply Environment Settings to current java.simulation without reset
     *
     * @param event ActionEvent that triggered this
     */
    @FXML
    private void applySettings(ActionEvent event) {
        applySettings(simDisplay);
    }

    /**
     * Display a transparent text box next to the cursor containing information about the Genetic
     * the cursor is currently hovering over
     *
     * @param event MouseEvent containing mouse position relative to canvas
     */
    @FXML
    private void displayGeneticInfo(MouseEvent event) {
        int simX = Utils.clamp((int) (event.getX() / displayScale), 0, simulation.getWidth());
        int simY = Utils.clamp((int) (event.getY() / displayScale), 0, simulation.getHeight());
        infoPane.setTranslateX(event.getX());
        infoPane.setTranslateY(event.getY());
        displayInfoPos = new Position(simY, simX, simulation.getHeight(), simulation.getWidth());
        if (!isRunning && entityMap[displayInfoPos.getY()][displayInfoPos.getX()] != null) {
            String info = entityMap[displayInfoPos.getY()][displayInfoPos.getX()].toString();
            int numLines = info.split("\n").length;
            double fontSize = infoLabel.getFont().getSize();
            infoPane.setOpacity(INFO_PANE_OPACITY);
            infoLabel.setPrefHeight(numLines * fontSize);
            infoPane.setPrefHeight(numLines * fontSize + 20);
            infoLabel.setText(info);
        } else {
            infoPane.setOpacity(0);
        }
    }

    /**
     * Hide the transparent info box
     *
     * @param event MouseEvent that triggered this
     */
    @FXML
    private void hideInfoPane(MouseEvent event) {
        infoPane.setOpacity(0);
    }

    /**
     * Apply values specified in Environment Settings to simulation environment
     *
     * @param display Canvas to calculate simulation to display scale from
     */
    private void applySettings(Canvas display) {
        simulation.generateFoodMap((int) foodTileFreqSlider.getValue(),
                (int) foodRegenSlider.getValue(), (int) foodRegenVarSlider.getValue(),
                (int) foodTypeSlider.getValue() - 1,
                (int) foodTypeVarSlider.getValue(), (int) maxTileFoodSlider.getValue(),
                (int) maxTileFoodVarSlider.getValue());
        displayScale = display.getHeight() / (double) simulation.getHeight();
    }

    /**
     * Draw food Tiles in current state and each Genetic in current positions on given Canvas
     *
     * @param display Canvas to draw on
     */
    private void drawSimulation(Canvas display) {
        GraphicsContext gc = display.getGraphicsContext2D();
        LinkedList<Genetic> toDraw = simulation.getEntities();
        Tile[][] foodMap = simulation.getFoodMap();
        gc.clearRect(0, 0, display.getWidth(), display.getHeight());

        /* draw food tiles */
        for (int row = 0; row < simulation.getHeight(); row++) {
            for (int col = 0; col < simulation.getWidth(); col++) {
                Color tileColor;
                double gradientPos = 0;
                /* calculate how the current tile will be represented on the color gradient */
                if (foodMap[row][col].getMaxFood() != 0) {
                    gradientPos = foodMap[row][col].getAvailableFood() /
                            (double) TILE_MAX_FOOD;
                }
                int RGB_MAX_VAL = 255;
                int red = (int) (RGB_MAX_VAL * (TILE_EMPTY_COLOR.getRed() - gradientPos *
                        (TILE_EMPTY_COLOR.getRed() - TILE_FULL_COLOR.getRed())));
                int green = (int) (RGB_MAX_VAL * (TILE_EMPTY_COLOR.getGreen() - gradientPos *
                        (TILE_EMPTY_COLOR.getGreen() - TILE_FULL_COLOR.getGreen())));
                int blue = (int) (RGB_MAX_VAL * (TILE_EMPTY_COLOR.getBlue() - gradientPos *
                        (TILE_EMPTY_COLOR.getBlue() - TILE_FULL_COLOR.getBlue())));
                tileColor = Color.rgb(
                        Utils.clamp(red, 0, RGB_MAX_VAL),
                        Utils.clamp(green, 0, RGB_MAX_VAL),
                        Utils.clamp(blue, 0, RGB_MAX_VAL));
                gc.setFill(tileColor);
                gc.fillRect(col * displayScale, row * displayScale, displayScale, displayScale);
            }
        }

        if (drawGenetics) {
            /* draw each living Genetic */
            while (!toDraw.isEmpty()) {
                Genetic g = toDraw.remove();
                Position p = g.getPosition();

                gc.setFill(g.getColor());
                gc.fillRect(p.getX() * displayScale, p.getY() * displayScale, displayScale,
                        displayScale);
            }
        }
    }

    /**
     * Create a 2D array storing the position of each Genetic in the java.simulation
     *
     * @param s Simulation to create array from
     * @return 2D array storing Genetics at corresponding positions
     */
    private Genetic[][] createEntityMap(Simulation s) {
        LinkedList<Genetic> entities = s.getEntities();
        Genetic[][] map = new Genetic[s.getHeight()][s.getHeight()];
        while (!entities.isEmpty()) {
            Genetic thisEntity = entities.remove();
            Position pos = thisEntity.getPosition();
            map[pos.getY()][pos.getX()] = thisEntity;
        }
        return map;
    }
}
