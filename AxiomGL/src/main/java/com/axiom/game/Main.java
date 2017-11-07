package com.axiom.game;

import com.axiom.engine.Engine;
import com.axiom.engine.IGame;
import com.axiom.game.Game;

public class Main {	 
    public static void main(String[] args) {
        try {
        		System.setProperty("java.awt.headless", "true");
            boolean vSync = true;
            IGame gameLogic = new Game();
            Engine gameEng = new Engine("MY GAME", 600, 480, vSync, gameLogic);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
        }
    }
}
