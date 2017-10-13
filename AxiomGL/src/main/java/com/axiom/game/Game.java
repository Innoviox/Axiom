package com.axiom.game;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;
import java.util.Arrays;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import com.axiom.engine.Scene;
import com.axiom.engine.Texture;
import com.axiom.engine.Window;
import com.axiom.engine.input.MouseInput;
import com.axiom.engine.loaders.OBJLoader;
import com.axiom.engine.math.Camera;
import com.axiom.InputHandler;
import com.axiom.engine.Item;
import com.axiom.engine.Mesh;
import com.axiom.engine.Renderer;
import org.joml.Vector2f;
public class Game implements Scene {

    private int displxInc = 0;
    private int displyInc = 0;
    private int displzInc = 0;
    private int scaleInc = 0;
    private final Renderer renderer;
    private Item[] gameItems;
	private final InputHandler input = new InputHandler();
	private GLFWKeyCallback keyCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWScrollCallback scrollCallback;
	
	private float rx, ry, rz, x, y, z;
	private double[] oldMousePos;
	private Camera camera;
	private Vector3f cameraInc;
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.05f;
    public Game() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        Mesh mesh = OBJLoader.loadMesh("/models/bunny.obj");
        //Texture texture = new Texture("/textures/grassblock.png");
        //mesh.setTexture(texture);
        
        Item gameItem1 = new Item(mesh);
        gameItem1.setScale(0.5f);
        gameItem1.setPosition(0, -1, -4);

        Item gameItem2 = new Item(mesh);
        gameItem2.setScale(0.5f);
        gameItem2.setPosition(0.5f, 0.5f, -2);

        Item gameItem3 = new Item(mesh);
        gameItem3.setScale(0.5f);
        gameItem3.setPosition(0, 0, -2.5f);

        Item gameItem4 = new Item(mesh);
        gameItem4.setScale(0.5f);

        gameItem4.setPosition(0.5f, 0, -2.5f);
        gameItems = new Item[]{gameItem1};//, gameItem2, gameItem3, gameItem4};
        
		glfwSetKeyCallback(window.getWindowHandle(), keyCallback = input.keyboard);
		glfwSetMouseButtonCallback(window.getWindowHandle(), mouseButtonCallback = input.mouse);
		glfwSetScrollCallback(window.getWindowHandle(), scrollCallback = input.scroll);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
    }


    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
    }
    
	public double[] getMousePosition() {
		DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(renderer.getWindow().getWindowHandle(), xBuffer, yBuffer);
		return new double[] { xBuffer.get(0), yBuffer.get(0) };
	}
	
    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameItems);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (Item gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }

}
