/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FileTypeMap - This class serves as a mapping for all types of files available
 *
 * @author Isaac
 */
public class FileTypeMap {

    private static FileTypeMap instance;
    private static Map<String, Class> fileClassType = new HashMap<>(); // Available types of files
    private static Map<String, String> fileGenreType = new HashMap<>(); // Available types of files
    private static FileTypeManager fileTypeManager;

    public static FileTypeMap getInstance() {
        if (instance == null) {
            instance = new FileTypeMap();
        }
        return instance;
    }

    /**
     * Declaration of all possible file types
     */
    private FileTypeMap() {
        fileClassType.put("html", DownloaderParseHtml.class);
        fileClassType.put("css", DownloaderParseCss.class);
        fileClassType.put("js", DownloaderParseJs.class);

        // Init web file types
        fileGenreType.put("html", "web");
        fileGenreType.put("css", "web");
        fileGenreType.put("js", "web");
        // Init image file types
        fileGenreType.put("jpg", "img");
        fileGenreType.put("jpeg", "img");
        fileGenreType.put("svg", "img");
        fileGenreType.put("png", "img");
        fileGenreType.put("gif", "img");
        fileGenreType.put("ico", "img");
        fileGenreType.put("raw", "img");
        fileGenreType.put("tiff", "img");
        fileGenreType.put("bmp", "img");
        // Init audio file types
        fileGenreType.put("mp3", "audio");
        fileGenreType.put("wav", "audio");
        fileGenreType.put("au", "audio");
        fileGenreType.put("flac", "audio");
        // Init video file types
        fileGenreType.put("mp4", "video");
        fileGenreType.put("mpeg", "video");
        fileGenreType.put("mpg", "video");
        fileGenreType.put("m1s", "video");
        fileGenreType.put("mpa", "video");
        fileGenreType.put("avi", "video");
        fileGenreType.put("mov", "video");
        fileGenreType.put("qt", "video");
        fileGenreType.put("asf", "video");
        fileGenreType.put("asx", "video");
        fileGenreType.put("wmv", "video");
        fileGenreType.put("wma", "video");
        fileGenreType.put("wmx", "video");
        fileGenreType.put("ogm", "video");
        fileGenreType.put("mkv", "video");
        // Init other file types
        fileGenreType.put("docx", "other");
        fileGenreType.put("pdf", "other");
        fileGenreType.put("txt", "other");
        fileGenreType.put("ttf", "other");
        fileGenreType.put("otf", "other");
    }

    public static Map<String, Class> getFileClassType() {
        return getInstance().fileClassType;
    }

    public static Map<String, String> getFileGenreType() {
        return getInstance().fileGenreType;
    }

    public static FileTypeManager getFileTypeManager() {
        return fileTypeManager;
    }

    public static void setFileTypeManager(FileTypeManager fileTypeManager) {
        FileTypeMap.getInstance().fileTypeManager = fileTypeManager;
    }

    /**
     * Returns the type of the file
     *
     * @param fileName
     * @return
     */
    public static Downloader getFileTypeClass(String fileName) {
        if (!fileName.isEmpty()) {
            int index = fileName.lastIndexOf(".");
            Class c;
            if (index == -1) {
                return new DownloaderParseHtml();
            } else {
                String ext = fileName.substring(index + 1, fileName.length());
                if (FileTypeMap.getFileClassType().containsKey(ext)) {
                    try {
                        c = FileTypeMap.getFileClassType().get(ext);
                        return (Downloader) c.newInstance();
                    } catch (InstantiationException | IllegalAccessException ex) {
                        Logger.getLogger(FileTypeMap.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    return (Downloader) new DownloaderDownloadFile();
                }
            }
        }
        return null;
    }

    /**
     * Returns the extension of the file
     *
     * @param fileName
     * @return
     */
    public static String getFileExt(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return "html";
        } else {
            return fileName.substring(index + 1, fileName.length());
        }
    }

    /**
     * Returns the type of the file
     *
     * @param fileName
     * @return
     */
    public String getFileType(String ext) {
        return getFileGenreType().get(ext);
    }
}
