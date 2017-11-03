package com.axiom.engine.item;

import org.joml.Vector3f;

import com.axiom.engine.item.Mesh;
public class Item {

    private final Mesh mesh;

    protected final Vector3f position;
    protected Vector3f oldPosition;
    private float scale;

    protected final Vector3f rotation;

    public Item(Mesh mesh) {
        this.mesh = mesh;
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
        oldPosition = new Vector3f(0, 0, 0);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
        this.mesh.updatePositions(this.position, this.oldPosition);
        this.oldPosition = new Vector3f(x, y, z);
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
        this.mesh.mulPositions(scale);
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Mesh getMesh() {
        return mesh;
    }

	public void setPosition(Vector3f pos) {
		setPosition(pos.x, pos.y, pos.z);
	}
}