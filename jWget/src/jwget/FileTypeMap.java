/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Isaac
 */
public class FileTypeMap {

    private static FileTypeMap instance;
    private static Map<String, FileType> fileType = new HashMap<String, FileType>(); // Available types of files

    private static FileTypeMap getInstance() {
        if (instance == null) {
            instance = new FileTypeMap();
        }
        return instance;
    }

    private FileTypeMap() {
        fileType.put("html", FileType.HTML);
        fileType.put("css", FileType.CSS);
        fileType.put("js", FileType.JS);
        fileType.put("pdf", FileType.PDF);
        fileType.put("jpg", FileType.JPG);
        fileType.put("jpeg", FileType.JPEG);
        fileType.put("png", FileType.PNG);
        fileType.put("gif", FileType.GIF);
        fileType.put("svg", FileType.SVG);
        fileType.put("ico", FileType.ICO);
    }

    public enum FileType {      // Available types of files

        HTML, CSS, JS,
        PDF, JPG, JPEG,
        PNG, GIF, SVG, ICO
    };

    public static Map<String, FileType> getFileType() {
        return getInstance().fileType;
    }
}
