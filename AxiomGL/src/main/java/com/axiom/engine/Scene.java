/**
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
    
    /**
     * Instantiate a new Scene
     */
    public Scene() {
        meshMap = new HashMap();
    }
    
    /**
     * Getter for gameItems
     * @return the scene's items
     */
    public Item[] getGameItems() {
        return gameItems;
    }
    
    /**
     * Set game item list
     * @param gameItems new game items
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
    
    /**
     * Return sky box
     * @return the scene's skybox
     */
    public SkyBox getSkyBox() {
        return skyBox;
    }

    /**
     * Set new sky box
     * @param skyBox new skybox
     */
    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }

    /**
     * Get light
     * @return the scene's light
     */
    public Light getSceneLight() {
        return sceneLight;
    }

    /**
     * Set new light
     * @param sceneLight the new light
     */
    public void setSceneLight(Light sceneLight) {
        this.sceneLight = sceneLight;
    }

	public void addGameItem(Item i) {
		Item[] gi = new Item[gameItems.length + 1];
		int j;
		for (j = 0; j < gameItems.length; j++) gi[j] = gameItems[j];
		gi[j] = i;
		this.setGameItems(gi);
	}
    
}
