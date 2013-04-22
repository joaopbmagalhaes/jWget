/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jwget.Webfile.FileType.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Downloader - This class extends the Thread class and is responsible for the
 * webfiles download
 *
 * @author Joao
 */
public class Downloader extends Thread {

    private Config jConfig;                                                   // Main class with all the info
    private ExecutorService executor = Executors.newCachedThreadPool(); // Thread pool
    private ConcurrentLinkedQueue<Webfile> controlQueue = new ConcurrentLinkedQueue();   // Concurrent queue for websites (already downloaded, control dups)
    private Webfile wf;
    
    public Downloader() {
    }

    public Downloader(Config jConfig, ExecutorService executor, ConcurrentLinkedQueue<Webfile> controlQueue, Webfile wf) {
        this.jConfig = jConfig;
        this.executor = executor;
        this.controlQueue = controlQueue;
        this.wf = wf;
    }

    

    /**
     *
     * GETTERS AND SETTERS - BEGIN
     *
     */
    public Config getFolderPath() {
        return jConfig;
    }

    public void setFolderPath(Config config) {
        this.jConfig = config;
    }
    /**
     *
     * GETTERS AND SETTERS - END
     *
     */
    /**
     * Parses a given URL to extract to domain
     *
     * @return String url
     * @throws URISyntaxException
     */
    public String getDomain(String url) throws URISyntaxException {
        if (url.isEmpty()) {
            return null;
        } else {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        }
    }

    @Override
    public void run() {
//        while (true) {
            // Retrieve webfile from queue
//            Webfile wf = this.websiteQueue.poll();

            // Parse the file
            switch (wf.getType()) {
                case HTML:
                        parseHtml(wf);
                        break;
                case CSS:
                    parseCSS(wf);
                    break;
                case JS:
                    parseJS(wf);
                    break;
                default:
                    break;
            }
//        }
    }

    private void parseHtml(Webfile wf) {
        // Name of the file
        String fileName;
        if(wf.getLevel()==0)
            fileName = "index.html";
        else
            fileName = "2.html";

        try {
            // Connect to server
            Document doc = Jsoup.connect(wf.getUrl()).get();

            Elements links = doc.select("a[href]"); // a with href
            for (Element element : links) {
                
            }

            // Save to file
            FileWriter fstream = new FileWriter(this.jConfig.getFolderPath() + "\\" + fileName);
            PrintWriter out = new PrintWriter(fstream);
            out.println(doc.toString());
            out.close();
            System.out.println(doc.toString());
            
            if(wf.getLevel()<2) {                
                Downloader dl = new Downloader(jConfig, executor, controlQueue, new Webfile("http://www.iana.org/protocols", wf.getLevel()+1, HTML));
                executor.execute(dl);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void parseCSS(Webfile wf) {
    }

    private void parseJS(Webfile wf) {
    }
}
