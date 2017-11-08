package tuton.engine;

import tuton.engine.*;
import tuton.engine.graph.*;
import tuton.engine.graph.lights.*;
import tuton.engine.items.*;

public interface IHud {

    GameItem[] getGameItems();

    default void cleanup() {
        GameItem[] gameItems = getGameItems();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}
