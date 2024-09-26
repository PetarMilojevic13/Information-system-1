/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package podsistem1;


import entiteti.Korisnik;
import entiteti.Mesto;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import poruka.Poruka;
import java.util.List;
/**
 *
 * @author Petar
 */
public class PodSistem1 {

    @Resource(lookup = "myConnFactory")
    private static ConnectionFactory myConnFactory;
    
    @Resource(lookup = "PodSistem1PrimalacQueue")
    private static javax.jms.Queue QueuePodsistem1Primalac;
    
    @Resource(lookup = "PodSistem2PrimalacQueue")
    private static javax.jms.Queue QueuePodsistem2Primalac;
    
    @Resource(lookup = "PodSistem3PrimalacQueue")
    private static javax.jms.Queue QueuePodsistem3Primalac;
    
    @Resource(lookup = "CentralniServerPrimalacQueue")
    private static javax.jms.Queue QueueRestServer;
    
    private static EntityManagerFactory emf;
    private static EntityManager em;
 
    private static JMSContext context;
    private static JMSProducer producer;
    private static JMSConsumer consumer;
    
    static int primljeno_zahtev3=0;
    static int primljeno_zahtev2=0;
    static int primljeno_zahtev4=0;
    
 public static void main(String[] args) {
        
        emf= Persistence.createEntityManagerFactory("PodSistem1PU");
        em=emf.createEntityManager();
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueuePodsistem1Primalac);
      
        System.out.println("podsistem1.PodSistem1.main()");
        
