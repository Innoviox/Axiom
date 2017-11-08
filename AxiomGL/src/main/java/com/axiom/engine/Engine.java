/*
 * The Engine class
 * <p>
 * <br>
 * This class is the controller class
 * for the game. It contains a input,
 * game, window, and the main thread
 * where the game is run. 
 * <br>
 * Example usage:
 * <pre>
 *      boolean vSync = true;
        IGame gameLogic = new Game();
        Engine gameEng = new Engine("MY GAME", 600, 480, vSync, gameLogic);
        gameEng.start();
 * </pre>
 * </p>
 * <p>
 * @author Antonio Hernández Bejarano (@lwjglgamedev)
 * @author The Axiom Corp, 2017.
 * </p>
 */
package com.axiom.engine;

import com.axiom.engine.Utils.Timer;
import com.axiom.engine.input.KeyboardListener;
import com.axiom.engine.input.MouseListener;

public class Engine implements Runnable {

    public static final int TARGET_FPS = 75;
    public static final int TARGET_UPS = 30;
    private final Window window;
    private final Thread gameLoopThread;
    private final Timer timer;
    private final IGame gameLogic;
    private final MouseListener mouseInput;
    private final KeyboardListener keyboardInput;
    /*
     * Construct an Engine
     * <br>
     * Construct an engine with the given parameters.
     * <br>
     * Note that this does not initialize the engine, just
     * starts it.
     * @param windowTitle the title of the game
     * @param width the width of the window
     * @param height the height of the window
     * @param vSync should the window sync every second
     * @param gameLogic the game to run
     */
    public Engine(String windowTitle, int width, int height, boolean vSync, IGame gameLogic) throws Exception {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(windowTitle, width, height, vSync);
        this.gameLogic = gameLogic;
        timer = Utils.makeTimer();
        mouseInput = new MouseListener();
        keyboardInput = new KeyboardListener();
    }
    
    /*
     * Start the game
     * <br>
     * This game starts the thread that
     * contains the game loop.
     */
    public void start() {
        String osName = System.getProperty("os.name");
        if ( osName.contains("Mac") ) {
            gameLoopThread.run();
        } else {
            gameLoopThread.start();
        }
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            //cleanup();
        }
    }
    
    /*
     * Initialize the engine.
     * <br>
     * This method initializes all of the
     * various parts of the Engine: the
     * window, timer, input, and game.
     */
    protected void init() throws Exception {
        window.init();
        timer.init();
        mouseInput.init(window);
        keyboardInput.init(window);
        gameLogic.init(window);
    }
    
    /*
     * Run the game
     * <br>
     * This method runs the game loop,
     * taking input from the user and 
     * syncing the window to get the
     * target fps.
     */
    protected void gameLoop() {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if ( !window.isvSync() ) {
                sync();
            }
        }
    }
    
    @Deprecated
    /* Clean up the Engine *DEPRECATED*
     * <br>
     * Dismantle all of the buffers and 
     * gl functions used by the engine.
     * @deprecated do not call
     */
    protected void cleanup() {
        //gameLogic.cleanup();                
    }
    
    @Deprecated
    /*
     * Sync the window
     * <br>
     * This method holds up the thread
     * until the target fps is met.
     * @deprecated do not call
     */
    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
            }
        }
    }
    
    /*
     * Take input from the user
     * <br>
     * This method takes in input from the user 
     * and passes it to the Engine's game.
     */
    protected void input() {
        gameLogic.input(window, mouseInput, keyboardInput);
    }
    
    /*
     * Update the display
     * <br>
     * This method updates the game.
     */
    protected void update(float interval) {
        gameLogic.update(interval, mouseInput, keyboardInput);
    }

    /*
     * Render the display
     * <br>
     * This method renders the display to the window
     * and then updates the window to implement 
     * double-buffering.
     */
    protected void render() {
        gameLogic.render(window);
        window.update();
    }
}
