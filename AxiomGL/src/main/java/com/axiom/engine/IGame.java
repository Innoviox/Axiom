/*
 * A Game interface
 * <p>
 * <br>
 * This interface should be implemented by
 * the client so that the Engine can interface
 * with it.
 * </p>
 * <p>
 * @author Antonio Hernández Bejarano (@lwjglgamedev)
 * @author The Axiom Corp, 2017.
 * </p>
 */
package com.axiom.engine;

import com.axiom.engine.input.KeyboardListener;
import com.axiom.engine.input.MouseListener;

public interface IGame {
    /*
     * Initialize the game
     */
	void init(Window window) throws Exception;
	
    /*
     * Render the game to a window
     */
	void render(Window window);
	
    @Deprecated
    /*
     * Clean up the game
     */
    void cleanup();
    
    /*
     * Update the game based on input
     */
	void update(float interval, MouseListener mouseInput, KeyboardListener keyboardInput);
	
	/*
	 * Take in input
	 */
	void input(Window window, MouseListener mouseInput, KeyboardListener keyboardInput);
}
