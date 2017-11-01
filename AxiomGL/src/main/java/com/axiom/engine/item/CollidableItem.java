package com.axiom.engine.item;

import org.joml.Vector3f;

public class CollidableItem extends Item implements Collidable {

	private Vector3f oldPosition;
	private Vector3f oldRotation;
	public CollidableItem(Mesh mesh) {
		super(mesh);
		oldPosition = new Vector3f(0, 0, 0);
        oldRotation = new Vector3f(0, 0, 0);	
    }

	
    public void setPosition(float x, float y, float z) {
    		oldPosition = new Vector3f(position.x, position.y, position.x);
        super.setPosition(x, y, z);
      
    }

    public void setRotation(float x, float y, float z) {
    		oldRotation = new Vector3f(rotation.x, rotation.y, rotation.x);
        super.setRotation(x, y, z);
    }
    
    public void resetPosition() {
    		super.setPosition(oldPosition.x, oldPosition.y, oldPosition.z);
    }
    
    public void resetRotation() {
		super.setPosition(oldRotation.x, oldRotation.y, oldRotation.z);
}
}
