/*
 * Utilities for the game
 * <p>
 * <br>
 * This class contains utilities for the game engine:
 * <br>
 * <ul>
 * <li> Timer: helping with sync
 * <li> Reading resources from files
 * <li> Making a java.util.List into an array
 * </ul>
 * </p>
 * <p>
 * @author Antonio Hernández Bejarano (@lwjglgamedev)
 * @author The Axiom Corp, 2017.
 * </p>
 */
package com.axiom.engine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {
	/*
	 * A Timer class
	 * <br>
	 * This class contains a timer
	 * that times the lengths of frames
	 * to make sure we are hitting the
	 * target FPS.
	 */
	public static class Timer {

	    private double lastLoopTime;
	    
	    /*
	     * Initialize/restart the timer
	     */
	    public void init() {
	        lastLoopTime = getTime();
	    }
	    
	    /*
	     * Get the time
	     */
	    public double getTime() {
	        return System.nanoTime() / 1_000_000_000.0;
	    }

	    /*
	     * Get and update the total time since last frame
	     */
	    public float getElapsedTime() {
	        double time = getTime();
	        float elapsedTime = (float) (time - lastLoopTime);
	        lastLoopTime = time;
	        return elapsedTime;
	    }

	    /*
	     * Get last frame time
	     */
	    public double getLastLoopTime() {
	        return lastLoopTime;
	    }
	}
	
	/*
	 * Make a timer
	 */
	public static Timer makeTimer() {
		return new Utils.Timer();
	}
	
	/*
	 * Read a file into a String
	 */
    public static String loadResource(String fileName) throws Exception {
        String result;
        try (InputStream in = Utils.class.getClass().getResourceAsStream(fileName);
                Scanner scanner = new Scanner(in, "UTF-8")) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }
    
    /*
     * Read a file into a List
     */
    public static List<String> readAllLines(String fileName) throws Exception {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Utils.class.getClass().getResourceAsStream(fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }
    
    /*
     * Turn a List into an array
     */
    public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }
}
