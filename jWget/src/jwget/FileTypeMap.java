/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.util.HashMap;
import java.util.Map;

/**
 * FileTypeMap - This class serves as a mapping for all types of files available
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

    /**
     * Declaration of all possible file types
     */
    private FileTypeMap() {
        fileType.put("html", FileType.HTML);
        fileType.put("css", FileType.CSS);
        fileType.put("js", FileType.JS);
        fileType.put("pdf", FileType.PDF);
        fileType.put("docx", FileType.DOCX);
        fileType.put("jpg", FileType.JPG);
        fileType.put("jpeg", FileType.JPEG);
        fileType.put("png", FileType.PNG);
        fileType.put("gif", FileType.GIF);
        fileType.put("svg", FileType.SVG);
        fileType.put("ico", FileType.ICO);
        fileType.put("mp3", FileType.MP3);
    }

    /**
     * Available types of files
     */
    public enum FileType {
        HTML, CSS, JS,
        PDF, DOCX,
        JPG, JPEG, PNG, GIF, SVG, ICO,
        MP3
    };

    public static Map<String, FileType> getFileType() {
        return getInstance().fileType;
    }
    
    /**
     * Returns the type of the file
     * 
     * @param fileName
     * @return 
     */
    public static FileType getFileType(String fileName) {
        int index = fileName.lastIndexOf(".");

        if (index == -1) {
            return FileType.HTML;
        } else {
            String ext = fileName.substring(index + 1, fileName.length());
            if (FileTypeMap.getFileType().containsKey(ext)) {
                return FileTypeMap.getFileType().get(ext);
            }
        }
        return null;
    }    
}
