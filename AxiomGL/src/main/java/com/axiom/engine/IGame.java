package com.axiom.engine;

import com.axiom.engine.input.MouseListener;

public interface IGame {
    void init(Window window) throws Exception;
    void render(Window window);
    void cleanup();
	void update(float interval, MouseListener mouseInput);
	void input(Window window, MouseListener mouseInput);
}
