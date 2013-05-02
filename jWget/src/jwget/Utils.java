/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Isaac
 */
public class Utils {

    //  Utils.extractFileName (url, this.jConfig.getRoot())
    /**
     *
     * Return the full path (contains the new fileName) to the resource
     * contained in the absoluteUrl received and creates the necessary folders
     *
     * @param baseFolderPath
     * @param rootUrl
     * @param absoluteUrl
     * @return
     */
    public static String getPathAndFileName(String baseFolderPath, String rootUrl, String absoluteUrl) {

        String pathAndFileName = null;

        String cleanAbsoluteUrl = cleanAbsoluteUrl(rootUrl, absoluteUrl);

        if (cleanAbsoluteUrl != null) {
            String fileName = extractFileName(cleanAbsoluteUrl);
            pathAndFileName = constructDirTree(baseFolderPath, cleanAbsoluteUrl, fileName);
        }

        return pathAndFileName;
    }

    public static String extractFileName(String rootUrl, String absoluteUrl) {
        String fileName = null;

        String cleanAbsoluteUrl = cleanAbsoluteUrl(rootUrl, absoluteUrl);

        if (cleanAbsoluteUrl != null) {
            fileName = extractFileName(cleanAbsoluteUrl);

        }

        return fileName;

    }

    /**
     *
     * Cleans the absoluteUrl, obtaining a String with the path to the resource
     *
     * @param rootUrl
     * @param absoluteUrl
     * @return
     */
    private static String cleanAbsoluteUrl(String rootUrl, String absoluteUrl) {
        String strUrlTemp = null;
        // Construct a URI of the rootUrl
        URL uriRootUrl = null;
        try {
            uriRootUrl = new URL(rootUrl);



        } catch (MalformedURLException ex) {
            Logger.getLogger(Downloader.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        if (uriRootUrl != null) {
            strUrlTemp = uriRootUrl.getHost();
            absoluteUrl = absoluteUrl.replace("http://", "");
            absoluteUrl = absoluteUrl.replace("https://", "");
            // Extract the fullRootUrl from the absoluteUrl
            strUrlTemp = absoluteUrl.replace(strUrlTemp, "");
            if (strUrlTemp.startsWith("/")) {
                strUrlTemp = strUrlTemp.substring(1, strUrlTemp.length());
            }
        }
        return strUrlTemp;

    }

    /**
     * Extracts the name of the resource contained in cleanAboluteUrl
     *
     * @param cleanAbsoluteUrl
     * @return
     */
    private static String extractFileName(String cleanAbsoluteUrl) {
        String newFileName = null;
        // If the result of the replacement follows the conditions in the if claus it means we are at the root level, so the file is our the index.html
        if ((cleanAbsoluteUrl.length() == 1 && cleanAbsoluteUrl.startsWith("/")) || cleanAbsoluteUrl.isEmpty()) {
            newFileName = "index.html";
        } else {
            int indexLastSlash = cleanAbsoluteUrl.lastIndexOf("/");

            if (indexLastSlash + 1 == cleanAbsoluteUrl.length()) {
                newFileName = cleanAbsoluteUrl.substring(0, indexLastSlash);
            } else {
                newFileName = cleanAbsoluteUrl.substring(indexLastSlash + 1, cleanAbsoluteUrl.length());
            }
            if (newFileName.lastIndexOf(".") == -1) {
                newFileName = newFileName + ".html";
            }

        }
        return newFileName;
    }

    /**
     *
     * Parses the cleanAbsoluteUrl and creates the necessary folders to create a
     * tree struture equal to the website
     *
     * @param baseFolderPath
     * @param cleanAbsoluteUrl
     * @param fileName
     * @return
     */
    private static String constructDirTree(String baseFolderPath, String cleanAbsoluteUrl, String fileName) {

        String currentDir = baseFolderPath;
        String[] folders = cleanAbsoluteUrl.split("/");

        if (folders.length > 0) {
            for (int i = 0; i < folders.length - 1; i++) {
                File theDir = new File(currentDir + "\\" + folders[i]);

                // if the directory does not exist, create it
                if (!theDir.exists()) {
                    if (!theDir.mkdir()) {
                        // TODO Handle error creating dir
                    }
                }
                currentDir += "\\" + folders[i];
            }
        }
        return currentDir + "\\" + fileName;
    }

    /*  public static void checkDirTree(String baseDir, String path, String fileName) {
     String pathTemp = path.replace(baseDir, "");
     if (pathTemp.startsWith("\\")) {
     pathTemp = pathTemp.substring(1, pathTemp.length());
     }
     int index = pathTemp.lastIndexOf("\\");
     if (index != -1) {
     pathTemp = pathTemp.substring(0, pathTemp.lastIndexOf("\\"));
     }

     int indexDot = path.lastIndexOf(".");
     if (index == -1) {
     index = 0;
     }
     if (indexDot == -1) {
     indexDot = path.length();
     }
     String str = path.substring(index, indexDot);

     if (!pathTemp.replace(fileName, "").isEmpty()) {
     String[] folders = pathTemp.split("/");

     for (String folder : folders) {
     File theDir = new File(baseDir + "\\" + folder);

     if (folder.replace(str, "").isEmpty()) {
     break;
     }

     // if the directory does not exist, create it
     if (!theDir.exists()) {
     if (!theDir.mkdir()) {
     // TODO Handle error creating dir
     }
     }
     baseDir += "\\" + folder;
     }
     }
     }*/
}
