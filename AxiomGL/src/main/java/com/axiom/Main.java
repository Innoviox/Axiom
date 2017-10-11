package com.axiom;

import javax.management.relation.RoleUnresolved;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.*;
import org.lwjgl.opengl.*;

class Main implements Runnable {
	private Thread thread;
	private boolean running = true;
	private InputHandler ih = new InputHandler();
	// private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWScrollCallback scrollCallback;
	private float rx = 0f, ry = 0f, rz = 0f;
	private float zoom = 1.0f;
	public Long window;
	private float[] cube = new float[] { 0.25f, -0.25f, 0.25f, 0.25f, 0.25f, 0.25f, -0.25f, 0.25f, 0.25f, -0.25f,
			-0.25f, 0.25f, 0.25f, -0.25f, -0.25f, 0.25f, 0.25f, -0.25f, -0.25f, 0.25f, -0.25f, -0.25f, -0.25f, -0.25f,
			-0.25f, -0.25f, 0.25f, -0.25f, 0.25f, 0.25f, -0.25f, 0.25f, -0.25f, -0.25f, -0.25f, -0.25f, 0.25f, -0.25f,
			-0.25f, 0.25f, 0.25f, -0.25f, 0.25f, 0.25f, 0.25f, 0.25f, -0.25f, 0.25f, 0.25f, -0.25f, -0.25f, 0.25f,
			-0.25f, 0.25f, -0.25f, -0.25f, 0.25f, -0.25f, -0.25f, -0.25f, 0.25f, 0.25f, 0.25f, 0.25f, 0.25f, -0.25f,
			-0.25f, 0.25f, -0.25f, -0.25f, 0.25f, 0.25f };
	private float[] colors = new float[] { 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 0.0f, 0.0f, 0.0f, 1.0f };
	private FloatBuffer vertex_data = BufferUtils.createFloatBuffer(24 * 3);
	private FloatBuffer color_data = BufferUtils.createFloatBuffer(24 * 3);
	private final int VERTICES = 24;
	private final int VERTEX_SIZE = 3;
	private final int COLOR_SIZE = 3;
	private final int SIDE_LEN = 4;
	private int vboid, cboid;
	private float x = 0.0f, y = 0.0f, z = 0.0f;
	private double[] oldMousePos = new double[] { 0, 0 };

	/*
	 * -
	 * https://stackoverflow.com/questions/19346343/java-how-to-use-the-vertex-
	 * buffers-object-in-lwjgl -
	 * http://wiki.lwjgl.org/wiki/Using_Vertex_Buffer_Objects_(VBO).html
	 */
	public static void main(String args[]) {

		Main game = new Main();
		// game.start();
		
		Face[] teapot = new Face[1];
		
		try {
			teapot = new ObjReader("teapot.obj").assembleObject();
		} catch (NullPointerException | IOException e) {
			e.printStackTrace();
		}
		
		for(Face f : teapot)
			//System.out.println(f);
		
		game.run();
	}

	public void start() {
		running = true;
		// thread = new Thread(this,"EndlessRunner");
		// thread.start();
	}

	private float[][] divide(float[] arr, int len) {
		float[][] divided = new float[arr.length / len][len];
		int s = 0; // sections
		for (int i = 0; i < arr.length / len; i++) {
			float[] section = new float[len];
			for (int j = 0; j < len; j++)
				section[j] = arr[i * len + j];
			divided[s] = section;
			s++;
		}
		return divided;
	}

