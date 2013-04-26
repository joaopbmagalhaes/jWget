/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import jwget.FileTypeMap.FileType;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Downloader - This class extends the Thread class and is responsible for the
 * webfiles downloading and parsing
 *
 * @author Joao
 */
public class Downloader extends Thread {

    private String fileName;        // Name of the file to save
    private Webfile wf;             // Webfile to parse and download
    private Config jConfig;         // Main class with all the info
    private static final String[] PARSE_TAGS = {"link", "img", "script", "a"};     // HTML tags to be parsed

    public Downloader() {
    }

    public Downloader(String fileName, Config jConfig, Webfile wf) {
        this.fileName = fileName;
        this.wf = wf;
        this.jConfig = jConfig;
    }

    /**
     *
     * GETTERS AND SETTERS - BEGIN
     *
     */
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Webfile getWf() {
        return wf;
    }

    public void setWf(Webfile wf) {
        this.wf = wf;
    }

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
    @Override
    public void run() {
        System.out.println("run: " + String.valueOf(this.jConfig.getCountLinks()));

        // Parse the file
        switch (wf.getType()) {
            case HTML:
                try {
                    parseHtml(wf);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case CSS:
                parseCSS(wf);
                break;
            case JS:
                parseJS(wf);
                break;
            case SVG:
                downloadFile(wf);
                break;
            default:
                break;
        }

    }

    /**
     * Parses a HTML page and saves it to a file
     *
     * @param wf
     * @throws URISyntaxException
     */
    private void parseHtml(Webfile wf) throws URISyntaxException {
        if (!this.jConfig.getControlQueue().contains(wf) // Control for repeated websites
                && this.jConfig.isInDomain(wf) // and websites outside the initial domain
                && this.jConfig.isInDeepLevel(wf)) {         // and also outside the deep level
            try {
                // Connect to server
                Document doc = Jsoup.connect(wf.getUrl()).get();

                // Name the file to be saved
                if (this.getFileName() == null) {
                    nameFile(".html");
                }

                // Parse all elements
                for (int i = 0; i < PARSE_TAGS.length; i++) {
                    Elements links = doc.select(PARSE_TAGS[i]);
                    String url = "";

                    String newFileName = null;
                    for (Element element : links) {

                        if (element.hasAttr("href")) {
                            url = element.absUrl("href");
                            newFileName = constructDirTree(url, Utils.extractFileName(url, this.jConfig.getRoot()));
                            element.attr("href", newFileName);
                        }
                        if (element.hasAttr("src")) {
                            url = element.absUrl("src");
                            newFileName = constructDirTree(url, Utils.extractFileName(url, this.jConfig.getRoot()));
                            element.attr("src", newFileName);
                        }

                        // TODO control the level of deepness
                        FileType fileType = getFileType(newFileName);
                        if (fileType != null) {
                            Webfile newWf = new Webfile(this.jConfig.buildURI(url).toString(), wf.getLevel() + 1, fileType);
                            if (!this.jConfig.getControlQueue().contains(newWf)) {
                                this.jConfig.incrementCountLinks();
                                Downloader dl = new Downloader(newFileName, this.jConfig, newWf);
                                this.jConfig.getExecutor().execute(dl);
                            }
                        } else {
                            // TODO Handle null fileType
                        }
                    }
                }
                checkDirTree(this.jConfig.getFolderPath(), this.fileName);
                // Save the file
                FileWriter fstream = new FileWriter(this.fileName);
                PrintWriter out = new PrintWriter(fstream);

                out.println(doc.toString());
                out.close();

                // Add webfile to the control queue
                this.jConfig.getControlQueue()
                        .add(wf);
                this.jConfig.decrementCountLinks();

                System.out.println(
                        "after run: " + String.valueOf(this.jConfig.getCountLinks()));
            } catch (MalformedURLException ex) {
                Logger.getLogger(Downloader.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Downloader.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void parseCSS(Webfile wf) {
        //Open a URL Stream
        Response resultImageResponse;
        try {
            resultImageResponse = Jsoup.connect(wf.getUrl()).ignoreContentType(true).execute();

            String src = wf.getUrl();


            System.out.println(this.fileName);


            //Open a URL Stream
            URL url = new URL(src);
            InputStream in = url.openStream();
            checkDirTree(this.jConfig.getFolderPath(), this.fileName);
            OutputStream out = new BufferedOutputStream(new FileOutputStream(this.fileName));

            for (int b; (b = in.read()) != -1;) {
                out.write(b);
            }
            out.close();
            in.close();
            /*
             // output here
             FileOutputStream out = (new FileOutputStream(new java.io.File(this.jConfig.getFolderPath() + "\\" + name)));
             out.write(resultImageResponse.bodyAsBytes());           // resultImageResponse.body() is where the image's contents are.

             out.close();

             */
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class
                    .getName()).log(Level.SEVERE, null, ex);
        }


    }

    private void parseJS(Webfile wf) {
    }

    private void downloadFile(Webfile wf) {

        //Open a URL Stream
        Response resultImageResponse;
        try {
            resultImageResponse = Jsoup.connect(wf.getUrl()).ignoreContentType(true).execute();


            System.out.println(this.fileName);


            //Open a URL Stream
          /*  URL url = new URL(src);
             InputStream in = url.openStream();

             OutputStream out = new BufferedOutputStream(new FileOutputStream(this.jConfig.getFolderPath() + name));

             for (int b; (b = in.read()) != -1;) {
             out.write(b);
             }
             out.close();
             in.close();
             */
            // output here
            checkDirTree(this.jConfig.getFolderPath(), this.fileName);
            FileOutputStream out = (new FileOutputStream(new java.io.File(this.fileName)));
            out.write(resultImageResponse.bodyAsBytes());           // resultImageResponse.body() is where the image's contents are.

            out.close();


        } catch (IOException ex) {
            Logger.getLogger(Downloader.class
                    .getName()).log(Level.SEVERE, null, ex);
        }






    }

    /**
     * Names the file to be saved
     */
    private void nameFile(String ext) {
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        setFileName(randomUUIDString + ext);
    }

    /**
     * Controls if a webfile is indeed of that deep level or not
     *
     * @param wf
     * @return boolean
     */
    private boolean controlDeepLevel(Webfile wf) {
        //TODO control the level of deepness of a webfile vs. control queue
        return true;
    }

    private String constructDirTree(String url, String fileName) {
        try {
            URL urlTemp = new URL(this.jConfig.getRoot());
            String strTemp = urlTemp.getProtocol() + "://" + urlTemp.getHost();
            strTemp = url.replace(strTemp, "");

            strTemp = strTemp.replace(fileName, "");
            strTemp = strTemp.substring(0, strTemp.length());
            if (strTemp.startsWith("/")) {
                strTemp = strTemp.substring(1, strTemp.length());
            }

            String str = fileName.substring(0, fileName.lastIndexOf("."));

            if (strTemp.replace(str, "").isEmpty()) {
                return fileName;
            }

            String[] folders = strTemp.split("/");

            String currentDir = this.jConfig.getFolderPath();

            for (String folder : folders) {
                File theDir = new File(currentDir + "\\" + folder);

                if (folder.replace(str, "").isEmpty()) {
                    break;
                }


                // if the directory does not exist, create it
                if (!theDir.exists()) {
                    if (!theDir.mkdir()) {
                        // TODO Handle error creating dir
                    }
                }
                currentDir += "\\" + folder;
            }
            return currentDir + "\\" + fileName;
        } catch (MalformedURLException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
            return fileName;
        }

    }

    private void checkDirTree(String baseDir, String path) {

        String pathTemp = path.replace(baseDir, "");
        if (pathTemp.startsWith("\\")) {
            pathTemp = pathTemp.substring(1, pathTemp.length());
        }
        int index = pathTemp.lastIndexOf("\\");
        if (index != -1) {
            pathTemp = pathTemp.substring(0, pathTemp.lastIndexOf("\\"));
        }

        int indexDot = path.lastIndexOf(".");
        if (index == -1) {
            index = 0;
        }
        if (indexDot == -1) {
            indexDot = path.length();
        }
        String str = path.substring(index, indexDot);

        if (!pathTemp.replace(fileName, "").isEmpty()) {


            String[] folders = pathTemp.split("/");

            for (String folder : folders) {
                File theDir = new File(baseDir + "\\" + folder);

                if (folder.replace(str, "").isEmpty()) {
                    break;
                }


                // if the directory does not exist, create it
                if (!theDir.exists()) {
                    if (!theDir.mkdir()) {
                        // TODO Handle error creating dir
                    }
                }
                baseDir += "\\" + folder;
            }
        }
    }

    private FileType getFileType(String fileName) {
        int index = fileName.lastIndexOf(".");

        if (index == -1) {
            return FileType.HTML;
        } else {
            String fileType = fileName.substring(index + 1, fileName.length());
            if (FileTypeMap.getFileType().containsKey(fileType)) {
                return FileTypeMap.getFileType().get(fileType);
            }
        }
        return null;
    }
}
