package com.axiom.engine.input;

import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;

public final class InputHandler {
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

	public GLFWScrollCallback scroll = new GLFWScrollCallback()
    {
    	public void invoke(long window, double xoffset, double yoffset)
    	{
    		scrollStates[0] = xoffset;
    		scrollStates[1] = yoffset;
    	}
    };

	public GLFWKeyCallback keyboard = new GLFWKeyCallback()
    {
        public void invoke(long window, int key, int scancode, int action, int mods)
        {
            activeKeys[key]=action!=GLFW_RELEASE;keyStates[key]=action;
        }
    };

	public GLFWMouseButtonCallback mouse = new GLFWMouseButtonCallback()
    {
        public void invoke(long window, int button, int action, int mods)
        {
            activeMouseButtons[button]=action!=GLFW_RELEASE;getMouseButtonStates()[button]=action;}};

	private void resetKeyboard() {
		for (int i = 0; i < keyStates.length; i++) {
			keyStates[i] = NO_STATE;
		}
	}

	private void resetScroll(){
		for( int i = 0; i < scrollStates.length; i ++ ){
			scrollStates[i] = 0.0;
		}
	}
	
	private void resetMouse() {
		for (int i = 0; i < getMouseButtonStates().length; i++) {
			getMouseButtonStates()[i] = NO_STATE;
		}

		long now = System.nanoTime();

		if (now - lastMouseNS > mouseDoubleClickPeriodNS)
			lastMouseNS = 0;
	}

	public boolean keyDown(int key) {
		return activeKeys[key];
	}

	public boolean keyPressed(int key) {
		return keyStates[key] == GLFW_PRESS;
	}

	public boolean keyReleased(int key) {
		return keyStates[key] == GLFW_RELEASE;
	}

	public boolean mouseButtonDown(int button) {
		return activeMouseButtons[button];
	}

	public boolean mouseButtonPressed(int button) {
		return getMouseButtonStates()[button] == GLFW_RELEASE;
	}

	public boolean mouseButtonReleased(int button) {
		boolean flag = getMouseButtonStates()[button] == GLFW_RELEASE;

		if (flag)
			lastMouseNS = System.nanoTime();

		return flag;
	}

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

	public int[] getMouseButtonStates() {
		return mouseButtonStates;
	}
	
	public double[] getScrollStates(){
		return scrollStates;
	}
}