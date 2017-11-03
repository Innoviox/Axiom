package com.axiom.game;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;
import java.util.Arrays;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.omg.CORBA.SystemException;

import com.axiom.engine.Scene;
import com.axiom.engine.Window;
import com.axiom.engine.input.MouseHandler;
import com.axiom.engine.item.Collidable;
import com.axiom.engine.item.CollidableItem;
import com.axiom.engine.item.Item;
import com.axiom.engine.item.Light;
import com.axiom.engine.item.Material;
import com.axiom.engine.item.Mesh;
import com.axiom.engine.item.Texture;
import com.axiom.engine.loaders.OBJLoader;
import com.axiom.engine.math.Camera;
import com.axiom.engine.input.KeyboardHandler;
import com.axiom.engine.Renderer;
import org.joml.Vector2f;
public class Game implements Scene {
    
    private final Renderer renderer;
    private Item[] gameItems;
    private final KeyboardHandler input = new KeyboardHandler();
    private GLFWKeyCallback keyCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWScrollCallback scrollCallback;
    
    private Camera camera;
    private Vector3f cameraInc;
    private double ry;
    private double rx;

    private Vector3f ambientLight;
    private Light light;
    private static final float CAMERA_POS_STEP = 0.05f;
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private boolean moving2 = false;
    
