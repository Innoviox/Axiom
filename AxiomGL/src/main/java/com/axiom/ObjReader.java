package com.axiom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;


import com.sun.javafx.geom.Vec3f;

public class ObjReader extends ArrayList<Vec3f> {

	BufferedReader inp;

	public ObjReader(Reader reader) {
		inp = new BufferedReader(reader);
	}

	public ObjReader(String fileName) throws FileNotFoundException {
		inp = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
	}

	public Face[] assembleObject() throws NullPointerException, IOException{
		String s;
		Scanner stringRead;
		while((s = inp.readLine()).contains("v")){
			stringRead = new Scanner(s.substring(1));
			this.add(new Vec3f(stringRead.nextFloat(),stringRead.nextFloat(),stringRead.nextFloat()));
		}
		ArrayList<Face> faces = new ArrayList<Face>();
		try{
			while((s = inp.readLine()).contains("f")){
				stringRead = new Scanner(s.substring(1));
				faces.add(new Face(this.get(stringRead.nextInt()-1),this.get(stringRead.nextInt()-1),this.get(stringRead.nextInt()-1)));
			}
		}catch(NullPointerException e){
			
		}
		
		return faces.toArray(new Face[faces.size()]);
	}

}
