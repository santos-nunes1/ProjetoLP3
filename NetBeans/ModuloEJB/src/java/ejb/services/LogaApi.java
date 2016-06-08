package ejb.services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
//import jdk.nashorn.api.scripting.JSObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author denis
 */
@Stateless
@LocalBean
public class LogaApi {

    private String apikey;
    private String privatekey;
    private String urlbase;
    private String resposta;

    public LogaApi() {
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

//    public static void main(String[] args) {
//         String apikey = "8a362aa3872e40564830a7564af63707";
//         String privatekey = "cf64ae1ac91ecb74a75f6ee47571d6d244409d6c";
//         String urlbase = "http://gateway.marvel.com/v1/public/characters";
//        LogaApi ap = new LogaApi(apikey,privatekey,urlbase);
//
//    }
    public LogaApi(String apikey, String privatekey, String Urlbase, String login) {

        this.apikey = apikey;
        this.privatekey = privatekey;
        this.urlbase = Urlbase;

        //Criação de um timestamp
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyyhhmmss");
        String ts = sdf.format(date);

        //Criação do HASH
        String hashStr = MD5(ts + privatekey + apikey);
        String uri;
        String name = "Cyclops";

        if (login.equals("mackTest@mack.br")) {
            name = "Wolverine";
        }

        //url de consulta
        uri = urlbase + "?nameStartsWith=" + name + "&ts=" + ts + "&apikey=" + apikey + "&hash=" + hashStr;
        //uri = urlbase + "nameStartsWith=" + name + "&ts=" + ts + "&apikey=" + apikey + "&hash=" + hashStr;

        try {
            HttpClient cliente = HttpClients.createDefault();
            System.out.println(uri);
            // HttpHost proxy = new HttpHost("", 3128, "http");
            // RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
            HttpGet httpget = new HttpGet(uri);
            //httpget.setConfig(config);
            HttpResponse response = cliente.execute(httpget);
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            Header[] h = response.getAllHeaders();

            for (Header head : h) {
                System.out.println(head.getValue());
            }

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                StringBuilder out = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }
                System.out.println(out.toString());

                this.resposta = out.toString();
                reader.close();
                instream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
