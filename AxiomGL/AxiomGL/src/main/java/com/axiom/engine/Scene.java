package com.axiom.engine;

import com.axiom.engine.input.MouseInput;

public interface Scene {
    void init(Window window) throws Exception;
    void render(Window window);
    void cleanup();
	void update(float interval, MouseInput mouseInput);
	void input(Window window, MouseInput mouseInput);
}
