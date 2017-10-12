package com.axiom.engine;

import java.io.InputStream;
import java.util.Scanner;

public class Utils {
	public static class Timer {

	    private double lastLoopTime;
	    
	    public void init() {
	        lastLoopTime = getTime();
	    }

	    public double getTime() {
	        return System.nanoTime() / 1000_000_000.0;
	    }

	    public float getElapsedTime() {
	        double time = getTime();
	        float elapsedTime = (float) (time - lastLoopTime);
	        lastLoopTime = time;
	        return elapsedTime;
	    }

	    public double getLastLoopTime() {
	        return lastLoopTime;
	    }
	}
	
	public static Timer makeTimer() {
		return new Utils.Timer();
	}
	
    public static String loadResource(String fileName) throws Exception {
        String result;
        try (InputStream in = Utils.class.getClass().getResourceAsStream(fileName);
                Scanner scanner = new Scanner(in, "UTF-8")) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }
}
