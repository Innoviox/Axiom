package com.axiom.engine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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
    
    public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }
}