	public void init() throws NullPointerException {
		if (!glfwInit())
			throw new NullPointerException("GLFW Could not initialize");

		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		window = glfwCreateWindow(800, 600, "running", NULL, NULL);
		if (window == null)
			throw new NullPointerException("Window not created");
		glfwSetWindowPos(window, 100, 100);
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GL.createCapabilities();
		glClearColor(0.1f, 0.4f, 0.3f, 1.0f);

		glfwSetKeyCallback(window, keyCallback = ih.keyboard);
		glfwSetMouseButtonCallback(window, mouseButtonCallback = ih.mouse);
		glfwSetScrollCallback(window, scrollCallback = ih.scroll);

		System.out.println("OpenGL: " + glGetString(GL_VERSION));

		for (float[] s : divide(cube, VERTEX_SIZE)) {
			vertex_data.put(s);
		}
		vertex_data.flip();

		for (float[] s : divide(colors, COLOR_SIZE)) {
			for (int i = 0; i < SIDE_LEN; i++)
				color_data.put(s);
		}
		color_data.flip();

		vboid = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboid);
		glBufferData(GL_ARRAY_BUFFER, vertex_data, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		cboid = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, cboid);
		glBufferData(GL_ARRAY_BUFFER, color_data, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void update() {
		glfwPollEvents();
		// if (ih.keyDown(GLFW_KEY_SPACE)) System.out.println("Space Key
		// Pressed");
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		// Enable depth test
		glEnable(GL_DEPTH_TEST);
		// Accept fragment if it closer to the camera than the former one
		glDepthFunc(GL_LESS);
		int mult;
		if (Math.cos(Math.toRadians(rx)) < 0) {
			mult = -1;
		} else {
			mult = 1;
		}
		if (ih.keyDown(GLFW_KEY_LEFT))
			ry += mult;
		if (ih.keyDown(GLFW_KEY_RIGHT))
			ry -= mult;
		if (ih.keyDown(GLFW_KEY_DOWN))
			rx += 1;
		if (ih.keyDown(GLFW_KEY_UP))
			rx -= 1;
		if(ih.keyDown(GLFW_KEY_W))
			y += .005;
		if(ih.keyDown(GLFW_KEY_S))
			y -= .005;
		if(ih.keyDown(GLFW_KEY_D))
			x += .005;
		if(ih.keyDown(GLFW_KEY_A))
			x -= .005;
		
		
		if (ih.mouseButtonDown(0)) {
			double[] newMousePos = getMousePosition();
			double ny = (newMousePos[0] - oldMousePos[0]);
			double nx = (newMousePos[1] - oldMousePos[1]);
			ry -= mult * ny;
			rx -= nx;
		}
		ry = (Math.abs(ry) % 360) * Math.signum(ry);
		rx = (Math.abs(rx) % 360) * Math.signum(rx);

		double zoomStep = ih.getScrollStates()[1] / 1000;
		if(zoom + zoomStep > .1){
			zoom += zoomStep;
		}
		
		glScalef(zoom, zoom, 1.0f);

		glTranslatef(x,y,z);
		
		glRotatef(rx + y * 100, 1.0f, 0.0f, 0.0f);
		glRotatef(ry - x * 100, 0.0f, 1.0f, 0.0f);
		glRotatef(rz, 0.0f, 0.0f, 1.0f);
		System.out.println(y + "," + ry);
		/*
		 * glEnableClientState(GL_VERTEX_ARRAY); glBindBuffer(GL_ARRAY_BUFFER,
		 * vboid); glVertexPointer(3, GL_FLOAT, 0, 0); glDrawArrays(GL_QUADS, 0,
		 * 24); glDisableClientState(GL_VERTEX_ARRAY);
		 */

		glBindBuffer(GL_ARRAY_BUFFER, vboid);
		glVertexPointer(VERTEX_SIZE, GL_FLOAT, 0, 0l);

		glBindBuffer(GL_ARRAY_BUFFER, cboid);
		glColorPointer(COLOR_SIZE, GL_FLOAT, 0, 0l);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);

		glDrawArrays(GL_QUADS, 0, VERTICES);

		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);

		oldMousePos = getMousePosition();

		glfwSwapBuffers(window);
		glfwShowWindow(window);
	}

	private double changeRot(double oldRot, double newRot) {
		if (oldRot < 0)
			return oldRot + newRot;
		return oldRot - newRot;
	}

	public double[] getMousePosition() {
		DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(window, xBuffer, yBuffer);
		return new double[] { xBuffer.get(0), yBuffer.get(0) };
	}

	public void run() {
		try {
			init();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		while (running) {
			render();
			update();
			if (glfwWindowShouldClose(window))
				running = false;
		}
	}

}
