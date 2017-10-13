package com.axiom.engine;

public interface Scene {
    void init(Window window) throws Exception;
    void input(Window window);
    void update(float interval);
    void render(Window window);
    void cleanup();
}
