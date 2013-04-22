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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import jwget.ui.DownloadProgress;

/**
 *
 * @author Joao
 */
public class jWget {
    private Config jConfig;                                                              // Configuration file
    //private ConcurrentLinkedQueue<Webfile> websiteQueue = new ConcurrentLinkedQueue();   // Concurrent queue for websites (on hold to get downloaded)
    private ConcurrentLinkedQueue<Webfile> controlQueue = new ConcurrentLinkedQueue();   // Concurrent queue for websites (already downloaded, control dups)
    private static final ExecutorService executor = Executors.newCachedThreadPool();            // Thread pool
    private static final int NCORES = Runtime.getRuntime().availableProcessors();       // Number of cores the current computer has
    
    public jWget() {
    }

    /**
     * Class constructor
     * 
     * @param config
     */
    public jWget(Config config) {
        this.jConfig = config;
    }

    /**
     * 
     * GETTERS AND SETTERS - BEGIN
     * 
     */
    public Config getConfig() {
        return jConfig;
    }

    public void setConfig(Config config) {
        this.jConfig = config;
    }

//    public ConcurrentLinkedQueue<Webfile> getWebsiteQueue() {
//        return websiteQueue;
//    }
//
//    public void setWebsiteQueue(ConcurrentLinkedQueue<Webfile> websiteQueue) {
//        this.websiteQueue = websiteQueue;
//    }

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
        if (!this.jConfig.getUrl().startsWith("http://") || !this.jConfig.getUrl().startsWith("https://")) {
            this.jConfig.setUrl("http://" + this.jConfig.getUrl());
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
            out.println(this.jConfig.getDateTime() + ";" + this.jConfig.getUrl() + ";" + this.jConfig.getFolderPath() + ";" + this.jConfig.getDeepLevel());
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
        this.jConfig.setDateTime(dateFormat.format(date));
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
                this.jConfig.getDateTime()
                + "\nStarting new download for: "
                + this.jConfig.getUrl()
                + "\nExtracting files to: "
                + this.jConfig.getFolderPath()
                + "\nLevel of deepness: "
                + this.jConfig.getDeepLevel());

        // Format URL
        formatUrl();

        // Update the hisory file
        updateHistory();

        // Create the first webfile to download and add to queue
        Webfile wf = new Webfile(this.jConfig.getUrl(), this.jConfig.getDeepLevel(), Webfile.FileType.HTML);
        this.controlQueue.add(wf);
        
        // Begin the downloads
        // DownloadProgress.main(null);
        Downloader d = new Downloader(this.jConfig,executor,controlQueue,wf);
        executor.execute(d);
    }
}