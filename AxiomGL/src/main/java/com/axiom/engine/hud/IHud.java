/**
 * A Hud interface
 * <p>
 * <br>
 * This interface should be implemented by
 * the client so that the Renderer can interface
 * with it.
 * </p>
 * <p>
 * @author Antonio Hernández Bejarano (@lwjglgamedev)
 * @author The Axiom Corp, 2017.
 * </p>
 */
package com.axiom.engine.hud;

import com.axiom.engine.item.Item;

public interface IHud {
	/**
	 * Get the game items
	 * @return game items
	 */
    Item[] getGameItems();

    /**
     * Clean up the HUD
     */
    default void cleanup() {
    		Item[] gameItems = getGameItems();
        for (Item gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}