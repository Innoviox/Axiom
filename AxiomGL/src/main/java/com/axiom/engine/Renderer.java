package com.axiom.engine;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glViewport;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

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
    private ShaderReader shaderProgram;
    private Window window;

    public Renderer() {
        transformation = Transformation.getInstance();
    }

    public void init(Window window) throws Exception {
        // Create shader
        shaderProgram = new ShaderReader();
        shaderProgram.createVertexShader(Utils.loadResource("/shaders/phong.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/shaders/phong.fs"));
        shaderProgram.link();
        
        // Create uniforms for modelView and projection matrices and texture
        shaderProgram.createUniform("projection");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createMaterialUniform("material");
        shaderProgram.createUniform("texture_sampler");
        shaderProgram.createLightUniform("light");
        
        this.window = window;
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, Item[] gameItems, Light light) {
        clear();

        if ( window.isResized() ) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();
        
        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projection", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);
        Light currPointLight = new Light(light);
        Vector3f lightPos = currPointLight.getPosition();
        Vector4f aux = new Vector4f(lightPos, 1);
        aux.mul(viewMatrix);
        lightPos.x = aux.x;
        lightPos.y = aux.y;
        lightPos.z = aux.z;
        shaderProgram.setUniform("light", currPointLight);  
        shaderProgram.setUniform("texture_sampler", .5f);
        // Render each gameItem
        for(Item gameItem : gameItems) {
            Mesh mesh = gameItem.getMesh();
            // Set model view matrix for this item
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            shaderProgram.setUniform("material", mesh.getMaterial());
            mesh.render();
        }

        shaderProgram.unbind();
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
    
    public Window getWindow() {
    		return window;
    }
}