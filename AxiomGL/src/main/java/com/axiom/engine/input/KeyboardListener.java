/**
 * Responds to keyboard presses
 * <p><br>
 * This class contains methods for interacting with
 * the keyboard, mouse, and scroll wheel. 
 * <br>
 * The mouse of this is deprecated and has been moved
 * to {@link com.axiom.engine.input.MouseListener}.
 * </p><p>
 * @author The Axiom Corp, 2017.
 * </p>
 */
package com.axiom.engine.input;

import org.lwjgl.glfw.*;

import com.axiom.engine.Window;

import static org.lwjgl.glfw.GLFW.*;

public final class KeyboardListener {
	private static final int KEYBOARD_SIZE = 512;
	private static final int MOUSE_SIZE = 16;

	private int[] keyStates = new int[KEYBOARD_SIZE];
	private boolean[] activeKeys = new boolean[KEYBOARD_SIZE];

	private int[] mouseButtonStates = new int[MOUSE_SIZE];
	private boolean[] activeMouseButtons = new boolean[MOUSE_SIZE];

	private double[] scrollStates = new double[2];

	private long lastMouseNS = 0;
	private long mouseDoubleClickPeriodNS = 1000000000 / 5; // 5th of a second
															// for double click.

	private int NO_STATE = -1;
	// tied to the scroll wheel
	public GLFWScrollCallback scroll = new GLFWScrollCallback()
    {
	    	public void invoke(long window, double xoffset, double yoffset)
	    	{
	    		scrollStates[0] = xoffset;
	    		scrollStates[1] = yoffset;
	    	}
    };

    //tied to the keyboard
	public GLFWKeyCallback keyboard = new GLFWKeyCallback()
    {
        public void invoke(long window, int key, int scancode, int action, int mods)
        {
            activeKeys[key]=action!=GLFW_RELEASE;keyStates[key]=action;
        }
    };
    
    @Deprecated
	public GLFWMouseButtonCallback mouse = new GLFWMouseButtonCallback()
    {
        public void invoke(long window, int button, int action, int mods)
        {
            activeMouseButtons[button]=action!=GLFW_RELEASE;mouseButtonStates[button]=action;}};
	
    /**
     * Resets the keyboard states
     */
	public void resetKeyboard() {
		for (int i = 0; i < keyStates.length; i++) {
			keyStates[i] = NO_STATE;
		}
	}

	/**
	 * Resets the scroll states
	 */
	public void resetScroll(){
		for( int i = 0; i < scrollStates.length; i ++ ){
			scrollStates[i] = 0.0;
		}
	}
	
	@Deprecated
	/**
	 * Resets the mouse states
	 */
	public void resetMouse() {
		for (int i = 0; i < mouseButtonStates.length; i++) {
			mouseButtonStates[i] = NO_STATE;
		}

		long now = System.nanoTime();

		if (now - lastMouseNS > mouseDoubleClickPeriodNS)
			lastMouseNS = 0;
	}

	/**
	 * Check if a key is down
	 * @param key the key to check
	 * @return key down?
	 */
	public boolean keyDown(int key) {
		return activeKeys[key];
	}

	/**
	 * Check if a key is pressed
	 * @param key the key to check
	 * @return key pressed?
	 */
	public boolean keyPressed(int key) {
		return keyStates[key] == GLFW_PRESS;
	}

	/**
	 * Check if a key is released
	 * @param key the key to check
	 * @return key released?
	 */
	public boolean keyReleased(int key) {
		return keyStates[key] == GLFW_RELEASE;
	}

	@Deprecated
	/**
	 * Check if a mouse button is down
	 * @param button the button to check
	 * @return button down?
	 * @deprecated {@link com.axiom.engine.input.MouseListener}
	 */
	public boolean mouseButtonDown(int button) {
		return activeMouseButtons[button];
	}

	@Deprecated
	/**
	 * Check if a mouse button is pressed
	 * @param button the button to check
	 * @return button pressed?
	 * @deprecated {@link com.axiom.engine.input.MouseListener}
	 */
	public boolean mouseButtonPressed(int button) {
		return mouseButtonStates[button] == GLFW_RELEASE;
	}

	@Deprecated
	/**
	 * Check if a mouse button is released
	 * @param button the button to check
	 * @return button released?
	 * @deprecated {@link com.axiom.engine.input.MouseListener}
	 */
	public boolean mouseButtonReleased(int button) {
		boolean flag = mouseButtonStates[button] == GLFW_RELEASE;

		if (flag)
			lastMouseNS = System.nanoTime();

		return flag;
	}

	@Deprecated
	/**
	 * Check if a mouse button is double clicked
	 * @param button the button to check
	 * @return button double clicked?
	 * @deprecated {@link com.axiom.engine.input.MouseListener}
	 */
	public boolean mouseButtonDoubleClicked(int button) {
		long last = lastMouseNS;
		boolean flag = mouseButtonReleased(button);

		long now = System.nanoTime();

		if (flag && now - last < mouseDoubleClickPeriodNS) {
			lastMouseNS = 0;
			return true;
		}

		return false;
	}
	
	/**
	 * Initialize the input
	 * @param window the window to tie to
	 */
    public void init(Window window) {
    		glfwSetKeyCallback(window.getWindowHandle(), this.keyboard);
    		glfwSetScrollCallback(window.getWindowHandle(), this.scroll);
    }
}