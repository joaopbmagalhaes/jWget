/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
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
            // Connect to server
            Document doc = Jsoup.connect(wf.getUrl()).get();

            // Parse all elements
            for (int i = 0; i < PARSE_TAGS.length; i++) {
                Elements links = doc.select(PARSE_TAGS[i]);
                String absoluteUrl = "";
                String newPathAndFileName = null;
                Webfile newWf = null;

                // For each occurrence of the element, parses it
                // and changes the reference if necessary
                for (Element element : links) {
                    // For elements with href
                    if (element.hasAttr("href")) {
                        absoluteUrl = element.absUrl("href");
                        newPathAndFileName = Utils.getPathAndFileName(this.getConfig().getFolderPath(), this.getConfig().getRoot(), absoluteUrl);
                        newWf = new Webfile(newPathAndFileName, this.jConfig.buildURI(absoluteUrl).toString(), (this.wf.getLevel() + 1));
                        if (this.jConfig.getDeepLevel() == this.wf.getLevel()
                                && PARSE_TAGS[i].equals("a")
                                && !this.jConfig.getControlQueue().contains(newWf)) {
                            element.attr("href", absoluteUrl);
                        } else {
                            element.attr("href", newPathAndFileName);
                        }
                    }
                    // For elements with src
                    if (element.hasAttr("src")) {
                        absoluteUrl = element.absUrl("src");
                        newPathAndFileName = Utils.getPathAndFileName(this.getConfig().getFolderPath(), this.getConfig().getRoot(), absoluteUrl);
                        newWf = new Webfile(newPathAndFileName, this.jConfig.buildURI(absoluteUrl).toString(), (this.wf.getLevel() + 1));
                        if (this.jConfig.getDeepLevel() == this.wf.getLevel()
                                && PARSE_TAGS[i].equals("a")
                                && !this.jConfig.getControlQueue().contains(newWf)) {
                            element.attr("src", absoluteUrl);
                        } else {
                            element.attr("src", newPathAndFileName);
                        }
                    }

                    System.out.println("Next URL: " + absoluteUrl);
                    System.out.println("Next file name: " + newPathAndFileName);

                    //Creates a new downloader object for the filetype in question
                    Downloader newDownloader = FileTypeMap.getFileTypeClass(newPathAndFileName);
                    // Get file type extension
                    String fileTypeExt = FileTypeMap.getFileExt(newPathAndFileName);
                    if (newDownloader != null) {
                        // Creates a new Webfile to be downloaded
                        FileTypeMap.getFileTypeManager().canDownload(fileTypeExt);
                        if (!this.jConfig.getControlQueue().contains(newWf) // Control for repeated websites
                                && this.jConfig.isInDomain(newWf) // and websites outside the initial domain
                                && this.jConfig.isInDeepLevel(newWf) // and also outside the deep level
                                //      && FileTypeMap.getFileTypeManager().canDownload(fileTypeExt)) { // and if the download is wanted
                                ) {
                           
                            newDownloader.setConfig(this.jConfig);
                            newDownloader.setWebfile(newWf);
                            this.jConfig.incrementCountLinks();
                            this.jConfig.getExecutor().execute(newDownloader);
                        }
                    }
                }
            }
            // TODO Check if this call is necessary
            //  Utils.checkDirTree(this.jConfig.getRoot(), this.jConfig.getFolderPath(), this.wf.getFileName());
            // Save the file
            FileWriter fstream = new FileWriter(this.wf.getFileName());
            try (PrintWriter out = new PrintWriter(fstream)) {
                out.println(doc.toString());
            }

            // Add webfile to the control queue
            this.jConfig.getControlQueue().add(wf);
            this.jConfig.decrementCountLinks();

            System.out.println("after run: " + String.valueOf(this.jConfig.getCountLinks()));
        } catch (URISyntaxException ex) {
            Logger.getLogger(DownloaderParseHtml.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
