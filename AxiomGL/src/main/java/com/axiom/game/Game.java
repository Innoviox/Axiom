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

import com.axiom.engine.IGame;
import com.axiom.engine.Window;
import com.axiom.engine.input.MouseListener;
import com.axiom.engine.item.Collidable;
import com.axiom.engine.item.CollidableItem;
import com.axiom.engine.item.Item;
import com.axiom.engine.item.Light;
import com.axiom.engine.item.Material;
import com.axiom.engine.item.Mesh;
import com.axiom.engine.item.SkyBox;
import com.axiom.engine.item.Texture;
import com.axiom.engine.loaders.OBJLoader;
import com.axiom.engine.math.Camera;
import com.axiom.engine.input.KeyboardListener;
import com.axiom.engine.Renderer;
import com.axiom.engine.Scene;

import org.joml.Vector2f;
public class Game implements IGame {
    
    private final Renderer renderer;
    private Item[] gameItems;
    private final KeyboardListener input = new KeyboardListener();
    private GLFWKeyCallback keyCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWScrollCallback scrollCallback;
    private Hud hud;
    private Camera camera;
    private Vector3f cameraInc;
    private double ry;
    private double rx;

    private Vector3f ambientLight;
    private Light light;
    private static final float CAMERA_POS_STEP = 0.05f;
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private boolean moving2 = false;
    private Scene scene;
    
    private int n = 0;
    private float chng = .005f;
    public Game() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        float reflectance = 10f;
        scene = new Scene();

        String modelFile = "/models/cube.obj";
        String textureFile = "/textures/grassblock.png";
        
        float blockScale = 0.5f;        
        float skyBoxScale = 10.0f;
        float extension = 2.0f;
        
        float startx = extension * (-skyBoxScale + blockScale);
        float startz = extension * (skyBoxScale - blockScale);
        float starty = -1.0f;
        float inc = blockScale * 2;
        
        float posx = startx;
        float posz = startz;
        float incy = 0.0f;
        int NUM_ROWS = (int)(extension * skyBoxScale * 2 / inc);
        int NUM_COLS = (int)(extension * skyBoxScale * 2/ inc);
        Item[] gameItems  = new Item[2 + NUM_ROWS * NUM_COLS];
        
		Mesh mesh = OBJLoader.loadMesh(modelFile);
		Texture texture = new Texture(textureFile);
		Material material = new Material(texture, reflectance);
		mesh.setMaterial(material);
		
		CollidableItem i1 = new CollidableItem(mesh.clone());
		i1.setPosition(0, 3, -1);
	
		CollidableItem i2 = new CollidableItem(mesh.clone());
		i2.setPosition(0, 3, 2);
		
		gameItems[0] = i1;
		gameItems[1] = i2;
		
		
        for(int i=0; i<NUM_ROWS; i++) {
            for(int j=0; j<NUM_COLS; j++) {
                Item gameItem = new CollidableItem(mesh.clone());
                gameItem.setScale(blockScale);
                incy = Math.random() > 0.9f ? blockScale * 2 : 0f;
                gameItem.setPosition(posx, starty + incy, posz);
                gameItems[2 + i*NUM_COLS + j] = gameItem;
                posx += inc;
            }
            posx = startx;
            posz -= inc;
        }
        scene.setGameItems(gameItems);
        //System.out.println(gameItems[0].getPosition());
        ambientLight = new Vector3f(1,1,1);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(-1, 0, 1);
        //float lightIntensity = 1.0f;
        light = new Light(lightColour, lightPosition, ambientLight, 0.2f, 5.0f);
        glfwSetKeyCallback(window.getWindowHandle(), keyCallback = input.keyboard);
        //glfwSetMouseButtonCallback(window.getWindowHandle(), mouseButtonCallback = input.mouse);
        glfwSetScrollCallback(window.getWindowHandle(), scrollCallback = input.scroll);
        
        SkyBox skyBox = new SkyBox("/models/skybox.obj", "/textures/skybox.png");
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);
        
        scene.setSceneLight(light);
		//System.exit(0);
        hud = new Hud("DEMO");//GHIJKLMNOPQRSTUVWXYZ
        /*
        camera.getPosition().x = 0.65f;
        camera.getPosition().y = 1.15f;
        camera.getPosition().y = 4.34f;
        */
    }
    
    @Override
    public void input(Window window, MouseListener mouseInput) {
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
    public void update(float interval, MouseListener mouseInput) {
    		n++;
        camera.moveRotation((float)rx, (float)ry, 0.0f);
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
        // Update camera based on mouse            
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);

            // Update HUD compass
            hud.rotateCompass(camera.getRotation().y);
        }
        CollidableItem a=(CollidableItem)scene.getGameItems()[0], b=(CollidableItem)scene.getGameItems()[1];
        Vector3f pos = b.getPosition();
        if (moving2) b.setPosition(pos.x, pos.y, pos.z - .01f);
        if (a.collides(b, camera)) b.resetPosition();
        /*
         * Vector3f pos = gameItems[1].getPosition();
         
        CollidableItem a=(CollidableItem)gameItems[0], b=(CollidableItem)gameItems[1];
        if (moving2) b.setPosition(pos.x, pos.y, pos.z - .01f);
        
        if ((n % 200) == 0) chng *= -1;
        light.incPosition(chng, 0, 0);
        
        gameItems[2].setPosition(a.max(camera));
        gameItems[3].setPosition(a.min(camera));
        gameItems[4].setPosition(b.max(camera));
        gameItems[5].setPosition(b.min(camera));
        //System.out.println(Arrays.toString(a.getVertexPositions(camera)));
        //System.out.println(Arrays.toString(a.getVertexPositions(camera)));
        //System.out.println(b.oldPosition);
        if (a.collides(b, camera)) {
        		System.out.println("collision");
        		System.out.println(b.getPosition());
        		b.resetPosition();
        		System.out.println(b.getPosition());
        		/*
        		for (Item i: gameItems) {
        			CollidableItem j = (CollidableItem)i;
        			System.out.println(Arrays.toString(j.getMesh().getPositions()));
        			System.out.println(Arrays.toString(j.getVertexPositions(camera)));
        		}*/
        		//System.exit(0);
        			
        //}
        //else System.out.println("no collision");
        //System.out.println(camera.getPosition());
        //System.out.println(light.getPosition());
        //System.out.println(a.getPosition());
        //System.out.println(b.getPosition());
        Vector3f ambient = light.getAmbient();
        float x=ambient.x, y=ambient.y, z=ambient.z;
        if (x == 1f || x < 0.3) chng *= -1;
        x+=chng;y+=chng;z+=chng;
        ambient = new Vector3f(x, y, z);
        light.setAmbient(ambient);
        
        Vector3f position = light.getPosition();
        x=position.x; y=position.y; z=position.z;
        if (x == 1 || x == 0) chng *= -1;
        x+=chng;y+=chng;z+=chng;
        position = new Vector3f(x, y, z);
        light.setPosition(position);
    }
    
    public double[] getMousePosition() {
        DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(renderer.getWindow().getWindowHandle(), xBuffer, yBuffer);
        return new double[] { xBuffer.get(0), yBuffer.get(0) };
    }
    
    @Override
    public void render(Window window) {
    		hud.updateSize(window);
        renderer.render(window, camera, scene, hud);
    }
    
    @Override
    public void cleanup() {
        renderer.cleanup();
        for (Item gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
    
}
