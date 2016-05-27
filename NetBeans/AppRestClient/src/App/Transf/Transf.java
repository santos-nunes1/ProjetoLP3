package App.Transf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Transf {
 public static void main(String[] args) throws TransformerConfigurationException,
TransformerException, FileNotFoundException, UnsupportedEncodingException{

 Source xmlSource = new StreamSource("C:\\Users\\Vinicius\\Documents\\GitHub\\ProjetoLP3\\NetBeans\\AppGeo\\web\\js\\dados\\posicoes.xml");
 File xslFile = new File("C:\\Users\\Vinicius\\Documents\\GitHub\\ProjetoLP3\\NetBeans\\AppGeo\\web\\js\\dados\\gpx.xsl");
 TransformerFactory transFact = TransformerFactory.newInstance();
 Transformer trans = transFact.newTransformer(new StreamSource(xslFile));
 ByteArrayOutputStream bos = new ByteArrayOutputStream();
 trans.transform(xmlSource, new StreamResult(bos));
 PrintWriter writer = new PrintWriter("C:\\Users\\Vinicius\\Documents\\GitHub\\ProjetoLP3\\NetBeans\\AppGeo\\web\\js\\dados\\posicao_proc.gpx", "UTF-8");
 writer.print(bos);
 writer.close();
 }
}
