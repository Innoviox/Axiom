package com.axiom.game;

import java.awt.Font;

import org.joml.Vector4f;

import com.axiom.engine.Window;
import com.axiom.engine.hud.HudScene;
import com.axiom.engine.hud.TextItem;
import com.axiom.engine.hud.FontTexture;
import com.axiom.engine.item.Item;
import com.axiom.engine.item.Material;
import com.axiom.engine.item.Mesh;
import com.axiom.engine.loaders.OBJLoader;


public class Hud implements HudScene {


    private static final Font FONT = new Font("Arial", Font.BOLD, 20);

    private static final String CHARSET = "ISO-8859-1";

    private final Item[] gameItems;

    private final TextItem statusTextItem;

    private final Item compassItem;

    public Hud(String statusText) throws Exception {
        FontTexture fontTexture = new FontTexture(FONT, CHARSET);
        this.statusTextItem = new TextItem(statusText, fontTexture);
        this.statusTextItem.getMesh().getMaterial().setAmbientColour(new Vector4f(1, 1, 1, 1));

        // Create compass
        Mesh mesh = OBJLoader.loadMesh("/models/compass.obj");
        Material material = new Material();
        material.setAmbientColour(new Vector4f(1, 0, 0, 1));
        mesh.setMaterial(material);
        compassItem = new Item(mesh);
        compassItem.setScale(40.0f);
        // Rotate to transform it to screen coordinates
        compassItem.setRotation(0f, 0f, 180f);

        // Create list that holds the items that compose the HUD
        gameItems = new Item[]{statusTextItem, compassItem};
    }

    public void setStatusText(String statusText) {
        this.statusTextItem.setText(statusText);
    }
    
    public void rotateCompass(float angle) {
        this.compassItem.setRotation(0, 0, 180 + angle);
    }

    @Override
    public Item[] getGameItems() {
        return gameItems;
    }
   
    public void updateSize(Window window) {
        this.statusTextItem.setPosition(10f, window.getHeight() - 50f, 0);
        this.compassItem.setPosition(window.getWidth() - 40f, 50f, 0);
    }
}
