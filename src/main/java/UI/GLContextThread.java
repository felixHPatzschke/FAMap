package UI;

import FAProps.FAMap;
import MapRenderers.HeightmapRenderer;
import MapRenderers.MapRenderer;
import OpenGL.Shader;
import Renderables.*;
import org.joml.Vector2d;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.awt.*;
import java.io.InputStream;
import java.nio.DoubleBuffer;
import java.util.ArrayList;

import static UI.Logger.logOut;
import static UI.Logger.logErr;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by Game on 01.01.2016.
 */
public class GLContextThread extends Thread {

    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private GLFWWindowSizeCallback windowSizeCallback;
    private GLFWWindowPosCallback windowPosCallback;
    private GLFWWindowFocusCallback windowFocusCallback;
    private GLFWScrollCallback scrollCallback;
    private InputStream mapIs;
    private String mapName;

    private long window;

    private Shader shader;
    private FAMap map;
    private MapRenderer mapRenderer = new HeightmapRenderer();
    //private Camera camera = new Camera();
    //private Camera2 camera = new Camera2(0, 0, 4, 0, 0, 0, 0, 1, 0, 45.0f, 300, 300);
    private Camera4 camera = new Camera4();
    private RenderableMap renderableMap = new RenderableMap();
    private ArrayList<Renderable> renderablesList = new ArrayList<>();

    private int height, width;
    private boolean resized, input = true, mouseLocked = false, focused = true;
    private Vector2d oldMouse = new Vector2d(0.0, 0.0);


    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        try {
            glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        } catch (Throwable x) {
            logErr("Failed to set GLFW error callback: ", x.getLocalizedMessage());
        }

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (glfwInit() != GLFW_TRUE)
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        width = Settings.glfw_window_width;
        height = Settings.glfw_window_height;

        camera.setViewport(width,height);

        // Create the window
        window = glfwCreateWindow(width, height, "FAHeightmap", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.

        // Center our window
        glfwSetWindowPos(
                window,
                Settings.glfw_window_posx,
                Settings.glfw_window_posy
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);
        // Make the window visible
        glfwShowWindow(window);
    }

    public static void checkError() throws Exception {
        int error = glGetError();
        if (error != GL_NO_ERROR) {
            throw new Exception(String.valueOf(error));
        }
    }

    private void loop() throws Exception {
        GL.createCapabilities();
        renderablesList.add(0, renderableMap);
        shader = new Shader("simple");
        // Set the clear color
        glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        glLineWidth(3.0f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        shader.bind();
        renderablesList.add(new CoordSystem());
        renderablesList.add(new PlainSystem());

        glfwSetWindowSizeCallback(window, windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                resized = true;
                width = w;
                height = h;
                Settings.glfw_window_width = w;
                Settings.glfw_window_height = h;

                GL11.glViewport(0, 0, width, height);
            }
        });
        glfwSetWindowPosCallback(window, windowPosCallback = new GLFWWindowPosCallback() {
            @Override
            public void invoke(long window, int xpos, int ypos) {
                Settings.glfw_window_posx = xpos;
                Settings.glfw_window_posy = ypos;
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
                //camera.setTranslationZ((float) (camera.getTranslationZ() + yoffset * speed));
                //camera.setFoV(camera.getFoV() + (float)(yoffset*speed));    // Dirty hack
                camera.zoom(yoffset * 0.05);
            }
        });
        shader.unbind();

        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                logOut("Key Action: " + key + " scancode " + scancode);
                if (key == GLFW_KEY_SPACE && action == GLFW_RELEASE) {
                    //camera.init(shader);
                    mouseLocked = !mouseLocked;
                    logOut("Mouse Locked: " + ((mouseLocked) ? ("true") : ("false")));
                    if (mouseLocked) {
                        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
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
        checkError();

        while (glfwWindowShouldClose(window) == GLFW_FALSE) {
            checkError();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            shader.bind();

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

            camera.applyMatrix(shader);

            renderablesList.stream().filter(Renderable::isRenderable).forEach(r -> {
                r.applyMatrix(shader);
                GL20.glUniform1f(shader.getTransparencyLocation(), r.getTransparency());
                r.render(null); //TODO Give Camera to renderer to reduce performance impact of large models outside of the camera
            });

            if (map != null) {
                if (map.isLoaded()) {
                    glfwSetWindowTitle(window, "FAM - " + map.getMapDetails().getName());
                }
            }

            shader.unbind();
            glfwSwapBuffers(window);
            if (input) {
                glfwPollEvents();
                input = false;
            } else {
                glfwWaitEvents();
            }
        }
    }

    private void mouseMovement() {
        if (focused) {
            DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

            glfwGetCursorPos(window, x, y);
            x.rewind();
            y.rewind();

            double dX = x.get() - oldMouse.x;
            double dY = y.get() - oldMouse.y;

            if (((int) dX) != 0 || ((int) dY) != 0) {

                //logOut("Old Mouse Position: " + oldMouse.toString());
                oldMouse.x += dX;
                oldMouse.y += dY;

                input = true;

                if (mouseLocked) {
                    logOut("Mouse Position: " + oldMouse.toString(), "Mouse Movement: " + dX + " | " + dY);
                    //if (((int)dX) != 0 || ((int)dY) != 0) {
                    //glfwSetCursorPos(window, width / 2, height / 2);
                    //camera.addRotationDeg(((float) dY) / 10, 0, ((float) dX) / 10);
                    camera.rotateCenter(dX * 0.1, dY * 0.1);
                    //}
                } else {
                    //TODO Add ray casting and tool code here
                }
            }
        }
    }

    private void keyboardInput() {
        float speed = 0.1f;

        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            speed = speed * 5;
        }
        if (glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
            speed = speed / 5;
        }

        Point transl = new Point(0, 0);

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
            //camera.setTranslationZ(camera.getTranslationZ() - (speed * 10));
            //camera.translateForward(speed);
        }
        if (glfwGetKey(window, GLFW_KEY_KP_SUBTRACT) == GLFW_PRESS) {
            //camera.setTranslationZ(camera.getTranslationZ() + (speed * 10));
            //camera.translateForward(-speed);
        }
        if (transl.x != 0 || transl.y != 0) {
            input = true;
            //transl.rotate(-camera.getEyeZ());
            //camera.addTranslation((float) transl.x, (float) transl.y, 0);
            //camera.translateRight((float)transl.x);
            //camera.translateUp((float)transl.y);
        }
    }

    public void cleanup() {
        if (shader != null) {
            shader.unbind();
            shader.destroy();
        }
        if (renderablesList != null) {
            renderablesList.forEach(renderable -> {
                if (renderable != null) {
                    renderable.remove();
                }
            });
        }
        if (keyCallback != null)
            keyCallback.release();
        if (errorCallback != null)
            errorCallback.release();
        if (windowPosCallback != null)
            windowPosCallback.release();
        if (scrollCallback != null)
            scrollCallback.release();
        if (windowFocusCallback != null)
            windowFocusCallback.release();
        if (windowSizeCallback != null)
            windowSizeCallback.release();
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    @Override
    public void run() {
        try {
            logOut("Starting");
            init();
            logOut("Init successful");
            loop();
            // Release window and window callbacks
            logOut("Terminating");
        } catch (Exception ex) {
            logErr(ex);
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            logOut("Cleaning up");
            cleanup();
            ToolboxController.exit(0);
        }
    }

    public void stopThread() {
        glfwSetWindowShouldClose(window, GLFW_TRUE);
        glfwPostEmptyEvent();
    }

    public void loadMap(InputStream is, String name) {
        mapIs = is;
        mapName = name;
    }
}