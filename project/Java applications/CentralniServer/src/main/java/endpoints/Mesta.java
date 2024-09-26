/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package endpoints;

import resources.Korisnik;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import poruka.Poruka;
import resources.Mesto;

/**
 *
 * @author Petar
 */


@Path("mesto")
public class Mesta implements Serializable {

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
    public Response createMesto(
            @PathParam("naziv") String naziv)
    {
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(1);
        poruka.setPolje1(naziv);

        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem1Primalac, objPoruka);
        
        Message message = consumer.receive();
        
        ObjectMessage obj = (ObjectMessage)message;
        Poruka poruka_primljena;
        try {
            poruka_primljena = (Poruka)(obj.getObject());
            int IdMes = poruka_primljena.getBroj1();
            Mesto mesto = new Mesto(naziv);
            mesto.setIdMes(IdMes);
            
            String res = "IdMes|Naziv\n";
            res+=mesto.getIdMes()+"|"+mesto.getNaziv();
            
            return Response.status(Response.Status.OK).entity(res).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;  
    }
    
    
    @GET
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiMesta()
    {
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(17);

        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem1Primalac, objPoruka);
        
        Message message = consumer.receive();
        if (message instanceof ObjectMessage)
        {
              ObjectMessage obj = (ObjectMessage)message;

            try {
                poruka = (Poruka)(obj.getObject());
                int broj_korisnika = poruka.getBroj1();
                
                if (broj_korisnika==0)
                {
                    return Response.status(Response.Status.NO_CONTENT).build();
                }
                
                List<Mesto> lista = new ArrayList();
                
                
                String res="IdMes|Naziv\n";
                for (int i = 0; i < broj_korisnika; i++) {
                    message = consumer.receive();
                    ObjectMessage obj_novo = (ObjectMessage)message;
                    poruka = (Poruka)(obj_novo.getObject());
                    
                    
                    
                    Mesto mesto = new Mesto(poruka.getPolje1());
                    
                    mesto.setIdMes(poruka.getBroj1());
                    
                    res+=mesto.getIdMes()+"|";
                    res+=mesto.getNaziv()+"\n";
                    
                    lista.add(mesto);
                }
                
                return Response.status(Response.Status.OK).entity(res).build();
                
                
                
            } catch (JMSException ex) {
                Logger.getLogger(Korisnici.class.getName()).log(Level.SEVERE, null, ex);
            }
         
              
        }
        return null; 
    }
    
    
    
}
