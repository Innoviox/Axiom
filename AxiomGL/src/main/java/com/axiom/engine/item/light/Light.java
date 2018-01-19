package com.axiom.engine.item.light;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Light {

    private Vector3f color;
    private Vector3f position;
    private Vector3f ambient;
    private float falloff;
    private float radius;
    
    public Light(Vector3f color, Vector3f position, Vector3f ambient, float falloff, float radius) {
        this.color = color;
        this.position = position;
        this.setAmbient(ambient);
        this.setFalloff(falloff);
        this.setRadius(radius);
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

    public void incPosition(float x, float y, float z) {
    		position.x += x;
    		position.y += y;
    		position.z += z;
    }

	public float getFalloff() {
		return falloff;
	}

	public void setFalloff(float falloff) {
		this.falloff = falloff;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public Vector3f getAmbient() {
		return ambient;
	}

	public void setAmbient(Vector3f ambient) {
		this.ambient = ambient;
	}

}
