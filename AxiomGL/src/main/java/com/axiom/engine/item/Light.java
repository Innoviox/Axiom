package com.axiom.engine.item;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Light {

    private Vector3f color;
    private Vector3f position;
    protected Vector3f ambient;
    private float falloff;
    private float radius;
    
    public Light(Vector3f color, Vector3f position, Vector3f ambient, float falloff, float radius) {
        this.color = color;
        this.position = position;
        this.ambient = ambient;
        this.falloff = falloff;
        this.radius = radius;
    }

    public Light(Light light) {
        this(new Vector3f(light.getColor()), new Vector3f(light.getPosition()), light.getAmbient(), light.getFalloff(), light.getRadius());
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }


	public Vector3f getAmbient() {
		// TODO Auto-generated method stub
		return null;
	}

	public float getFalloff() {
		// TODO Auto-generated method stub
		return 0;
	}

	public float getRadius() {
		// TODO Auto-generated method stub
		return 0;
	}
}
