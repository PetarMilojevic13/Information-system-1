package endpoints;

import resources.Korisnik;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import poruka.Poruka;
import resources.Kategorija;
import resources.Mesto;

/**
 *
 * @author 
 */
@Path("kategorija")
public class Kategorije {
    
    @Resource(lookup = "myConnFactory")
    private  ConnectionFactory myConnFactory;
    
    @Resource(lookup = "PodSistem1PrimalacQueue")
    private  javax.jms.Queue QueuePodsistem1Primalac;
    
    @Resource(lookup = "PodSistem2PrimalacQueue")
    private  javax.jms.Queue QueuePodsistem2Primalac;
    
    @Resource(lookup = "PodSistem3PrimalacQueue")
    private  javax.jms.Queue QueuePodsistem3Primalac;
    
    @Resource(lookup = "CentralniServerPrimalacQueue")
    private  javax.jms.Queue QueueRestServer;

    private  JMSContext context;
    private  JMSProducer producer;
    private  JMSConsumer consumer;


    @POST
    @Path("{naziv}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response createKategorija(
            @PathParam("naziv") String naziv)
    {
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(5);
        poruka.setPolje1(naziv);
  

        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem2Primalac, objPoruka);
        
        Message message = consumer.receive();
        
        ObjectMessage obj = (ObjectMessage)message;
        
      
        try {
            poruka = (Poruka)(obj.getObject());
            
            
            String res = "IdKat|Naziv\n";
            
            Kategorija kategorija = new Kategorija(naziv);
            kategorija.setIdKat(poruka.getBroj1());
            
            res+=kategorija.getIdKat()+"|"+kategorija.getNaziv()+"\n";
            
           return Response.status(Response.Status.OK).entity(res).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Korisnici.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    
    
    
    
    
    
    
    
    @GET
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response getKategorije()
    {
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(19);

        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem2Primalac, objPoruka);
        
        Message message = consumer.receive();
        if (message instanceof ObjectMessage)
        {
              ObjectMessage obj = (ObjectMessage)message;

            try {
                poruka = (Poruka)(obj.getObject());
                int broj_korisnika = poruka.getBroj1();
                
                if (broj_korisnika==0)
                {
                    return Response.status(Response.Status.OK).entity("Nijedna kategorija ne postoji").build();
                }
                
                List<Kategorija> lista = new ArrayList();
                
                String res="IdKat|Naziv\n";
                
                for (int i = 0; i < broj_korisnika; i++) {
                    message = consumer.receive();
                    ObjectMessage obj_novo = (ObjectMessage)message;
                    poruka = (Poruka)(obj_novo.getObject());
                    
                    
                    
                    Kategorija kategorija = new Kategorija(poruka.getPolje1());
                    
                    kategorija.setIdKat(poruka.getBroj1());
                    
                    res+=kategorija.getIdKat()+"|"+kategorija.getNaziv()+"\n";
                    
                    
                    lista.add(kategorija);
                }
                
                return Response.status(Response.Status.OK).entity(res).build();
                
                
                
            } catch (JMSException ex) {
                Logger.getLogger(Korisnici.class.getName()).log(Level.SEVERE, null, ex);
            }
         
              
        }
        return null; 
    }
    
    
    
    
    
}
    
    
    
    
