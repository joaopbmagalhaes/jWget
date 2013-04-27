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
    private static Map<String, Class> fileType = new HashMap<String, Class>(); // Available types of files

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
        fileType.put("html", DownloaderParseHtml.class);
        fileType.put("css", DownloaderParseCss.class);
        fileType.put("js", DownloaderParseJs.class);
        fileType.put("pdf", DownloaderDownloadFile.class);
        fileType.put("docx", DownloaderDownloadFile.class);
        fileType.put("txt", DownloaderDownloadFile.class);
        fileType.put("jpg", DownloaderDownloadFile.class);
        fileType.put("jpeg", DownloaderDownloadFile.class);
        fileType.put("png", DownloaderDownloadFile.class);
        fileType.put("gif", DownloaderDownloadFile.class);
        fileType.put("svg", DownloaderDownloadFile.class);
        fileType.put("ico", DownloaderDownloadFile.class);
        fileType.put("mp3", DownloaderDownloadFile.class);
        /* fileType.put("js", FileType.JS);
         fileType.put("pdf", FileType.PDF);
         fileType.put("txt", FileType.TXT);
         fileType.put("docx", FileType.DOCX);
         fileType.put("jpg", FileType.JPG);
         fileType.put("jpeg", FileType.JPEG);
         fileType.put("png", FileType.PNG);
         fileType.put("gif", FileType.GIF);
         fileType.put("svg", FileType.SVG);
         fileType.put("ico", FileType.ICO);
         fileType.put("mp3", FileType.MP3);
         */
    }

    /**
     * Available types of files
     */
    public enum FileType {

        HTML, CSS, JS,
        PDF, DOCX, TXT,
        JPG, JPEG, PNG, GIF, SVG, ICO,
        MP3
    };

    public static Map<String, Class> getFileType() {
        return getInstance().fileType;
    }

    /**
     * Returns the type of the file
     *
     * @param fileName
     * @return
     */
    public static Downloader getFileType(String fileName) {
        int index = fileName.lastIndexOf(".");
        Class c;
        if (index == -1) {
            return new DownloaderParseHtml();
        } else {
            String ext = fileName.substring(index + 1, fileName.length());
            if (FileTypeMap.getFileType().containsKey(ext)) {
                try {
                    c = FileTypeMap.getFileType().get(ext);
                    return (Downloader) c.newInstance();
                } catch (InstantiationException ex) {
                    Logger.getLogger(FileTypeMap.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(FileTypeMap.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }
}
