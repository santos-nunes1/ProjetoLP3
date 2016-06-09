/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.beans;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import monitor.entities.Log;

@MessageDriven(name = "EventMDB", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue
            = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue
            = "java:/jms/queue/eventQueue"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Autoacknowledge")})
public class MDBBean implements MessageListener {

    private final static Logger LOGGER
            = Logger.getLogger(MDBBean.class.toString());

    @PersistenceContext(unitName = "DerbyPU")
    private EntityManager em;

    public MDBBean() {
    }

    @Override
    public void onMessage(Message message) {
        TextMessage msg = null;
        try {
            if (message instanceof TextMessage) {
                msg = (TextMessage) message;
//                System.out.println("Mensagem recebida da fila ---------- TESTE DE IMPLEMENTACAO!!!!!!!!!: " + msg.getText());
            } else {
                System.out.println("Mensagem de tipo não esperado: "
                        + message.getClass().getName());
            }
            
            String texto = msg.getText();

           
                persistirLog(msg.getText());
             

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean persistirLog(String message) {

        try {

            Timestamp tt = new Timestamp((new Date()).getTime());

            Log u = new Log();
            u.setTimestamp(tt);
            u.setEvento(message);

            em.persist(u);

            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }
    
    public List<Log> list() {
        Query query = em.createQuery("FROM Log l");
        List<Log> list = query.getResultList();
        return list;
    }
}
