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
        
        String web[] = {"html,css,js"};
        fileGenreType.put("web",web);
        String img[] = {"jpg,jpeg,gif,ico,svg,png"};
        fileGenreType.put("img",img);
        String audio[] = {"mp3"};
        fileGenreType.put("audio",audio);
        String video[] = {"mp4,avi"};
        fileGenreType.put("video",video);
        String other[] = {"pdf,docx,txt"};
        fileGenreType.put("other",other);
    }

    public static Map<String, Class> getFileClassType() {
        return getInstance().fileClassType;
    }

    public static Map<String, String[]> getFileGenreType() {
        return getInstance().fileGenreType;
    }
    
    /**
     * Returns the type of the file
     *
     * @param fileName
     * @return
     */
    public static Downloader getFileTypeClass(String fileName) {
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
    public static String getFileType(String fileName) {
        String ext = getFileExt(fileName);
        Map<String, String[]> fileGenre = FileTypeMap.getFileGenreType();
        for(int i = 0; i < fileGenre.size(); i++) {
            String[] extList = fileGenre.get("web");
        }
        if(FileTypeMap.getFileGenreType().containsValue(ext)) {
            
        }
        return null;
    }
}
