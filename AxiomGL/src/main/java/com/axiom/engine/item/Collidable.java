package com.axiom.engine.item;

import java.util.Arrays;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.axiom.engine.math.Camera;
import com.axiom.engine.math.Transformation;

public interface Collidable {
	
	/**
	 * @param other object to be compared
	 * @param camera Camera
	 * @return if this object contains part of the other object
	 */
	public default boolean contains(Collidable other, Camera camera) {
		boolean flag = false;
		
		Vector3f maxOther = new Vector3f(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
		Vector3f minOther = new Vector3f(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
		
		Vector3f maxThis = new Vector3f(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
		Vector3f minThis = new Vector3f(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
		
		for(Vector4f currentVector : other.getVertexPositions(camera)){
			maxOther.x = Math.max(maxOther.x , currentVector.x / currentVector.w);
			maxOther.y = Math.max(maxOther.y, currentVector.y / currentVector.w);
			maxOther.z = Math.max(maxOther.z, currentVector.z / currentVector.w);

			minOther.x = Math.min(minOther.x , currentVector.x / currentVector.w);
			minOther.y = Math.min(minOther.y, currentVector.y / currentVector.w);
			minOther.z = Math.min(minOther.z, currentVector.z / currentVector.w);
		}
		
		//generate every vertex in the other object's hitbox, forming an 8 point hitbox
		Vector3f[] otherHitboxVert = new Vector3f[8];
		
		otherHitboxVert[0] = maxOther;
		otherHitboxVert[1] = new Vector3f(maxOther.x,maxOther.y, minOther.z); 
		otherHitboxVert[2] = new Vector3f(maxOther.x,minOther.y, maxOther.z); 
		otherHitboxVert[3] = new Vector3f(maxOther.x,minOther.y, minOther.z); 
		otherHitboxVert[4] = new Vector3f(minOther.x,maxOther.y, maxOther.z); 
		otherHitboxVert[5] = new Vector3f(minOther.x,maxOther.y, minOther.z); 
		otherHitboxVert[6] = new Vector3f(minOther.x,minOther.y, maxOther.z); 
		otherHitboxVert[7] = minOther;
		
		//find max and min coordinates for this object, forming a 2 point hitbox
		for(Vector4f currentVector : this.getVertexPositions(camera)){
			maxThis.x = Math.max(maxThis.x , currentVector.x / currentVector.w);
			maxThis.y = Math.max(maxThis.y, currentVector.y / currentVector.w);
			maxThis.z = Math.max(maxThis.z, currentVector.z / currentVector.w);

			minThis.x = Math.min(minThis.x , currentVector.x / currentVector.w);
			minThis.y = Math.min(minThis.y, currentVector.y / currentVector.w);
			minThis.z = Math.min(minThis.z, currentVector.z / currentVector.w);
		}
		//check every vector in other hitbox to see if it's contained
		for(Vector3f v : otherHitboxVert){
			boolean vectorContained = true;
			vectorContained &= v.x <= maxThis.x && v.x >= minThis.x;
			vectorContained &= v.y <= maxThis.y && v.y >= minThis.y;
			vectorContained &= v.z <= maxThis.z && v.z >= minThis.z;
			//System.out.println(v + "," + vectorContained + "," + flag);
			flag |= vectorContained;
		}
		
		//System.out.println(minThis + "," + maxThis);
		//System.out.println(minOther + "," + maxOther);
		//return true if any point from other object is in this one
		return flag;
	}
	
	
	/**
	 * determines whether 2 objects collide
	 * @param other other object
	 * @param camera the camera
	 * @return whether 2 objects collide
	 */
	public default boolean collides(Collidable other, Camera camera){
		return this.contains(other, camera) || other.contains(this, camera);
	}
	
	public default Vector4f[] getVertexPositions(Camera camera) {
		float[] positions = getPositions();//getMesh().getPositions();
		Vector4f[] vertices = new Vector4f[positions.length / 3];
		for (int i = 0; i < positions.length; i += 3) {
			vertices[i / 3] = new Vector4f(positions[i], positions[i + 1], positions[i + 2], 1);
		}

		Transformation transformation = Transformation.getInstance();

		Matrix4f viewMatrix = transformation.getViewMatrix(camera);
		Matrix4f modelViewMatrix = transformation.getModelViewMatrix((Item)this, viewMatrix);

		for (int j = 0; j < vertices.length; j++) {
			//vertices[j] = modelViewMatrix.transform(vertices[j]);	
		}
		//System.out.println(Arrays.toString(vertices));
		return vertices;
	}

	public default Vector3f[] genHitbox(Camera camera) {
		Vector3f maxOther = new Vector3f(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
		Vector3f minOther = new Vector3f(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
		
		for(Vector4f currentVector : this.getVertexPositions(camera)){
			maxOther.x = Math.max(maxOther.x , currentVector.x / currentVector.w);
			maxOther.y = Math.max(maxOther.y, currentVector.y / currentVector.w);
			maxOther.z = Math.max(maxOther.z, currentVector.z / currentVector.w);

			minOther.x = Math.min(minOther.x , currentVector.x / currentVector.w);
			minOther.y = Math.min(minOther.y, currentVector.y / currentVector.w);
			minOther.z = Math.min(minOther.z, currentVector.z / currentVector.w);
		}
		
		//generate every vertex in the other object's hitbox, forming an 8 point hitbox
		Vector3f[] otherHitboxVert = new Vector3f[8];
		
		otherHitboxVert[0] = maxOther;
		otherHitboxVert[1] = new Vector3f(maxOther.x,maxOther.y, minOther.z); 
		otherHitboxVert[2] = new Vector3f(maxOther.x,minOther.y, maxOther.z); 
		otherHitboxVert[3] = new Vector3f(maxOther.x,minOther.y, minOther.z); 
		otherHitboxVert[4] = new Vector3f(minOther.x,maxOther.y, maxOther.z); 
		otherHitboxVert[5] = new Vector3f(minOther.x,maxOther.y, minOther.z); 
		otherHitboxVert[6] = new Vector3f(minOther.x,minOther.y, maxOther.z); 
		otherHitboxVert[7] = minOther;
		
		return otherHitboxVert;
	}
	
	public default Vector3f min(Camera camera) {
		Vector3f minOther = new Vector3f(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
		
		for(Vector4f currentVector : this.getVertexPositions(camera)){
			minOther.x = Math.min(minOther.x , currentVector.x / currentVector.w);
			minOther.y = Math.min(minOther.y, currentVector.y / currentVector.w);
			minOther.z = Math.min(minOther.z, currentVector.z / currentVector.w);
		}
		return minOther;
	}
	
	public default Vector3f max(Camera camera) {
		Vector3f maxOther = new Vector3f(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
		
		for(Vector4f currentVector : this.getVertexPositions(camera)){
			maxOther.x = Math.max(maxOther.x , currentVector.x / currentVector.w);
			maxOther.y = Math.max(maxOther.y, currentVector.y / currentVector.w);
			maxOther.z = Math.max(maxOther.z, currentVector.z / currentVector.w);
		}
		
		return maxOther;
	}
	public abstract float[] getPositions();
}