           primljeno_zahtev2=0;
           primljeno_zahtev3=0;
           primljeno_zahtev4=0;
           

           
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                
                if (message instanceof ObjectMessage)
                {
                    System.out.println("USLO");
                    ObjectMessage obj = (ObjectMessage)message;
                    Poruka poruka=null;
                    try {
                        poruka = (Poruka)(obj.getObject());
                    } catch (JMSException ex) {
                        Logger.getLogger(PodSistem1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    int zahtev = poruka.getBroj_zahteva();
                    switch (zahtev)
                    {
                        case 1:
                        {
                            String naziv = poruka.getPolje1();
                            int idMes = KreirajGrad(naziv);
                            
                            //Obavesti server
                            
                            
                                                       
                                Poruka novaPoruka = new Poruka(1);
                                novaPoruka.setBroj1(idMes);
                                novaPoruka.setPolje1(naziv);
                                
                                
                                
                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                producer.send(QueueRestServer, objPorukaServer);
                            
                        }
                            break;
                        case 2:
                        {
                            String ime = poruka.getPolje1();
                            String email = poruka.getPolje2();
                            String pol = poruka.getPolje3();
                            int godiste = poruka.getBroj1();
                            int idMes = poruka.getBroj2();
                            Mesto mesto = em.find(Mesto.class, idMes);
                            
                            if (mesto==null)
                            {
                                Poruka slanje = new Poruka(-1);
                                slanje.setBroj1(-1);
                                ObjectMessage objPorukaServer = context.createObjectMessage(slanje);
                                producer.send(QueueRestServer, objPorukaServer);
                            }
                            
                            
                            
                            else
                            {
                                KreirajKorisnika(ime,email,godiste,pol,mesto);
                                obavestiPodsistem2_zahtev2(ime, email, godiste, pol, idMes);
                                obavestiPodsistem3_zahtev2(ime, email, godiste, pol, idMes);
                            }
                            

                        }
                            break;
                        case 3:
                        {

                            String email = poruka.getPolje1();
                            int IdKor = poruka.getBroj1();
                            Korisnik k = em.find(Korisnik.class,IdKor);
                            
                            
                            if (k==null)
                            {
                                Poruka slanje = new Poruka(-1);
                                slanje.setBroj1(-1);
                                ObjectMessage objPorukaServer = context.createObjectMessage(slanje);
                                producer.send(QueueRestServer, objPorukaServer);
                            }
                            
                            
                            else
                            {
                                em.getTransaction().begin();
                                k.setEmail(email);
                                em.getTransaction().commit();
                                obavestiPodsistem2_zahtev3(email,IdKor);
                                obavestiPodsistem3_zahtev3(email,IdKor);
                            }

                            

                        }
                            break;
                            
                          case 102:
                        {
                            
                            primljeno_zahtev2++;
                            if (primljeno_zahtev2==2)
                            {
                                primljeno_zahtev2=0;
                                
                                Poruka novaPoruka = new Poruka(2);
                                
                                novaPoruka.setBroj1(poruka.getBroj1());
                                novaPoruka.setBroj2(poruka.getBroj2());
                                novaPoruka.setBroj3(poruka.getBroj3());
                                novaPoruka.setPolje1(poruka.getPolje1());
                                novaPoruka.setPolje2(poruka.getPolje2());
                                novaPoruka.setPolje3(poruka.getPolje3());
                                
                                System.out.println("SERVERU POSLATO");
                                 
                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                producer.send(QueueRestServer, objPorukaServer);
                                
                            }
                        }
                            break;
                            
                        case 103:
                        {
                            System.out.println("ODGOVOR STIGAO");
                            primljeno_zahtev3++;
                            if (primljeno_zahtev3==2)
                            {
                             primljeno_zahtev3=0;
                                // Slanje ka serveru
                                Korisnik k = em.find(Korisnik.class,poruka.getBroj1());
                                
                                Poruka novaPoruka = new Poruka(3);
                                
                                novaPoruka.setBroj1(k.getIdKor());
                                novaPoruka.setBroj2(k.getGodiste());
                                novaPoruka.setBroj3(k.getMesto().getIdMes());
                                novaPoruka.setPolje1(k.getIme());
                                novaPoruka.setPolje2(k.getEmail());
                                novaPoruka.setPolje3(k.getPol());
                                
                                System.out.println("SERVERU POSLATO");
                                 
                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                producer.send(QueueRestServer, objPorukaServer);
                            }
                           

                        }
                            break;
                        
                        case 4:
                        {
                            int IdKor = poruka.getBroj1();
                            int IdMes = poruka.getBroj2();
                            Mesto mesto  = em.find(Mesto.class,IdMes);
                            Korisnik k = em.find(Korisnik.class,IdKor);
                            
                            if (k==null || mesto==null)
                            {
                                Poruka slanje = new Poruka(-1);
                                slanje.setBroj1(-1);
                                ObjectMessage objPorukaServer = context.createObjectMessage(slanje);
                                producer.send(QueueRestServer, objPorukaServer);
                            }
                            
                            
                            else
                            {
                                                              
                                em.getTransaction().begin();
                                k.setMesto(mesto);
                                em.getTransaction().commit();
                                obavestiPodsistem2_zahtev4(IdKor, IdMes);
                                obavestiPodsistem3_zahtev4(IdKor, IdMes);
                            }

                        }
                            break;
                            
                            
                            
                            
                        case 104:
                        {
                            System.out.println("ODGOVOR STIGAO");
                            primljeno_zahtev4++;
                            if (primljeno_zahtev4==2)
                            {
                                primljeno_zahtev4=0;
                                // Slanje ka serveru
                                
                                Korisnik k = em.find(Korisnik.class,poruka.getBroj1());
                                
                                Poruka novaPoruka = new Poruka(3);
                                
                                novaPoruka.setBroj1(k.getIdKor());
                                novaPoruka.setBroj2(k.getGodiste());
                                novaPoruka.setBroj3(k.getMesto().getIdMes());
                                novaPoruka.setPolje1(k.getIme());
                                novaPoruka.setPolje2(k.getEmail());
                                novaPoruka.setPolje3(k.getPol());
                                
                                System.out.println("SERVERU POSLATO");
                                 
                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                producer.send(QueueRestServer, objPorukaServer);
                            }
                           

                        }
                            break;
                            
                        case 17:
                        {
                            TypedQuery<Mesto> query = em.createQuery("SELECT m FROM Mesto m", Mesto.class);
                            List<Mesto> results = query.getResultList();
                            Poruka nova = new Poruka(17);
                            nova.setBroj1(results.size());
                            ObjectMessage objPoruka = context.createObjectMessage(nova);
                            producer.send(QueueRestServer, objPoruka);
                          
                            //SLANJE SERVERU
                            
                            for (Mesto mesto:results)
                            {
                           
                                Poruka novaPoruka = new Poruka(17);
                                novaPoruka.setBroj1(mesto.getIdMes());
                                novaPoruka.setPolje1(mesto.getNaziv());
                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                
                                
                                producer.send(QueueRestServer, objPorukaServer);
                                //SLANJE SERVERU
                            }
                        }
                            break;
                        case 18:
                        {
                            TypedQuery<Korisnik> query = em.createQuery("SELECT k FROM Korisnik k", Korisnik.class);
                            List<Korisnik> results = query.getResultList();
                            Poruka nova = new Poruka(18);
                            nova.setBroj1(results.size());
                            ObjectMessage objPoruka = context.createObjectMessage(nova);
                            producer.send(QueueRestServer, objPoruka);
                            System.out.println(results.size());
                            //SLANJE SERVERU
                            
                            for (Korisnik korisnik:results)
                            {
                           
                                Poruka novaPoruka = new Poruka(18);
                                novaPoruka.setPolje1(korisnik.getIme());
                                novaPoruka.setPolje2(korisnik.getEmail());
                                novaPoruka.setPolje3(korisnik.getPol());
                                novaPoruka.setBroj1(korisnik.getIdKor());
                                novaPoruka.setBroj2(korisnik.getGodiste());
                                novaPoruka.setBroj3(korisnik.getMesto().getIdMes());
                            
                                
                                System.out.println(korisnik.getIdKor());
                                
                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                producer.send(QueueRestServer, objPorukaServer);
                                //SLANJE SERVERU
                            }
                        }
                            break;
                            
                    }
                    
                }
                
         
            }
        });
        
       while(true)
       {
           
       }
    }
        
      
    
    
    public static int KreirajGrad(String naziv)
    {
        Mesto m = new Mesto(naziv);

        em.getTransaction().begin();
        em.persist(m);
        em.getTransaction().commit();
        System.out.println("OK poslato\n");
        return m.getIdMes();
    }
    
    public static void KreirajKorisnika(String ime,String email,int godiste,String pol,Mesto mesto)
    {
        Korisnik k = new Korisnik(ime,email,godiste,pol,mesto);

        em.getTransaction().begin();
        em.persist(k);
        em.getTransaction().commit();
        System.out.println("OK poslato\n");
    }
    
     public static void obavestiPodsistem2_zahtev2(String ime,String email,int godiste,String pol,int mesto)
     {
        Poruka poruka = new Poruka(2);
        
        poruka.setPolje1(ime);
        poruka.setPolje2(email);
        poruka.setPolje3(pol);
        poruka.setBroj1(godiste);
        poruka.setBroj2(mesto);
       
        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem2Primalac, poruka);
     }
     
    public static void obavestiPodsistem3_zahtev2(String ime,String email,int godiste,String pol,int mesto)
     {
        Poruka poruka = new Poruka(2);
        
        poruka.setPolje1(ime);
        poruka.setPolje2(email);
        poruka.setPolje3(pol);
        poruka.setBroj1(godiste);
        poruka.setBroj2(mesto);
       
        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem3Primalac, poruka);
     }
    
         public static void obavestiPodsistem2_zahtev3(String email,int IdKor)
     {
        Poruka poruka = new Poruka(3);
        
        poruka.setPolje1(email);

        poruka.setBroj1(IdKor);

       
        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem2Primalac, objPoruka);
     }
     
    public static void obavestiPodsistem3_zahtev3(String email,int IdKor)
     {
        Poruka poruka = new Poruka(3);
        
        poruka.setPolje1(email);

        poruka.setBroj1(IdKor);

       
        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem3Primalac, objPoruka);
     }
    
        public static void obavestiPodsistem2_zahtev4(int IdKor,int IdMes)
     {
        Poruka poruka = new Poruka(4);
        
        poruka.setBroj1(IdKor);

        poruka.setBroj2(IdMes);

       
        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem2Primalac, objPoruka);
     }
        
            public static void obavestiPodsistem3_zahtev4(int IdKor,int IdMes)
     {
        Poruka poruka = new Poruka(4);
        
        poruka.setBroj1(IdKor);

        poruka.setBroj2(IdMes);

       
        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem3Primalac, objPoruka);
     }
    
}
