package tuton.game;

import tuton.engine.*;
import tuton.engine.graph.*;
import tuton.engine.graph.lights.*;
import tuton.engine.items.*;
 
public class Main {
 
    public static void main(String[] args) {
        try {
        		System.setProperty("java.awt.headless", "true");
            boolean vSync = true;
            IGameLogic gameLogic = new DummyGame();
            GameEngine gameEng = new GameEngine("GAME", vSync, gameLogic);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}