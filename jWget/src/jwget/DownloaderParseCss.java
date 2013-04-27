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
public class DownloaderParseCss extends Downloader {

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
            Connection.Response resultImageResponse = Jsoup.connect(wf.getUrl()).ignoreContentType(true).execute();

            String src = wf.getUrl();

            System.out.println(this.wf.getFileName());

            //Open a URL Stream
            URL url = new URL(src);
            InputStream in = url.openStream();
            // TODO Check if this call is necessary
            // Utils.checkDirTree(this.jConfig.getRoot(), this.jConfig.getFolderPath(), this.wf.getFileName());
            OutputStream out = new BufferedOutputStream(new FileOutputStream(this.wf.getFileName()));

            for (int b; (b = in.read()) != -1;) {
                out.write(b);
            }
            out.close();
            in.close();
            /*
             // output here
             FileOutputStream out = (new FileOutputStream(new java.io.File(this.jConfig.getFolderPath() + "\\" + name)));
             out.write(resultImageResponse.bodyAsBytes());           // resultImageResponse.body() is where the image's contents are.

             out.close();

             */
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
