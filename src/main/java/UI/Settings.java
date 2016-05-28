package UI;

import org.json.JSONObject;

import java.io.*;

import static UI.Logger.*;


/**
 * Created by felix on 21.02.2016.
 */
public abstract class Settings {

    public static int glfw_window_width, glfw_window_height, glfw_window_posx, glfw_window_posy;
    public static boolean glfw_window_maximized;
    public static int tool_window_width, tool_window_height, tool_window_posx, tool_window_posy;
    public static String path;
    public static boolean logger_console_output, logger_file_output;


    private static void importCONFIG() throws FileNotFoundException, IOException{

        BufferedReader br = new BufferedReader(new FileReader("./settings.json"));

        StringBuilder sb = new StringBuilder();
        String line;

        while((line = br.readLine()) != null)
        {
            sb.append(line);
            sb.append(System.lineSeparator());
        }

        JSONObject o = new JSONObject(sb.toString());

        JSONObject glfw_window = o.getJSONObject("glfw_window");
        JSONObject tool_window = o.getJSONObject("tool_window");

        glfw_window_width = glfw_window.getInt("width");
        glfw_window_height = glfw_window.getInt("height");
        glfw_window_posx = glfw_window.getInt("x");
        glfw_window_posy = glfw_window.getInt("y");
        glfw_window_maximized = glfw_window.getBoolean("maximized");

        tool_window_width = tool_window.getInt("width");
        tool_window_height = tool_window.getInt("height");
        tool_window_posx = tool_window.getInt("x");
        tool_window_posy = tool_window.getInt("y");

        path = o.getString("path");

        logger_file_output = o.getBoolean("logger_file_output");
        logger_console_output = o.getBoolean("logger_console_output");

    }

    public static void init()
    {
        try
        {
            importCONFIG();
        } catch (IOException ioex)
        {
            createDefault();
        }
    }

    private static void createDefault()
    {
        glfw_window_width = 600;
        glfw_window_height = 600;
        glfw_window_posx = 400;
        glfw_window_posy = 200;
        glfw_window_maximized = false;

        tool_window_width = 250;
        tool_window_height = 400;
        tool_window_posx = 20;
        tool_window_posy = 20;

        path = "src\\main\\resources";

        logger_console_output = true;
        logger_file_output = true;
    }

    public static void exportCONFIG()
    {
        JSONObject res = new JSONObject();
        JSONObject glfw_window = new JSONObject();
        JSONObject tool_window = new JSONObject();

        glfw_window.put("x", glfw_window_posx);
        glfw_window.put("y", glfw_window_posy);
        glfw_window.put("width", glfw_window_width);
        glfw_window.put("height", glfw_window_height);
        glfw_window.put("maximized", glfw_window_maximized);

        tool_window.put("x", tool_window_posx);
        tool_window.put("y", tool_window_posy);
        tool_window.put("width", tool_window_width);
        tool_window.put("height", tool_window_height);

        res.put("glfw_window", glfw_window);
        res.put("tool_window", tool_window);
        res.put("logger_console_output", logger_console_output);
        res.put("logger_file_output", logger_file_output);
        res.put("path", path);

        try(FileWriter fWriter = new FileWriter("./settings.json"))
        {
            fWriter.write(res.toString(4));
            fWriter.flush();
            fWriter.close();
        }catch(IOException ioex)
        {
            logErr(ioex);
            logErr("could not write settings to file", false);
        }

    }


}
