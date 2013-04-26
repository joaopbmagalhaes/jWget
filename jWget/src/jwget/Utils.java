/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Isaac
 */
public class Utils {

    public static String extractFileName(String src, String root) {
        //Exctract the name of the image from the src attribute
        URL urlTemp;
        try {
            urlTemp = new URL(root);

            String strTemp = urlTemp.getProtocol() + "://" + urlTemp.getHost();
            strTemp = src.replace(strTemp, "");
            if (strTemp.length() == 1) {
                return "index.html";
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }

        int indexname = src.lastIndexOf("/");
        if (indexname == src.length()) {
            src = src.substring(1, indexname);
        }
        indexname = src.lastIndexOf("/");
        String name = src.substring(indexname + 1, src.length());
        if (name.lastIndexOf(".") == -1) {
            name = name + ".html";
        }

        if (name.isEmpty()) {
            name = "index.html";
        }
        return name;
    }
}
