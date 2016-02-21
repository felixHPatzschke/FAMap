package UI;

import FAProps.FAMap;
import FAProps.Vector2;
import MapRenderers.HeightmapRenderer;
import MapRenderers.MapRenderer;
import OpenGL.Matrix;
import OpenGL.Shader;
import Renderables.Camera;
import Renderables.Renderable;
import Renderables.RenderableMap;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import static UI.Logger.*;

/**
 * Created by Game on 01.01.2016.
 */
public class Lwjgl extends Thread {

    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private GLFWWindowSizeCallback windowSizeCallback;
    private GLFWWindowFocusCallback windowFocusCallback;
    private GLFWScrollCallback scrollCallback;
    private InputStream mapIs;
    private String mapName;

    private long window;

    private Shader s;
    private FAMap map;
    private MapRenderer mapRenderer = new HeightmapRenderer();
    private Camera camera = new Camera();
    private RenderableMap renderableMap = new RenderableMap();
    private List<Renderable> renderablesList = new ArrayList<>();

    private int height, width;
    private boolean resized, input = true, mouseLocked = false, focused = true;

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (glfwInit() != GLFW_TRUE)
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        width = 300;
        height = 300;

        // Create the window
        window = glfwCreateWindow(width, height, "Hello World!", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.

        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                window,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);
        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {
        GL.createCapabilities();

        renderablesList.add(0, renderableMap);

        s = new Shader("simple");

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);

        s.bind();

        glfwSetWindowSizeCallback(window, windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                resized = true;
                width = w;
                height = h;
                if (width > height) {
                    GL11.glViewport(0, 0, width, width);
                } else {
                    GL11.glViewport(0, 0, height, height);
                }
            }
        });

        glfwSetScrollCallback(window, scrollCallback = new GLFWScrollCallback() {

            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                float speed = 1f;

                yoffset = yoffset * 10;

                if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
                    speed = speed * (1 / map.getMapDetails().getHeightmapScale());
                }
                if (glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
                    speed = speed / (1 / map.getMapDetails().getHeightmapScale());
                }
                camera.setTranslationZ((float) (camera.getTranslationZ() + yoffset * speed));
            }
        });

        s.unbind();

        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, GLFW_TRUE); // We will detect this in our rendering loop
                if (key == GLFW_KEY_SPACE && action == GLFW_RELEASE) {
                    //camera.init(s);
                    mouseLocked = !mouseLocked;
                    if (mouseLocked) {
                        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
                    } else {
                        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                    }
                }
            }
        });

        glfwSetWindowFocusCallback(window, windowFocusCallback = new GLFWWindowFocusCallback() {
            @Override
            public void invoke(long window, int foc) {
                focused = foc == 1;
            }
        });

        while (glfwWindowShouldClose(window) == GLFW_FALSE) {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            s.bind();

            if (mapIs != null) {
                renderableMap.remove();
                renderableMap.setMapRenderer(mapRenderer);
                map = new FAMap();
                map.loadFAMap(mapIs, mapName);
                renderableMap.setMap(map);
                mapIs = null;
            }


            if (resized) {
                camera.setViewport(width, height);
                resized = false;
            }

            mouseMovement();
            keyboardInput();

            camera.applyMatrix(s);

            for (Renderable r : renderablesList) {
                if (r.isRenderable()) {
                    r.applyMatrix(s);
                    r.render(camera);
                }
            }

            if (map != null) {
                if (map.isLoaded()) {
                    glfwSetWindowTitle(window, "FAM - " + map.getMapDetails().getName());
                }
            }
            s.unbind();
            glfwSwapBuffers(window);
            if (input) {
                glfwPollEvents();
                input = false;
            } else {
                glfwWaitEvents();
            }
        }

        s.unbind();
        s.destroy();
    }

    private int oldX = 0, oldY = 0;

    private void mouseMovement() {
        if (focused) {
            DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

            glfwGetCursorPos(window, x, y);
            x.rewind();
            y.rewind();

            int dX = width / 2 - (int) x.get();
            int dY = height / 2 - (int) y.get();

            if (dX != oldX || dY != oldY) {

                oldX = dX;
                oldY = dY;

                input = true;

                if (mouseLocked) {
                    if (dX != 0 || dY != 0) {
                        glfwSetCursorPos(window, width / 2, height / 2);
                        camera.addRotation(((float) dY) / 10, 0, ((float) dX) / 10);
                    }
                } else {
                    //TODO Add ray casting and tool code here
                }
            }
        }
    }

    private void keyboardInput() {
        float speed = 1f;

        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            speed = speed * 5;
        }
        if (glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
            speed = speed / 5;
        }

        Vector2 transl = new Vector2(0, 0);

        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            transl.y -= speed;
        }
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            transl.y += speed;
        }
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            transl.x -= speed;
        }
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            transl.x += speed;
        }
        if (glfwGetKey(window, GLFW_KEY_KP_ADD) == GLFW_PRESS) {
            camera.setTranslationZ(camera.getTranslationZ() - (speed * 10));
        }
        if (glfwGetKey(window, GLFW_KEY_KP_SUBTRACT) == GLFW_PRESS) {
            camera.setTranslationZ(camera.getTranslationZ() + (speed * 10));
        }
        if (transl.x != 0 || transl.y != 0) {
            input = true;
            //transl.rotate(-camera.getEyeZ());
            camera.addTranslation((float) transl.x, (float) transl.y, 0);
        }
    }

    public void run() {
        try {
            logOut("Starting");
            init();
            logOut("Init successful");
            loop();

            // Release window and window callbacks
            logOut("Terminating");
            glfwDestroyWindow(window);
        } finally {
            // Terminate GLFW and release the GLFWErrorCallback
            glfwTerminate();
            keyCallback.release();
            errorCallback.release();
            scrollCallback.release();
            windowFocusCallback.release();
            windowSizeCallback.release();
        }
    }

    public void loadMap(InputStream is, String name) {
        mapIs = is;
        mapName = name;
    }
}