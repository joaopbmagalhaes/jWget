/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jwget.Downloader.PARSE_TAGS;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Isaac
 */
public class DownloaderParseHtml extends Downloader implements Runnable {

    public DownloaderParseHtml() {
        super();
    }

    /**
     * Class constructor
     *
     * @param jConfig
     * @param wf
     */
    public DownloaderParseHtml(Config jConfig, Webfile wf) {
        super(jConfig, wf);
    }

    /**
     * Parses a HTML page and saves it to a file
     */
    @Override
    public void run() {
        try {

            // Connect to server - set Timeout         
            Document doc = Jsoup.connect(wf.getUrl()).timeout(CONNECT_TIMEOUT_VALUE).get();
            //Document doc = Jsoup.connect(wf.getUrl()).get();

            // Parse all elements
            for (int i = 0; i < PARSE_TAGS.length; i++) {
                Elements links = doc.select(PARSE_TAGS[i]);

                // For each occurrence of the element, parses it
                // and changes the reference if necessary
                for (Element element : links) {

                    // For elements with href
                    if (element.hasAttr("href")) {
                        handleElement(element, "href");
                    }

                    // For elements with src
                    if (element.hasAttr("src")) {
                        handleElement(element, "src");
                    }

                }
            }
            // Save the file
            Utils.constructDirTree(this.getConfig().getFolderPath(), this.wf.getFileName());
            FileWriter fstream = new FileWriter(this.wf.getFileName());

            PrintWriter out = null;
            try {
                out = new PrintWriter(fstream);
                out.println(doc.toString());
            } finally {
                if (out != null) {
                    out.close();
                }
            }
            // Add webfile to the control queue
            this.jConfig.getControlQueue().add(wf);
        } catch (SocketTimeoutException | URISyntaxException ex) {
            Logger.getLogger(DownloaderParseHtml.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(DownloaderParseHtml.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | NullPointerException ex) {
            Logger.getLogger(DownloaderParseHtml.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleElement(Element element, String elementType) throws URISyntaxException {
        String absoluteUrl;
        String newPathAndFileName;
        Webfile newWf;
        boolean isToDownload = false;
        absoluteUrl = element.absUrl(elementType);
        newPathAndFileName = Utils.getPathAndFileName(this.getConfig().getFolderPath(), this.getConfig().getRoot(), absoluteUrl);
        //Creates a new downloader object for the filetype in question
        if (newPathAndFileName != null) {
            newWf = new Webfile(newPathAndFileName, this.jConfig.buildURI(absoluteUrl).toString(), (this.wf.getLevel() + 1));
            Downloader newDownloader = FileTypeMap.getFileTypeClass(newPathAndFileName);
            if (newDownloader instanceof DownloaderParseHtml) {
                if (this.jConfig.isInDomain(newWf)) {
                    element.attr(elementType, newPathAndFileName);
                    isToDownload = true;
                }
            } else {
                element.attr(elementType, newPathAndFileName);
                isToDownload = true;
            }
            if (isToDownload) {
                // Create New Downloader to Handle the new Resource
                prepareNewDownloader(newDownloader, newPathAndFileName, newWf, absoluteUrl);
            }
        }
    }

    private void prepareNewDownloader(Downloader newDownloader, String newPathAndFileName, Webfile newWf, String absoluteUrl) throws URISyntaxException {

        // Get file type extension
        String fileTypeExt = FileTypeMap.getFileExt(newPathAndFileName);

        // Creates a new Webfile to be downloaded
        if (!this.jConfig.getControlQueue().contains(newWf)) {
            // Check diferente properties if is a HTML or a resource
            if (newDownloader instanceof DownloaderParseHtml) {
                // Control websites outside the initial domain
                // and also outside the deep level
                if (this.jConfig.isInDomain(newWf)
                        && this.jConfig.isInDeepLevel(newWf)) {
                    System.out.println("Next URL: " + absoluteUrl);
                    System.out.println("Next file name: " + newPathAndFileName);

                    newDownloader.setConfig(this.jConfig);
                    newDownloader.setWebfile(newWf);
                    Utils.constructDirTree(this.getConfig().getFolderPath(), newPathAndFileName);
                    this.jConfig.getExecutor().execute(newDownloader);
                }
            } else {
                // Control if the download is wanted
                if (FileTypeMap.getFileTypeManager().canDownload(fileTypeExt)) {
                    System.out.println("Next URL: " + absoluteUrl);
                    System.out.println("Next file name: " + newPathAndFileName);

                    newDownloader.setConfig(this.jConfig);
                    newDownloader.setWebfile(newWf);
                    Utils.constructDirTree(this.getConfig().getFolderPath(), newPathAndFileName);
                    this.jConfig.getExecutor().execute(newDownloader);
                }
            }
        }
    }

}
