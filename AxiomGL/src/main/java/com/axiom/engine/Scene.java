/*
 * Holds data about the world
 * <p>
 * <br>
 * This class holds the Items, Lights, Meshes and SkyBox
 * that are in the Game. A Game should contain a Scene
 * instance and render based on it.
 * </p>
 * <p>
 * @author Antonio Hernández Bejarano (@lwjglgamedev)
 * @author The Axiom Corp, 2017.
 * </p>
 */
package com.axiom.engine;

import com.axiom.engine.item.SkyBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.axiom.engine.item.Item;
import com.axiom.engine.item.Light;
import com.axiom.engine.item.Mesh;

public class Scene {

    private Item[] gameItems;
    private SkyBox skyBox;
    private Light sceneLight;
    private Map<Mesh, List<Item>> meshMap;
    
    /*
     * Instantiate a new Scene
     */
    public Scene() {
        meshMap = new HashMap();
    }
    
    /*
     * Getter for gameItems
     */
    public Item[] getGameItems() {
        return gameItems;
    }
    
    /*
     * Set game item list
     */
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
    
    /*
     * Return sky box
     */
    public SkyBox getSkyBox() {
        return skyBox;
    }

    /*
     * Set new sky box
     */
    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }

    /*
     * Get light
     */
    public Light getSceneLight() {
        return sceneLight;
    }

    /*
     * Set new light
     */
    public void setSceneLight(Light sceneLight) {
        this.sceneLight = sceneLight;
    }
    
}
