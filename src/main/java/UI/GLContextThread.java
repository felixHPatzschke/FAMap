package UI;

import FAProps.FAMap;
import MapRenderers.HeightmapRenderer;
import MapRenderers.MapRenderer;
import OpenGL.MapShader;
import OpenGL.Shader;
import Renderables.*;
import Tools.HeightTool;
import Tools.MapChangeHandler;
import Tools.SymmetryHandler;
import Tools.Tool;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.io.InputStream;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static UI.Logger.logOut;
import static UI.Logger.logErr;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GLXEXTSwapControl.glXSwapIntervalEXT;
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
    private DebugView debugView = new DebugViewImpl();

    private Shader shader;
    private MapShader mapShader;
    private FAMap map;
    private MapRenderer mapRenderer = new HeightmapRenderer();
    private Camera camera = new Camera();
    private ArrayList<Renderable> renderablesList = new ArrayList<>();
    private RenderableMap renderableMap;

    private SymmetryHandler symmetryHandler = new SymmetryHandler();

    private int height, width;
    private boolean resized, input = true, mouseLocked = false, focused = true;
    private Vector2d oldMouse = new Vector2d(0.0, 0.0);
    private float toolPosX = 0, toolPosY = 0;


    private void init() {
        symmetryHandler.setTool(new HeightTool());
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

        camera.setViewport(width, height);

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
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        // VSYNC BROKEN ON WANGBLOWS, CALLING LEADS TO LOWER FPS!
        //glfwSwapInterval(1);
        // Make the window visible
        glfwShowWindow(window);
    }

    public static void checkError() throws Exception {
        int error = glGetError();
        if (error != GL_NO_ERROR) {
            throw new Exception(String.valueOf(error));
        }
    }

    public void init3d(){
        mapShader.bind();
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glLineWidth(3.0f);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        camera.applyMatrix(mapShader);
    }

    private void loop() throws Exception {
        GL.createCapabilities();

        renderableMap = new RenderableMap();
        RenderableWater renderableWater = new RenderableWater();

        renderablesList.add(renderableMap);
        renderablesList.add(renderableWater);
        shader = new Shader("simple");
        mapShader = new MapShader();
        // Set the clear color

        init3d();

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
                camera.zoom(yoffset * -1);
            }
        });

        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_SPACE && action == GLFW_RELEASE) {
                    //camera.init(shader);
                    mouseLocked = !mouseLocked;
                    if (mouseLocked) {
                        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
                    } else {
                        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
                    }
                }
                if (key == GLFW_KEY_F3 && action == GLFW_RELEASE) {
                    debugView.toggleDisplay();
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

        debugView.setContext(this);

        while (glfwWindowShouldClose(window) == GLFW_FALSE) {
            debugView.beforeRender();
            mouseMovement();
            keyboardInput();

            checkError();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            GL20.glUniform1f(mapShader.getToolXLocation(), toolPosX);
            GL20.glUniform1f(mapShader.getToolYLocation(), toolPosY);
            GL20.glUniform1f(mapShader.getToolRSQRLocation(), 16.0f);
            GL20.glUniform1f(mapShader.getHminLocation(), 0.0f);
            GL20.glUniform1f(mapShader.getHmaxLocation(), 1000.0f);
            GL20.glUniform1f(mapShader.getTransparencyLocation(), 0.5f);
            FloatBuffer campos = BufferUtils.createFloatBuffer(3);
            camera.getPos().get(campos);
            GL20.glUniform3fv(mapShader.getCameraPositionLocation(), campos);
            GL20.glUniform1f(mapShader.getFogDistanceLocation(), 1000.0f /*TODO: insert dynamically calculated fog distance*/);

            if (mapIs != null) {
                renderableMap.remove();
                renderableMap.setMapRenderer(mapRenderer);
                map = new FAMap();
                map.loadFAMap(mapIs, mapName);
                renderableMap.setMap(map);
                renderableWater.setWaterShader(map);
                mapIs = null;
            }

            if (resized) {
                camera.setViewport(width, height);
                resized = false;
            }

            camera.applyMatrix(mapShader);

            GL20.glUniform1f(mapShader.getHminLocation(), renderableMap.getMinHeight());
            GL20.glUniform1f(mapShader.getHmaxLocation(), renderableMap.getMaxHeight());

            renderablesList.stream().filter(Renderable::isRenderable).forEach(r -> {
                GL20.glUniform1i(mapShader.getFragEnumLocation(), r.getFragEnum());
                r.applyMatrix(mapShader);
                GL20.glUniform1f(mapShader.getTransparencyLocation(), r.getTransparency());
                r.render(camera);
            });
            
            glfwSwapBuffers(window);

            debugView.afterRender();
            debugView.render();

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
                    //if (((int)dX) != 0 || ((int)dY) != 0) {
                    //glfwSetCursorPos(window, width / 2, height / 2);
                    //camera.addRotationDeg(((float) dY) / 10, 0, ((float) dX) / 10);
                    camera.rotateCenter(dX * 0.1, dY * 0.1);
                    //}
                } else {
                    //TODO Add ray casting and tool code here
                    calculateToolPosition();
                }
            }
            if(map!=null) {
                boolean
                        left=glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS,
                        middle=glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_MIDDLE) == GLFW_PRESS,
                        right=glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_RIGHT) == GLFW_PRESS;
                if(left||middle||right) {
                    MapChangeHandler handler = symmetryHandler.applyTool(map,
                            left,
                            middle,
                            right,
                            new Vector2f(toolPosX, toolPosY));
                    handler.applyChanges(renderableMap);
                    input=true;
                }
            }
        }
    }

    private void keyboardInput() {
        float speed = 1.0f;

        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            speed *= 5;
        }
        if (glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
            speed /= 5;
        }

        Vector2f transl = new Vector2f(0, 0);

        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            transl.x -= speed;
        }
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            transl.x += speed;
        }
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            transl.y += speed;
        }
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            transl.y -= speed;
        }
        if (transl.x != 0 || transl.y != 0) {
            input = true;
            camera.moveBy(transl);
            calculateToolPosition();
        }
        if (map != null) {
            Vector3f pos = camera.getPos();
            if (-pos.x >= 0 && -pos.x <= map.getMapDetails().getHeightmap().length - 1 &&
                    -pos.y >= 0 && -pos.y <= map.getMapDetails().getHeightmap()[0].length - 1) {
                pos.z=-((float)map.getMapDetails().getHeightmap()[(int)-pos.x][(int)-pos.y]*map.getMapDetails().getHeightmapScale());
                camera.setPos(pos);
            }
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

    public void calculateToolPosition()
    {
        float x = 2*((float)oldMouse.x/(float)Settings.glfw_window_width)-1;
        float y = -2*((float)oldMouse.y/(float)Settings.glfw_window_height)+1;

        Vector4f near = camera.clipspaceToWorldspace(x, y, 0.0f);
        Vector4f far = camera.clipspaceToWorldspace(x, y, 1.0f);
        Vector4f l = new Vector4f();
        far.sub(near, l);
        float znear = near.z;
        float zl = l.z;
        Vector4f pos = new Vector4f();
        l.mul(znear/zl);
        near.sub(l, pos);
        toolPosX = pos.x;
        toolPosY = pos.y;
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