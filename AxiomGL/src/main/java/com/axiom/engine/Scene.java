package com.axiom.engine;

import com.axiom.engine.input.MouseHandler;

public interface Scene {
    void init(Window window) throws Exception;
    void render(Window window);
    void cleanup();
	void update(float interval, MouseHandler mouseInput);
	void input(Window window, MouseHandler mouseInput);
}
