/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Downloader - This class extends the Thread class nad is responsible for the webpage download
 * @author Joao
 */
public class Downloader extends Thread {
    private String url;                  // URL to download
    private String filePath;             // Path to save the download file
    private String fileName;             // Name of the downloaded file

    public Downloader() {
    }

    /**
     * Class constructor
     * 
     * @param url
     * @param filePath
     * @param fileName 
     */
    public Downloader(String url, String filePath, String fileName) {
        this.url = url;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    @Override
    public void run() {
        URL website;
        try {
            website = new URL(this.url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(filePath + fileName);
            fos.getChannel().transferFrom(rbc, 0, 1 << 24);            
        } catch (MalformedURLException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
