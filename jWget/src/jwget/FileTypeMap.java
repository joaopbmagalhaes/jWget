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
    private static Map<String, String[]> fileGenreType = new HashMap<>(); // Available types of files

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
        fileClassType.put("html", DownloaderParseHtml.class);
        fileClassType.put("css", DownloaderParseCss.class);
        fileClassType.put("js", DownloaderParseJs.class);
        fileClassType.put("pdf", DownloaderDownloadFile.class);
        fileClassType.put("docx", DownloaderDownloadFile.class);
        fileClassType.put("txt", DownloaderDownloadFile.class);
        fileClassType.put("jpg", DownloaderDownloadFile.class);
        fileClassType.put("jpeg", DownloaderDownloadFile.class);
        fileClassType.put("png", DownloaderDownloadFile.class);
        fileClassType.put("gif", DownloaderDownloadFile.class);
        fileClassType.put("svg", DownloaderDownloadFile.class);
        fileClassType.put("ico", DownloaderDownloadFile.class);
        fileClassType.put("mp3", DownloaderDownloadFile.class);
        
//        String xpto[] = {""};
//        fileGenreType.put("web",xpto);
//        fileGenreType.put("css");
//        fileGenreType.put("js");
//        fileGenreType.put("pdf");
//        fileGenreType.put("docx");
//        fileGenreType.put("txt");
//        fileGenreType.put("jpg");
//        fileGenreType.put("jpeg");
//        fileGenreType.put("png");
//        fileGenreType.put("gif");
//        fileGenreType.put("svg");
//        fileGenreType.put("ico");
//        fileGenreType.put("mp3");
    }

    public static Map<String, Class> getFileClassType() {
        return getInstance().fileClassType;
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
            if (FileTypeMap.getFileClassType().containsKey(ext)) {
                try {
                    c = FileTypeMap.getFileClassType().get(ext);
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
