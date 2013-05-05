/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import jwget.ui.Index;

/**
 *
 * @author Joao
 */
public class jWget {

    protected Config jConfig;         // Configuration file
    private static Index index;

    public jWget() {
    }

    /**
     *
     * @param root
     * @param domain
     * @param folderPath
     * @param dlAll
     * @param txtOther
     * @param dlImages
     * @param dlAudio
     * @param dlVideos
     * @param dlCss
     * @param dlJs
     * @param dlOther
     * @param deepLevel
     * @param dateTime
     */
    public jWget(Index index, String root, String domain, String folderPath, boolean dlAll, String txtOther, boolean dlImages, boolean dlAudio, boolean dlVideos, boolean dlCss, boolean dlJs, boolean dlOther, int deepLevel, String dateTime) throws URISyntaxException {
        jWget.index = index;
        Config config = new Config(root, domain, folderPath, deepLevel, dateTime);
        FileTypeManager fileTypeManager = new FileTypeManager(dlAll, dlImages, dlAudio, dlVideos, dlCss, dlJs, dlOther, txtOther);
        FileTypeMap.setFileTypeManager(fileTypeManager);

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
            try (PrintWriter out = new PrintWriter(fstream)) {
                out.println(this.jConfig.getDateTime() + ";" + this.jConfig.getDomain() + ";" + this.jConfig.getFolderPath() + ";" + this.jConfig.getDeepLevel());
            }
        } catch (IOException e) { //Catch exception if any
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

    public static void returnResult(Object result) {
        if (result instanceof String) {

            if (result.equals("Success")) {
                index.downloadFinalyzed();
            }
        } else {
            System.out.println("Not Received Correct String.");
        }
    }

    public void cancelDownload() {

        this.jConfig.getExecutor().shutdown();

        try {
            this.jConfig.getExecutor().awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Error Cancelling the download: " + e.getMessage());
        }
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
        String fullPathAndFileName = Utils.getPathAndFileName(this.jConfig.getFolderPath(), this.jConfig.getRoot(), this.jConfig.getRoot());

        if (fullPathAndFileName != null) {

            Webfile wf = new Webfile(fullPathAndFileName, this.jConfig.getRoot(), 0);

            // Begin the downloads
            Downloader d = new DownloaderParseHtml(this.jConfig, wf);
            this.jConfig.getExecutor().execute(d);


            /*   this.jConfig.getExecutor().shutdown();
             try {
             this.jConfig.getExecutor().awaitTermination(1, TimeUnit.SECONDS);
             } catch (InterruptedException e) {
             }*/
        } else {
            // TODO Handle fileName == null
        }

    }
}
