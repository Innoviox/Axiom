package com.axiom.engine;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glViewport;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.axiom.engine.hud.HudScene;
import com.axiom.engine.item.Item;
import com.axiom.engine.item.Light;
import com.axiom.engine.item.Mesh;
import com.axiom.engine.loaders.ShaderReader;
import com.axiom.engine.math.Transformation;
import com.axiom.engine.math.Camera;

public class Renderer {

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private final Transformation transformation;
    private ShaderReader sceneShaderProgram;
    private ShaderReader hudShaderProgram;
    private Window window;

    public Renderer() {
        transformation = Transformation.getInstance();
    }

    public void init(Window window) throws Exception {
        setupSceneShader();
        setupHudShader();
        this.window = window;
    }
    
    private void setupSceneShader() throws Exception {
        // Create shader
    		sceneShaderProgram = new ShaderReader();
    		sceneShaderProgram.createVertexShader(Utils.loadResource("/shaders/phong.vs"));
    		sceneShaderProgram.createFragmentShader(Utils.loadResource("/shaders/phong.fs"));
    		sceneShaderProgram.link();
        
        // Create uniforms for modelView and projection matrices and texture
    		sceneShaderProgram.createUniform("projection");
        sceneShaderProgram.createUniform("modelViewMatrix");
        sceneShaderProgram.createMaterialUniform();
        sceneShaderProgram.createUniform("flatShading");
        sceneShaderProgram.createLightUniform("light");
        
        //this.window = window;
    }

    private void setupHudShader() throws Exception {
        hudShaderProgram = new ShaderReader();
        hudShaderProgram.createVertexShader(Utils.loadResource("/shaders/hud_vertex.vs"));
        hudShaderProgram.createFragmentShader(Utils.loadResource("/shaders/hud_frag.fs"));
        hudShaderProgram.link();

        // Create uniforms for Ortographic-model projection matrix and base colour
        hudShaderProgram.createUniform("projModelMatrix");
        hudShaderProgram.createUniform("colour");
    }
    
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, Item[] gameItems, Light light, HudScene hud) {
        clear();

        if ( window.isResized() ) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        renderScene(window, camera, gameItems, light);

        renderHud(window, hud);
    }
    
    public void renderScene(Window window, Camera camera, Item[] gameItems, Light light) {
        sceneShaderProgram.bind();
        
        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        sceneShaderProgram.setUniform("projection", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);
        Light currPointLight = new Light(light);
        Vector3f lightPos = currPointLight.getPosition();
        Vector4f aux = new Vector4f(lightPos, 1);
        aux.mul(viewMatrix);
        lightPos.x = aux.x;
        lightPos.y = aux.y;
        lightPos.z = aux.z;
        sceneShaderProgram.setUniform("light", currPointLight);       
        // Render each gameItem
        for(Item gameItem : gameItems) {
            Mesh mesh = gameItem.getMesh();
            // Set model view matrix for this item
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
            sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            sceneShaderProgram.setUniform(mesh.getMaterial());
            mesh.render();
        }

        sceneShaderProgram.unbind();
    }

    private void renderHud(Window window, HudScene hud) {
        hudShaderProgram.bind();

        Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
        for (Item gameItem : hud.getGameItems()) {
            Mesh mesh = gameItem.getMesh();
            // Set ortohtaphic and model matrix for this HUD item
            Matrix4f projModelMatrix = transformation.getOrtoProjModelMatrix(gameItem, ortho);
            hudShaderProgram.setUniform("projModelMatrix", projModelMatrix);
            hudShaderProgram.setUniform("colour", gameItem.getMesh().getMaterial().getAmbientColour());

            // Render the mesh for this HUD item
            mesh.render();
        }

        hudShaderProgram.unbind();
    }
    
    public void cleanup() {
        if (sceneShaderProgram != null) {
        		sceneShaderProgram.cleanup();
        }
    }
    
    public Window getWindow() {
    		return window;
    }
}