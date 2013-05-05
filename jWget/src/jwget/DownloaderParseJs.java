/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 *
 * @author Isaac
 */
public class DownloaderParseJs extends Downloader implements Runnable {

    public DownloaderParseJs() {
        super();
    }

    /**
     * Class constructor
     *
     * @param jConfig
     * @param wf
     */
    public DownloaderParseJs(Config jConfig, Webfile wf) {
        super(jConfig, wf);
    }

    /**
     * Parses a Javascript file and saves it
     */
    @Override
    public void run() {
        //Open a URL Stream
        try {

            //Open a URL Stream
            URL url = new URL(wf.getUrl());
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
