package com.axiom.engine.item;

import org.joml.Vector3f;

import com.axiom.engine.item.model.Material;
import com.axiom.engine.item.model.Mesh;
import com.axiom.engine.item.model.Texture;
import com.axiom.engine.loaders.OBJLoader;
public class Item {

    private Mesh mesh;

    protected final Vector3f position;
    protected Vector3f oldPosition;
    private float scale;
    protected float[] positions;
    protected final Vector3f rotation;

    public Item(Mesh mesh) {
        this.mesh = mesh;
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
        oldPosition = new Vector3f(0, 0, 0);
        positions = mesh.getPositions();
    }
  
    public Item() {
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
        oldPosition = new Vector3f(0, 0, 0);
        this.mesh = null;
    }
    
    public Item(String modelFile, String textureFile, float reflectance) throws Exception {
    		this();
		Mesh mesh = OBJLoader.loadMesh(modelFile);
		Texture texture = new Texture(textureFile);
		Material material = new Material(texture, reflectance);
		mesh.setMaterial(material);
		this.mesh = mesh;
		positions = mesh.getPositions();
	}

	public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
		for (int i = 0; i < positions.length; i+=3) {
			positions[i]     -= oldPosition.x - position.x;
			positions[i + 1] -= oldPosition.y - position.y;
			positions[i + 2] -= oldPosition.z - position.z;
		}
        this.oldPosition = new Vector3f(x, y, z);
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
		for (int i = 0; i < positions.length; i+=3) {
			positions[i]     *= scale;
			positions[i + 1] *= scale;
			positions[i + 2] *= scale;
		}
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
    public void setMesh(Mesh mesh) {
    		this.mesh = mesh;
    		this.positions = mesh.getPositions();
    }
	  public void setPosition(Vector3f pos) {
		    setPosition(pos.x, pos.y, pos.z);
	  }

	  public void setRotation(Vector3f rot) {
		    setRotation(rot.x, rot.y, rot.z);
	  }

}