/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
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
            URL url = new URL(wf.getUrl());
            URLConnection conn = url.openConnection();

            // conn.setConnectTimeout(CONNECT_TIMEOUT_VALUE); // set Connect Timeout
            //conn.setReadTimeout(READ_TIMEOUT_VALUE); // set Read Timeout
            String content = "";
            try (BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content += inputLine;
                }
            }

            // Parse all Css Urls
            Pattern p = Pattern.compile("url\\(\\s*(['" + '"' + "]?+)(.*?)\\1\\s*\\)");
            Matcher m = p.matcher(content);

            while (m.find()) {

                String urlFound = m.group();
                String goodUrl = url.toString();
                goodUrl = goodUrl.substring(0, goodUrl.lastIndexOf("/") + 1);

                String resourceFound = urlFound.substring(4, urlFound.length() - 1);
                resourceFound = resourceFound.replace("\"", "");

                urlFound = resourceFound;

                String resource = url.getHost() + urlFound;

                String[] splitResource = urlFound.split("/");
                if (splitResource.length != 0) {
                    for (String split : splitResource) {
                        if (!split.isEmpty()) {
                            int index = goodUrl.indexOf("/" + split);
                            if (index != -1) {
                                goodUrl = goodUrl.substring(0, index);
                                break;
                            }
                        }
                    }
                }
                urlFound = goodUrl + urlFound;

                String newPathAndFileName;
                Webfile newWf;

                // Create the new fileName and Path of the resource to be available offline
                newPathAndFileName = Utils.getPathAndFileName(this.getConfig().getFolderPath(), this.getConfig().getRoot(), urlFound);
                if (!resourceFound.contains("/")) {
                    resource = urlFound;
                } else {
                    content = content.replace(resourceFound, newPathAndFileName.replace("\\", "/"));
                }
                // Create the new Webfile to be downloaded
                newWf = new Webfile(newPathAndFileName, this.jConfig.buildURI(resource).toString(), (this.wf.getLevel() + 1));

                //Creates a new downloader object for the filetype in question
                Downloader newDownloader = FileTypeMap.getFileTypeClass(newPathAndFileName);
                newDownloader.setConfig(this.jConfig);
                newDownloader.setWebfile(newWf);
                // Get file type extension
                String fileTypeExt = FileTypeMap.getFileExt(newPathAndFileName);

                // Check if the new Resource should be downloded  or not
                if (!this.jConfig.getControlQueue().contains(newWf)) {
                    if (FileTypeMap.getFileTypeManager().canDownload(fileTypeExt)) {

                        Utils.constructDirTree(this.getConfig().getFolderPath(), newPathAndFileName);
                        this.jConfig.getExecutor().execute(newDownloader);
                        // Add webfile to the control queue
                        this.jConfig.getControlQueue().add(newWf);
                    }
                }

            }
            // Saves the resource
            saveFile(content);
        } catch (SocketTimeoutException ex) {
            Logger.getLogger(DownloaderParseHtml.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void saveFile(String content) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(new File(this.wf.getFileName()));
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
