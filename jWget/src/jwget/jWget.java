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
import java.util.concurrent.TimeUnit;
import jwget.FileTypeMap.FileType;

/**
 *
 * @author Joao
 */
public class jWget {
    private Config jConfig;         // Configuration file

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
    /**
     *
     * GETTERS AND SETTERS - END
     *
     */

    /**
     * Updates the history file
     */
    public void updateHistory() {
        try {
            // Create file
            FileWriter fstream = new FileWriter("history.csv", true);
            PrintWriter out = new PrintWriter(fstream);
            out.println(this.jConfig.getDateTime() + ";" + this.jConfig.getDomain() + ";" + this.jConfig.getFolderPath() + ";" + this.jConfig.getDeepLevel());
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
                + this.jConfig.getDomain()
                + "\nExtracting files to: "
                + this.jConfig.getFolderPath()
                + "\nLevel of deepness: "
                + this.jConfig.getDeepLevel());

        // Update the hisory file
        updateHistory();

        // Create the first webfile to download and add to queue
        Webfile wf = new Webfile(this.jConfig.getFolderPath() + "\\" + Utils.extractFileName(this.jConfig.getRoot(), this.jConfig.getRoot()),this.jConfig.getRoot(), 0, FileType.HTML);

        // Start counting downloaded links
        int t = this.jConfig.getCountLinks();
        this.jConfig.incrementCountLinks();

        // Begin the downloads
        Downloader d = new Downloader(this.jConfig, wf);
        this.jConfig.getExecutor().execute(d);

        System.out.println("initial: " + String.valueOf(this.jConfig.getCountLinks()));
        while (this.jConfig.getCountLinks() > 0) {
            //     System.out.println("loop: " + this.jConfig.getCountLinks());
        }
        this.jConfig.getExecutor().shutdown();
        try {
            this.jConfig.getExecutor().awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
        }
    }
}