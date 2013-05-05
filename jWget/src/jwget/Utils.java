/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
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
            pathAndFileName = getDirTree(baseFolderPath, cleanAbsoluteUrl, fileName);
        }

        return pathAndFileName;
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

        String strRootUrlTemp;
        String strAbsoluteUrlTemp = null;
        // Construct a URI of the rootUrl
        URL uriRootUrl = null;
        URL uriAbsoluteUrl = null;
        if (!absoluteUrl.isEmpty()) {
            try {
                uriRootUrl = new URL(rootUrl);
                uriAbsoluteUrl = new URL(absoluteUrl);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Downloader.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            if (uriRootUrl != null && uriAbsoluteUrl != null) {
                strRootUrlTemp = uriRootUrl.getHost();
                strAbsoluteUrlTemp = uriAbsoluteUrl.getHost();

                strRootUrlTemp = strRootUrlTemp.replace("www.", "");
                strAbsoluteUrlTemp = strAbsoluteUrlTemp.replace("www.", "");
                strRootUrlTemp = strRootUrlTemp.replace("www2.", "");
                strAbsoluteUrlTemp = strAbsoluteUrlTemp.replace("www2.", "");
                String diff = strRootUrlTemp.replace(strAbsoluteUrlTemp, "");
                if (!diff.isEmpty()) {

                    if (diff.equals(strRootUrlTemp)) {
                        diff = strAbsoluteUrlTemp;
                    }

                    int index = strAbsoluteUrlTemp.indexOf(".");
                    if (index != -1) {
                        strAbsoluteUrlTemp = strAbsoluteUrlTemp.substring(0, index);
                    } else {
                        strAbsoluteUrlTemp = diff;
                    }
                    strAbsoluteUrlTemp += uriAbsoluteUrl.getPath();

                } else {
                    strRootUrlTemp = "www." + strRootUrlTemp;
                    strAbsoluteUrlTemp = "www." + strAbsoluteUrlTemp;
                    absoluteUrl = absoluteUrl.replace("http://", "");
                    absoluteUrl = absoluteUrl.replace("https://", "");

                    // Extract the fullRootUrl from the absoluteUrl
                    strAbsoluteUrlTemp = absoluteUrl.replace(strRootUrlTemp, "");
                    if (strAbsoluteUrlTemp.startsWith("/")) {
                        strAbsoluteUrlTemp = strAbsoluteUrlTemp.substring(1, strAbsoluteUrlTemp.length());
                    }
                }
            }
        }
        return strAbsoluteUrlTemp;

    }

    /**
     * Extracts the name of the resource contained in cleanAboluteUrl
     *
     * @param cleanAbsoluteUrl
     * @return
     */
    private static String extractFileName(String cleanAbsoluteUrl) {
        String newFileName;
        // If the result of the replacement follows the conditions in the if it means we are at the root level, so the file is our the index.html
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
     * Parses the fullPath and creates the necessary folders to create a tree
     * struture equal to the website
     *
     * @param baseFolderPath
     * @param fullPath
     */
    public static void constructDirTree(String baseFolderPath, String fullPath) {

        String currentDir = baseFolderPath;
        String cleanPath = fullPath.replace(baseFolderPath, "");
        String[] folders = (cleanPath.substring(1, cleanPath.length())).split("\\\\");

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
    }

    /**
     * Parses the cleanAbsoluteUrl and returns the new fullPath to the resource
     * containing the tree struture equal to the website
     *
     * @param baseFolderPath
     * @param cleanAbsoluteUrl
     * @param fileName
     * @return
     */
    private static String getDirTree(String baseFolderPath, String cleanAbsoluteUrl, String fileName) {

        String currentDir = baseFolderPath;
        String[] folders = cleanAbsoluteUrl.split("/");

        if (folders.length > 0) {
            for (int i = 0; i < folders.length - 1; i++) {
                currentDir += "\\" + folders[i];
            }
        }
        return currentDir + "\\" + fileName;
    }
}
