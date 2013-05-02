/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Isaac
 */
public class DownloaderParseCss extends Downloader implements Runnable {

    public DownloaderParseCss() {
        super();
    }

    /**
     * Class constructor
     *
     * @param jConfig
     * @param wf
     */
    public DownloaderParseCss(Config jConfig, Webfile wf) {
        super(jConfig, wf);
    }

    /**
     * Parses a CSS file and saves it
     */
    @Override
    public void run() {
        //Open a URL Stream
        try {

            String src = wf.getUrl();

            System.out.println(this.wf.getFileName());

            //Open a URL Stream
            URL url = new URL(src);
            try (InputStream in = url.openStream()) {
                OutputStream out = new BufferedOutputStream(new FileOutputStream(this.wf.getFileName()));

                for (int b; (b = in.read()) != -1;) {
                    out.write(b);
                }
                out.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
