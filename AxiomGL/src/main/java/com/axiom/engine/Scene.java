package com.axiom.engine;

import com.axiom.engine.item.SkyBox;
import com.axiom.engine.item.Item;
import com.axiom.engine.item.Light;

public class Scene {

    private Item[] gameItems;
    
    private SkyBox skyBox;
    
    private Light sceneLight;

    public Item[] getGameItems() {
        return gameItems;
    }

    public void setGameItems(Item[] gameItems) {
        this.gameItems = gameItems;
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }

    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }

    public Light getSceneLight() {
        return sceneLight;
    }

    public void setSceneLight(Light sceneLight) {
        this.sceneLight = sceneLight;
    }
    
}
