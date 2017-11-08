package com.axiom.engine;

import com.axiom.engine.input.InputHandler;

public interface IGame {
    void init(Window window) throws Exception;
    void render(Window window);
    void cleanup();
	void update(float interval, InputHandler keyboardInput);
	void input(Window window, InputHandler keyboardInput);
}
