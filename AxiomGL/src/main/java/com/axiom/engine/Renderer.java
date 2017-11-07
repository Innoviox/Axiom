package com.axiom.engine;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glViewport;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.axiom.engine.hud.IHud;
import com.axiom.engine.item.Item;
import com.axiom.engine.item.Light;
import com.axiom.engine.item.Mesh;
import com.axiom.engine.item.SkyBox;
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
	private ShaderReader skyBoxShaderProgram;

    private Window window;

    public Renderer() {
        transformation = Transformation.getInstance();
    }

    public void init(Window window) throws Exception {
    		setupSkyBoxShader();
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
        hudShaderProgram.createUniform("hasTexture");
    }
    
    private void setupSkyBoxShader() throws Exception {
        skyBoxShaderProgram = new ShaderReader();
        skyBoxShaderProgram.createVertexShader(Utils.loadResource("/shaders/skybox_vertex.vs"));
        skyBoxShaderProgram.createFragmentShader(Utils.loadResource("/shaders/skybox_frag.fs"));
        skyBoxShaderProgram.link();

        skyBoxShaderProgram.createUniform("projectionMatrix");
        skyBoxShaderProgram.createUniform("modelViewMatrix");
        skyBoxShaderProgram.createUniform("texture_sampler");
        skyBoxShaderProgram.createUniform("ambientLight");
    }
    
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, Scene scene, IHud hud) {
        clear();

        if ( window.isResized() ) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        transformation.updateProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        transformation.updateViewMatrix(camera);
        renderScene(window, camera, scene);
        renderSkyBox(window, camera, scene);
        renderHud(window, hud);
    }
    
    public void renderScene(Window window, Camera camera, Scene scene) {
        sceneShaderProgram.bind();
        
        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix();//FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        sceneShaderProgram.setUniform("projection", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix();//camera);
        Light currPointLight = new Light(scene.getSceneLight());
        Vector3f lightPos = currPointLight.getPosition();
        Vector4f aux = new Vector4f(lightPos, 1);
        aux.mul(viewMatrix);
        lightPos.x = aux.x;
        lightPos.y = aux.y;
        lightPos.z = aux.z;
        sceneShaderProgram.setUniform("light", currPointLight);       
        // Render each gameItem
        for(Item gameItem : scene.getGameItems()) {
        		//System.out.println(gameItem.getPosition());
            Mesh mesh = gameItem.getMesh();
            // Set model view matrix for this item
            Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(gameItem, viewMatrix);
            sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            sceneShaderProgram.setUniform(mesh.getMaterial());
            mesh.render();
        }
        //System.exit(0);
        //System.out.println(scene.getGameItems()[0].getPosition());
        sceneShaderProgram.unbind();
    }

    private void renderHud(Window window, IHud hud) {
        hudShaderProgram.bind();

        Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
        for (Item gameItem : hud.getGameItems()) {
        		
            Mesh mesh = gameItem.getMesh();
            // Set ortohtaphic and model matrix for this HUD item
            Matrix4f projModelMatrix = transformation.buildOrtoProjModelMatrix(gameItem, ortho);
            hudShaderProgram.setUniform("projModelMatrix", projModelMatrix);
            hudShaderProgram.setUniform("colour", gameItem.getMesh().getMaterial().getAmbientColour());
            hudShaderProgram.setUniform("hasTexture", gameItem.getMesh().getMaterial().isTextured() ? 1 : 0);
            // Render the mesh for this HUD item
            mesh.render();
        }

        hudShaderProgram.unbind();
    }
    
    private void renderSkyBox(Window window, Camera camera, Scene scene) {
        skyBoxShaderProgram.bind();

        skyBoxShaderProgram.setUniform("texture_sampler", 0);

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix();//FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        skyBoxShaderProgram.setUniform("projectionMatrix", projectionMatrix);
        SkyBox skyBox = scene.getSkyBox();
        Matrix4f viewMatrix = transformation.getViewMatrix();//camera);
        
        viewMatrix.m30(0);
        viewMatrix.m31(0);
        viewMatrix.m32(0);
        
        Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(skyBox, viewMatrix);
        skyBoxShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
        skyBoxShaderProgram.setUniform("ambientLight", scene.getSceneLight().getAmbient());

        scene.getSkyBox().getMesh().render();

        skyBoxShaderProgram.unbind();
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