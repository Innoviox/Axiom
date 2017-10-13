package com.axiom.engine;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glViewport;

import org.joml.Matrix4f;

import com.axiom.engine.loaders.ShaderReader;
import com.axiom.engine.math.Transformation;

public class Renderer {

    private ShaderReader shaderProgram;
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
	private Transformation transformation;
    private Window window;
    
    public Renderer() {
        transformation = new Transformation();
    }

    public void init(Window window) throws Exception {
        shaderProgram = new ShaderReader();
        shaderProgram.createVertexShader(Utils.loadResource("/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/fragment.fs"));
        shaderProgram.link();
        
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");
        shaderProgram.createUniform("texture_sampler");
        
        this.window = window;
        
        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Item[] gameItems) {
        clear();

            if ( window.isResized() ) {
                glViewport(0, 0, window.getWidth(), window.getHeight());
                window.setResized(false);
            }

        shaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);        

        shaderProgram.setUniform("texture_sampler", 0);

        // Render each gameItem
        for(Item gameItem : gameItems) {
            // Set world matrix for this item
            Matrix4f worldMatrix =
                transformation.getWorldMatrix(
                    gameItem.getPosition(),
                    gameItem.getRotation(),
                    gameItem.getScale());
            shaderProgram.setUniform("worldMatrix", worldMatrix);
            // Render the mesh for this game item
            gameItem.getMesh().render();
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
