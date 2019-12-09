/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.intx.netbeans.module.extools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;
import pl.intx.netbeans.module.extools.models.ExternalTool;

/**
 *
 * @author mariuszclapinski
 */
public class ExternalTools {

    public static final String PREFERENCES_KEY_TOOL_LIST = "ExternalToolsList";

    private final Logger logger = Logger.getLogger(ExternalTools.class.getName());

    private ArrayList<ExternalTool> tools;

    public void loadTools() {
        String listModelSerialized = NbPreferences.forModule(ExternalTools.class).get(PREFERENCES_KEY_TOOL_LIST, "");
        if (listModelSerialized.length() == 0) {
            tools = new ArrayList<>();
            return;
        }

        tools = (ArrayList<ExternalTool>) Utils.Serializer.fromString(listModelSerialized);
    }

    public void storeTools() {
        String listModelSerialized = Utils.Serializer.toString(tools);
        NbPreferences.forModule(ExternalTools.class).put(PREFERENCES_KEY_TOOL_LIST, listModelSerialized);
    }

    public ArrayList<ExternalTool> getTools() {
        return tools;
    }

    public void setTools(ArrayList<ExternalTool> tools) {
        this.tools = tools;
    }

    public void execute(ExternalTool tool, FileObject fo) {
        File actualFile = FileUtil.toFile(fo);
        String toolPath = tool.getPath();

        

        try {
            String args = tool.getArgs().replace("${file}", actualFile.getCanonicalPath())
                    .replace("${fileName}", fo.getNameExt())
                    .replace("${fileBasename}", fo.getName())
                    .replace("${fileExt}", fo.getExt());
            logger.log(Level.INFO, "Calling command: {0}", toolPath + " " + args);
            
            ProcessBuilder processBuilder = new ProcessBuilder(toolPath, args);
            processBuilder.inheritIO();
            processBuilder.start();

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
