package com.axiom.engine.item;

import org.joml.Vector3f;

import com.axiom.engine.item.interfaces.Collidable;

public class CollidableItem extends Item implements Collidable {

	public final Vector3f oldPosition;
	private final Vector3f oldRotation;
	public CollidableItem(Mesh mesh) {
		super(mesh);
		oldPosition = new Vector3f(0, 0, 0);
        oldRotation = new Vector3f(0, 0, 0);	
    }

	
    public void setPosition(float x, float y, float z) {
    		oldPosition.x = position.x;
    		oldPosition.y = position.y;
    		oldPosition.z = position.z;
        super.setPosition(x, y, z);
    }

    public void setRotation(float x, float y, float z) {
		oldRotation.x = rotation.x;
		oldRotation.y = rotation.y;
		oldRotation.z = rotation.z;
		super.setRotation(x, y, z);
    }
    
    public void resetPosition() {
    		super.setPosition(oldPosition.x, oldPosition.y, oldPosition.z);
    }
    
    public void resetRotation() {
		super.setPosition(oldRotation.x, oldRotation.y, oldRotation.z);
    }


	@Override
	public float[] getPositions() {
		return positions;
	}	
}
