/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Joao
 */
public class Parser extends Thread{
    private String url;         // URL to download

    public Parser() {
    }

    /**
     * Class constructor
     * 
     * @param url 
     */
    public Parser(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    @Override
    public void run(){
        try {
            Document doc = Jsoup.connect(this.url).get();
        } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
