package com.axiom;


import com.sun.javafx.geom.Matrix3f;
import com.sun.javafx.geom.Vec3f;

public class Face extends Matrix3f{

	public Face(Vec3f u, Vec3f v, Vec3f w) {
		super(	u.x,v.x,w.x,
				u.y,v.y,w.y,
				u.z,v.z,w.z);
	}
	
	public Vec3f[] getColumnVectors(){
		return new Vec3f[] {new Vec3f(this.m00,this.m10,this.m20), new Vec3f(this.m01,this.m11,this.m21),new Vec3f(this.m02,this.m12,this.m22)};
	}

}