    private int n = 0;
    private float chng = .01f;
    public Game() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
    }
    
    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        float reflectance = .1f;
        //Mesh mesh = OBJLoader.loadMesh("/models/bunny.obj");
        //Material material = new Material(new Vector3f(0.2f, 0.5f, 0.5f), reflectance);

        Mesh mesh = OBJLoader.loadMesh("/models/cube.obj");
        Texture texture = new Texture("/textures/newgrassblock.png");
        Material material = new Material(texture, reflectance);

        mesh.setMaterial(material);
        Item gameItem = new CollidableItem(mesh);
        gameItem.setScale(0.5f);
        //System.out.println("a1 "+Arrays.toString(gameItem.getMesh().getPositions()));
        //System.out.println("a1 "+Arrays.toString(((Collidable)gameItem).getVertexPositions(camera)));
        //System.out.println("a1 "+Arrays.toString(((Collidable)gameItem).genHitbox(camera)));
        gameItem.setPosition(0, 0, -2);
        //System.out.println("b1 "+Arrays.toString(gameItem.getMesh().getPositions()));
        //System.out.println("b1 "+Arrays.toString(((Collidable)gameItem).getVertexPositions(camera)));
        //System.out.println("b1 "+Arrays.toString(((Collidable)gameItem).genHitbox(camera)));        
        Mesh mesh2 = OBJLoader.loadMesh("/models/cuben.obj");
        Texture texture2 = new Texture("/textures/newgrassblock.png");
        Material material2 = new Material(texture2, reflectance);

        mesh2.setMaterial(material2);
        Item gameItem2 = new CollidableItem(mesh2);
        gameItem2.setScale(0.5f);
        //System.out.println("a1 "+Arrays.toString(gameItem2.getMesh().getPositions()));
        //System.out.println("a1 "+Arrays.toString(((Collidable)gameItem2).getVertexPositions(camera)));
        ///System.out.println("a1 "+Arrays.toString(((Collidable)gameItem2).genHitbox(camera)));
        gameItem2.setPosition(0, 0, 4);
        //System.out.println("a2 "+Arrays.toString(gameItem2.getMesh().getPositions()));
        //System.out.println("a2 "+Arrays.toString(((Collidable)gameItem2).getVertexPositions(camera)));
        //System.out.println("a2 "+Arrays.toString(((Collidable)gameItem2).genHitbox(camera)));   
        
        Mesh mesh3 = OBJLoader.loadMesh("/models/cuben.obj");
        Texture texture3 = new Texture("/textures/newgrassblock.png");
        Material material3 = new Material(texture3, reflectance);        
        mesh3.setMaterial(material3);
        Item gameItem3 = new CollidableItem(mesh3);
        gameItem3.setScale(0.01f);
        //System.out.println("a1 "+Arrays.toString(gameItem2.getMesh().getPositions()));
        //System.out.println("a1 "+Arrays.toString(((Collidable)gameItem2).getVertexPositions(camera)));
        //System.out.println("a1 "+Arrays.toString(((Collidable)gameItem2).genHitbox(camera)));
        gameItem3.setPosition(0, 0, 0);
        //System.out.println("a2 "+Arrays.toString(gameItem2.getMesh().getPositions()));
        //System.out.println("a2 "+Arrays.toString(((Collidable)gameItem2).getVertexPositions(camera)));
        //System.out.println("a2 "+Arrays.toString(((Collidable)gameItem2).genHitbox(camera)));   
        
        
        
        Mesh mesh4 = OBJLoader.loadMesh("/models/cuben.obj");
        Texture texture4 = new Texture("/textures/newgrassblock.png");
        Material material4 = new Material(texture4, reflectance);        
        mesh4.setMaterial(material4);
        Item gameItem4 = new CollidableItem(mesh4);
        gameItem4.setScale(0.01f);
        gameItem4.setPosition(0, 0, -2);
        
        Mesh mesh5 = OBJLoader.loadMesh("/models/cuben.obj");
        Texture texture5 = new Texture("/textures/newgrassblock.png");
        Material material5 = new Material(texture5, reflectance);        
        mesh5.setMaterial(material5);
        Item gameItem5 = new CollidableItem(mesh5);
        gameItem5.setScale(0.01f);
        gameItem5.setPosition(0, 0, -2);

        Mesh mesh6 = OBJLoader.loadMesh("/models/cuben.obj");
        Texture texture6 = new Texture("/textures/newgrassblock.png");
        Material material6 = new Material(texture6, reflectance);        
        mesh6.setMaterial(material6);
        Item gameItem6 = new CollidableItem(mesh6);
        gameItem6.setScale(0.01f);
        gameItem6.setPosition(0, 0, -2);

        
        gameItems = new Item[]{gameItem, gameItem2, gameItem3, gameItem4, gameItem5, gameItem6};


        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(-1, 0, 1);
        //float lightIntensity = 1.0f;
        light = new Light(lightColour, lightPosition, ambientLight, 0.2f, 5.0f);
        glfwSetKeyCallback(window.getWindowHandle(), keyCallback = input.keyboard);
        //glfwSetMouseButtonCallback(window.getWindowHandle(), mouseButtonCallback = input.mouse);
        glfwSetScrollCallback(window.getWindowHandle(), scrollCallback = input.scroll);

		//System.exit(0);
    }
    
    @Override
    public void input(Window window, MouseHandler mouseInput) {
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
        int mult;
        if (Math.cos(Math.toRadians(rx)) < 0) {
            mult = -1;
        } else {
            mult = 1;
        }
        ry = 0;
        rx = 0;
        if (input.keyDown(GLFW_KEY_LEFT))
            ry = mult;
        if (input.keyDown(GLFW_KEY_RIGHT))
            ry = -mult;
        if (input.keyDown(GLFW_KEY_DOWN))
            rx = 1;
        if (input.keyDown(GLFW_KEY_UP))
            rx = -1;
        
        ry = (Math.abs(ry) % 360) * Math.signum(ry);
        rx = (Math.abs(rx) % 360) * Math.signum(rx);
        moving2 = window.isKeyPressed(GLFW_KEY_M);
        mouseInput.input(window);
        
        if (input.keyDown(GLFW_KEY_Q))
            System.exit(0);
    }
    
    
    @Override
    public void update(float interval, MouseHandler mouseInput) {
    		n++;
        camera.moveRotation((float)rx, (float)ry, 0.0f);
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }
        Vector3f pos = gameItems[1].getPosition();
        if (moving2) gameItems[1].setPosition(pos.x, pos.y, pos.z - .01f);
        
        if ((n % 200) == 0) chng *= -1;
        light.incPosition(chng, 0, 0);
        CollidableItem a=(CollidableItem)gameItems[0], b=(CollidableItem)gameItems[1];
        gameItems[2].setPosition(a.max(camera));
        gameItems[3].setPosition(a.min(camera));
        gameItems[4].setPosition(b.max(camera));
        gameItems[5].setPosition(b.min(camera));
        //System.out.println(Arrays.toString(a.getVertexPositions(camera)));
        //System.out.println(Arrays.toString(a.getVertexPositions(camera)));
        if (a.collides(b, camera)) {
        		System.out.println("collision");
        		/*
        		for (Item i: gameItems) {
        			CollidableItem j = (CollidableItem)i;
        			System.out.println(Arrays.toString(j.getMesh().getPositions()));
        			System.out.println(Arrays.toString(j.getVertexPositions(camera)));
        		}*/
        		//System.exit(0);
        			
        }
        //else System.out.println("no collision");
        //System.out.println(camera.getPosition());
        //System.out.println(light.getPosition());
        //System.out.println(a.getPosition());
        //System.out.println(b.getPosition());
    }
    
    public double[] getMousePosition() {
        DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(renderer.getWindow().getWindowHandle(), xBuffer, yBuffer);
        return new double[] { xBuffer.get(0), yBuffer.get(0) };
    }
    
    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameItems, light);
    }
    
    @Override
    public void cleanup() {
        renderer.cleanup();
        for (Item gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
    
}
