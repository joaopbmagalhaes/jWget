/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * @author Joao
 */
public class jWget {
    private String url;                   // URL to download
    private String folderName;            // Folder name to sotre the files
    private String websitePath;           // Path to save all files
    private int deepLevel;                // Level of deepness to crawl for websites
    private String dateTime;              // Date and time of the download
    private ConcurrentLinkedQueue<Webfile> websiteQueue = new ConcurrentLinkedQueue();   // Concurrent queue for websites (on hold to get downloaded)
    private ConcurrentLinkedQueue<Webfile> controlQueue = new ConcurrentLinkedQueue();   // Concurrent queue for websites (already downloaded, control dups)
    private static final Executor executor = Executors.newCachedThreadPool();           // Thread pool

    public jWget() {
    }

    /**
     * Class constructor
     *
     * @param url
     * @param websitePath
     * @param deepLevel
     */
    public jWget(String url, String folderName, String websitePath, int deepLevel) {
        this.url = url;
        this.folderName = folderName;
        this.websitePath = websitePath;
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

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getWebsitePath() {
        return websitePath;
    }

    public void setWebsitePath(String websitePath) {
        this.websitePath = websitePath;
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
            out.println(this.dateTime + ";" + this.url + ";" + this.websitePath + ";" + this.deepLevel);
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
        setDateTime(dateFormat.format(date));
    }

    /**
     * Creates if does not exist the new folder for the website
     */
    public void createSiteFolder(String fullFolderPath) {
        File dir = new File(fullFolderPath);

        // If the directory does not exist, create it
        if (!dir.exists()) {
            System.out.println("Creating directory: " + fullFolderPath);
            boolean result = dir.mkdir();
            if (result) {
                System.out.println("DIR created!");
            }
        }
    }

    /**
     * Main method to coordinate the download and parse threads
     *
     * @return boolean
     * @throws URISyntaxException
     */
    public boolean execute() throws URISyntaxException {
        // Update date and time
        updateDate();

        // Dowlonad summary
        System.out.println(
                this.dateTime
                + "\nStarting new download for: "
                + this.url
                + "\nExtracting files to: "
                + this.websitePath
                + "\nLevel of deepness: "
                + this.deepLevel);

        // Format URL
        formatUrl();

        // Update the hisory file
        updateHistory();

        // Creates the new folder
        String fullFolderPath = this.websitePath + this.folderName;
        createSiteFolder(fullFolderPath);

        // Begin the downloads
        for (int i = 0; i < this.deepLevel; i++) {
            Downloader d = new Downloader(fullFolderPath, websiteQueue, controlQueue);
            executor.execute(d);
        }

        return true;
    }
}