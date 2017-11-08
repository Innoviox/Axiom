package com.axiom.engine;

import com.axiom.engine.item.SkyBox;
import com.axiom.engine.item.light.Light;
import com.axiom.engine.item.model.Mesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.axiom.engine.item.Item;

public class Scene {

    private Item[] gameItems;
    private SkyBox skyBox;
    private Light sceneLight;
    private Map<Mesh, List<Item>> meshMap;

    public Scene() {
        meshMap = new HashMap();
    }
    
    public Item[] getGameItems() {
        return gameItems;
    }

    public void setGameItems(Item[] gameItems) {
        int numGameItems = gameItems != null ? gameItems.length : 0;
        for (int i=0; i<numGameItems; i++) {
            Item gameItem = gameItems[i];
            Mesh mesh = gameItem.getMesh();
            List<Item> list = meshMap.get(mesh);
            if ( list == null ) {
                list = new ArrayList<>();
                meshMap.put(mesh, list);
            }
            list.add(gameItem);
        }
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
