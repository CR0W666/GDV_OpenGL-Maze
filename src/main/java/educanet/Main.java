package educanet;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

import java.util.Objects;

public class Main {
    private static long window;
    public static int width = 800;
    public static int height = 800;
    public int aspectRatio = width/height;


    public static void main(String[] args) throws Exception {
        run();
    }

    private static void run() {
        init();
        loop();

        GLFW.glfwDestroyWindow(window);
        // Don't forget to cleanup
        GLFW.glfwTerminate();
        Objects.requireNonNull(GLFW.glfwSetErrorCallback(null)).free();
    }

    private static void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        //region: Window init
        if ( !GLFW.glfwInit() ) throw new IllegalStateException("Unable to initialize GLFW");
        // Tell GLFW what version of OpenGL we want to use.
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        // TODO: Add support for macOS

        // Create the window...
        // We can set multiple options with glfwWindowHint ie. fullscreen, resizability etc.
        window = GLFW.glfwCreateWindow(width, height, "Maze", 0, 0);
        if (window == 0) {
            GLFW.glfwTerminate();
            throw new RuntimeException("Can't open window");
        }

        GLFW.glfwMakeContextCurrent(window);

        // Tell GLFW, that we are using OpenGL
        GL.createCapabilities();
        GL33.glViewport(0, 0, width, height);

        // Resize callback
        GLFW.glfwSetFramebufferSizeCallback(window, (win, w, h) -> {
            GL33.glViewport(0, 0, w, h);
        });
        //endregion

        GLFW.glfwSwapInterval(1); //vsync
        // Main game loop
        Game.init(window);
        GLFW.glfwShowWindow(window);
    }

    private static void loop() {
        // Draw in polygon mode
        //GL33.glPolygonMode(GL33.GL_FRONT_AND_BACK, GL33.GL_LINE);
        while (!GLFW.glfwWindowShouldClose(window)) {
            // Key input management
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS)
                GLFW.glfwSetWindowShouldClose(window, true); // Send a shutdown signal...

            // Change the background color
            GL33.glClearColor(0.3f, 0.5f, 0.9f, 1f);
            GL33.glClear(GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);

            Game.render(window);
            Game.update(window);

            // Swap the color buffer -> screen tearing solution
            GLFW.glfwSwapBuffers(window);
            // Listen to input
            GLFW.glfwPollEvents();
        }
    }



}
