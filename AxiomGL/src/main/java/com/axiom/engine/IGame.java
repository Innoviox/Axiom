/**
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
    /**
     * Initialize the game
     * @param window the window to initialize based on
     * @throws Exception if files are not found
     */
	void init(Window window) throws Exception;
	
    /**
     * Render the game to a window
     * @param window the window to render to
     */
	void render(Window window);
	
    @Deprecated
    /**
     * Clean up the game
     */
    void cleanup();
    
    /**
     * Update the game based on input
     * @param interval the sync interval (deprecated)
     * @param mouseInput the mouse input
     * @param keyboardInput the keyboard input
     */
	void update(float interval, MouseListener mouseInput, KeyboardListener keyboardInput);
	
	/**
	 * Take in input
	 * @param window the window to input from
	 * @param mouseInput the mouse input
     * @param keyboardInput the keyboard input
	 */
	void input(Window window, MouseListener mouseInput, KeyboardListener keyboardInput);
}
