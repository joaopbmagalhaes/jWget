/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 *
 * @author Isaac
 */
public class DownloaderDownloadFile extends Downloader {

    public DownloaderDownloadFile() {
        super();
    }

    /**
     * Class constructor
     *
     * @param jConfig
     * @param wf
     */
    public DownloaderDownloadFile(Config jConfig, Webfile wf) {
        super(jConfig, wf);
    }

    /**
     * Downloads a specific type of file rather than CSS, JS or HTML
     */
    @Override
    public void run() {
        //Open a URL Stream
        try {
            Connection.Response resultImageResponse = Jsoup.connect(wf.getUrl()).ignoreContentType(true).execute();

            System.out.println(this.wf.getFileName());

            FileOutputStream out = (new FileOutputStream(new java.io.File(this.wf.getFileName())));
            out.write(resultImageResponse.bodyAsBytes());           // resultImageResponse.body() is where the image's contents are.

            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
