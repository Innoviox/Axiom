package com.axiom.engine.hud;

import com.axiom.engine.item.Item;

public interface HudScene {
    Item[] getGameItems();

    default void cleanup() {
    		Item[] gameItems = getGameItems();
        for (Item gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}