/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import jwget.ui.DownloadProgress;

/**
 *
 * @author Joao
 */
public class jWget {
    private String url;                   // URL to download
    private String folderPath;            // Path to save all files
    private boolean dlImages;             // Download images
    private boolean dlVideos;             // Download videos
    private boolean dlCss;                // Download stylesheets
    private boolean dlJs;                 // Download javascript
    private int deepLevel;                // Level of deepness to crawl for websites
    private String dateTime;              // Date and time of the request
    private ConcurrentLinkedQueue<Webfile> websiteQueue = new ConcurrentLinkedQueue();   // Concurrent queue for websites (on hold to get downloaded)
    private ConcurrentLinkedQueue<Webfile> controlQueue = new ConcurrentLinkedQueue();   // Concurrent queue for websites (already downloaded, control dups)
    private static final Executor executor = Executors.newCachedThreadPool();           // Thread pool

    public jWget() {
    }

    /**
     * Class constructor
     * 
     * @param url
     * @param folderName
     * @param websitePath
     * @param dlImages
     * @param dlVideos
     * @param dlCss
     * @param dlJs
     * @param deepLevel
     * @param date
     * @param time 
     */
    public jWget(String url, String folderPath, boolean dlImages, boolean dlVideos, boolean dlCss, boolean dlJs, int deepLevel) {
        this.url = url;
        this.folderPath = folderPath;
        this.dlImages = dlImages;
        this.dlVideos = dlVideos;
        this.dlCss = dlCss;
        this.dlJs = dlJs;
        this.deepLevel = deepLevel;
    }

    /**
     * 
     * GETTERS AND SETTERS - BEGIN
     * 
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public boolean isDlImages() {
        return dlImages;
    }

    public void setDlImages(boolean dlImages) {
        this.dlImages = dlImages;
    }

    public boolean isDlVideos() {
        return dlVideos;
    }

    public void setDlVideos(boolean dlVideos) {
        this.dlVideos = dlVideos;
    }

    public boolean isDlCss() {
        return dlCss;
    }

    public void setDlCss(boolean dlCss) {
        this.dlCss = dlCss;
    }

    public boolean isDlJs() {
        return dlJs;
    }

    public void setDlJs(boolean dlJs) {
        this.dlJs = dlJs;
    }

    public int getDeepLevel() {
        return deepLevel;
    }

    public void setDeepLevel(int deepLevel) {
        this.deepLevel = deepLevel;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public ConcurrentLinkedQueue<Webfile> getWebsiteQueue() {
        return websiteQueue;
    }

    public void setWebsiteQueue(ConcurrentLinkedQueue<Webfile> websiteQueue) {
        this.websiteQueue = websiteQueue;
    }

    public ConcurrentLinkedQueue<Webfile> getControlQueue() {
        return controlQueue;
    }

    public void setControlQueue(ConcurrentLinkedQueue<Webfile> controlQueue) {
        this.controlQueue = controlQueue;
    }
    /**
     * 
     * GETTERS AND SETTERS - END
     * 
     */

    /**
     * Formats the class URL for missing protocol
     */
    public void formatUrl() {
        if (!this.url.startsWith("http://") || !this.url.startsWith("https://")) {
            this.url = "http://" + this.url;
        }
    }

    /**
     * Updates the history file
     */
    public void updateHistory() {
        try {
            // Create file
            FileWriter fstream = new FileWriter("history.csv", true);
            PrintWriter out = new PrintWriter(fstream);
            out.println(this.dateTime + ";" + this.url + ";" + this.folderPath + ";" + this.deepLevel);
            //Close the output stream
            out.close();
        } catch (Exception e) { //Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Updates to the current date and time
     */
    public void updateDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        this.dateTime = dateFormat.format(date);
    }

    public void createWebfile() {
        Webfile wf = new Webfile(this.url, this.deepLevel, Webfile.FileType.HTML);
        this.websiteQueue.add(wf);
        this.controlQueue.add(wf);
    }
    
    /**
     * Main method to coordinate the download and parse threads
     *
     * @return boolean
     * @throws URISyntaxException
     */
    public void execute() throws URISyntaxException {
        // Update date and time
        updateDate();

        // Dowlonad summary
        System.out.println(
                this.dateTime
                + "\nStarting new download for: "
                + this.url
                + "\nExtracting files to: "
                + this.folderPath
                + "\nLevel of deepness: "
                + this.deepLevel);

        // Format URL
        formatUrl();

        // Update the hisory file
        updateHistory();

        // Create the first webfile to download and add to queue
        createWebfile();
        
        // Begin the downloads
        DownloadProgress.main(null);
        Downloader d = new Downloader(this.folderPath, this.dlImages, this.dlVideos, this.dlCss, this.dlJs, this.deepLevel, this.websiteQueue, this.controlQueue);
        executor.execute(d);
    }
}