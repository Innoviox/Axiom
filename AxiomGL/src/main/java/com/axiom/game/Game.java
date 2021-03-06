package com.axiom.game;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import com.axiom.engine.IGame;
import com.axiom.engine.Window;
import com.axiom.engine.input.InputHandler;
import com.axiom.engine.item.CollidableItem;
import com.axiom.engine.item.Item;
import com.axiom.engine.item.SkyBox;
import com.axiom.engine.item.model.Texture;
import com.axiom.engine.item.interfaces.Collidable;
import com.axiom.engine.item.light.Light;
import com.axiom.engine.item.model.Material;
import com.axiom.engine.item.model.Mesh;
import com.axiom.engine.item.model.Texture;
import com.axiom.engine.item.terrain.Terrain;
import com.axiom.engine.loaders.OBJLoader;
import com.axiom.engine.math.Camera;
import com.axiom.engine.input.InputHandler;
import com.axiom.engine.Renderer;
import com.axiom.engine.Scene;

import org.joml.Vector2f;
public class Game implements IGame {
    
    private final Renderer renderer;
    private Item[] gameItems;

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
    private Terrain terrain;
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
        float reflectance = .1f;
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
        ambientLight = new Vector3f(.7f,.7f,.7f);
        Vector3f lightColour = new Vector3f(1,1,1);
        Vector3f lightPosition = new Vector3f(5, 2, 0);
        //float lightIntensity = 1.0f;
        light = new Light(lightColour, lightPosition, ambientLight, 0.2f, 5.0f);
        
        SkyBox skyBox = new SkyBox("/models/skybox.obj", "/textures/skybox.png");
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);
        
        scene.setSceneLight(light);
		//System.exit(0);
        hud = new Hud("DEMO");//GHIJKLMNOPQRSTUVWXYZ
        camera.getPosition().x = 0.0f;
        camera.getPosition().y = 5.0f;
        camera.getPosition().z = 0.0f;
        camera.getRotation().x = 90;
    }
    
    @Override
    public void input(Window window, InputHandler input) {
        cameraInc.set(0, 0, 0);
        if (input.keyDown(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (input.keyDown(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (input.keyDown(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (input.keyDown(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (input.keyDown(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (input.keyDown(GLFW_KEY_X)) {
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
        moving2 = input.keyDown(GLFW_KEY_M);
        input.input(window);
        
        if (input.keyDown(GLFW_KEY_Q))
            System.exit(0);
    }
    
    
    @Override
    public void update(float interval, InputHandler input) {
    		n++;
        camera.moveRotation((float)rx, (float)ry, 0.0f);
        //camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
        // Update camera based on mouse            
        if (input.mouseButtonDown(1)) {
            Vector2f rotVec = input.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);

            // Update HUD compass
            hud.rotateCompass(camera.getRotation().y);
        }
        // Update camera position
        Vector3f prevPos = new Vector3f(camera.getPosition());
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);        
        // Check if there has been a collision. If true, set the y position to
        // the maximum height
        /*
        float height = terrain.getHeight(camera.getPosition());
        if ( camera.getPosition().y <= height )  {
            camera.setPosition(prevPos.x, prevPos.y, prevPos.z);
        }
        */
        /*
        //CollidableItem a=(CollidableItem)scene.getGameItems()[0], b=(CollidableItem)scene.getGameItems()[1];
        Vector3f pos = b.getPosition();
        if (moving2) b.setPosition(pos.x, pos.y, pos.z - .01f);
        if (a.collides(b, camera)) b.resetPosition();
        */
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
        //light.setAmbient(ambient);
        
        Vector3f position = light.getPosition();
        x=position.x; y=position.y; z=position.z;
        if (x == 1 || x == 0) chng *= -1;
        x+=chng;y+=chng;z+=chng;
        position = new Vector3f(x, y, z);
        //light.setPosition(position);
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
