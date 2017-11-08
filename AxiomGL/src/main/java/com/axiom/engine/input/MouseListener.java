/**
 * Responds to mouse clicking, motion, and position
 * <p><br>
 * This class has methods to keep track of the right
 * and left mouse buttons, cursor motion, the cursor
 * position, and the display vector to update
 * the camera based on.
 * </p><p>
 * @author Antonio Hernández Bejarano (@lwjglgamedev)
 * </p>
 */
package com.axiom.engine.input;

import org.joml.Vector2d;
import org.joml.Vector2f;

import com.axiom.engine.Window;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {

    private final Vector2d previousPos;

    private final Vector2d currentPos;

    private final Vector2f displVec;

    private boolean inWindow = false;

    private boolean leftButtonPressed = false;

    private boolean rightButtonPressed = false;

    /**
     * Construct a new MouseListner
     * <br>
     * Default position: 0, 0
     * Default display vector: 0, 0
     */
    public MouseListener() {
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        displVec = new Vector2f();
    }

    /**
     * Initialize the mouseListener's glfw callbacks
     * @param window the window to tie it to
     */
    public void init(Window window) {
        glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xpos, ypos) -> {
            currentPos.x = xpos;
            currentPos.y = ypos;
        });
        glfwSetCursorEnterCallback(window.getWindowHandle(), (windowHandle, entered) -> {
            inWindow = entered;
        });
        glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode) -> {
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
    }

    /**
     * Get the display vector
     * @return the display vector
     */
    public Vector2f getDisplVec() {
        return displVec;
    }

    /**
     * Update the display vector
     * @param window the window to tie from
     */
    public void input(Window window) {
        displVec.x = 0;
        displVec.y = 0;
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double deltax = currentPos.x - previousPos.x;
            double deltay = currentPos.y - previousPos.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            if (rotateX) {
                displVec.y = (float) deltax;
            }
            if (rotateY) {
                displVec.x = (float) deltay;
            }
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

    /**
     * @return is the left button pressed?
     */
    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    /**
     * @return is the right button pressed?
     */
    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
