/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package podsistem2;

import entiteti.Kategorija;
import entiteti.Korisnik;
import entiteti.Video;
import entiteti.VideoKategorija;
import entiteti.VideoKategorijaPK;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
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
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import poruka.Poruka;

/**
 *
 * @author Petar
 */
public class PodSistem2 {
    
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
    
    private static int primljeno_zahtev6=0;
    private static int primljeno_zahtev7=0;
    private static int primljeno_zahtev16=0;

 public static void main(String[] args) {
        
        emf= Persistence.createEntityManagerFactory("PodSistem2PU");
        em=emf.createEntityManager();
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueuePodsistem2Primalac);
      
        System.out.println("podsistem2.PodSistem2.main()");
        primljeno_zahtev6=0;
        primljeno_zahtev7=0;
        primljeno_zahtev16=0;
        
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof ObjectMessage)
                {
                    System.out.println("USLO2");
                    ObjectMessage obj = (ObjectMessage)message;
                    Poruka poruka=null;
                   
                     try {
                         poruka = (Poruka)(obj.getObject());
                     } catch (JMSException ex) {
                         Logger.getLogger(PodSistem2.class.getName()).log(Level.SEVERE, null, ex);
                     }
                    
                     int zahtev = poruka.getBroj_zahteva();
                     
                     switch(zahtev)
                     {
                        case 2:
                        {
                            //Dobija poruku od podsistema 1 i salje mu odgovor nazad
                            String ime = poruka.getPolje1();
                            String email = poruka.getPolje2();
                            String pol = poruka.getPolje3();
                            int godiste = poruka.getBroj1();
                            int mesto = poruka.getBroj2();
                            int IdKor = KreirajKorisnika(ime,email,godiste,pol,mesto);
                            Poruka odgovor = new Poruka(102);
                            
                            
                            odgovor.setPolje1(ime);
                            odgovor.setPolje2(email);
                            odgovor.setPolje3(pol);
                            odgovor.setBroj1(IdKor);
                            odgovor.setBroj2(godiste);
                            odgovor.setBroj3(mesto);
       
                            ObjectMessage objPoruka = context.createObjectMessage(odgovor);
                            producer.send(QueuePodsistem1Primalac, objPoruka);
                        }
                        break;
                         
                         
                        
                        case 3:
                        {
                            //Dobija poruku od podsistema 1 i salje mu odgovor nazad
                            String email = poruka.getPolje1();
                            int IdKor = poruka.getBroj1();
                            Korisnik k = em.find(Korisnik.class,IdKor);
                          
                            
                            em.getTransaction().begin();
                            k.setEmail(email);
                            em.getTransaction().commit();
                            
                            Poruka odgovor = new Poruka(103);
                            odgovor.setBroj1(IdKor);
                            
                            System.out.println("ODGOVOR POSLAT");
       
                            ObjectMessage objPoruka = context.createObjectMessage(odgovor);
                            producer.send(QueuePodsistem1Primalac, objPoruka);
                        }
                        break;
                        
                        
                        case 4:
                        {
                            //Dobija poruku od podsistema 1 i salje mu odgovor nazad
                           
                            int IdKor = poruka.getBroj1();
                            int mesto = poruka.getBroj2();
                            Korisnik k = em.find(Korisnik.class,IdKor);
                          
                            
                            em.getTransaction().begin();
                            k.setMesto(mesto);
                            em.getTransaction().commit();
                            
                            Poruka odgovor = new Poruka(104);
                            odgovor.setBroj1(IdKor);
                            
                            System.out.println("ODGOVOR POSLAT");
       
                            ObjectMessage objPoruka = context.createObjectMessage(odgovor);
                            producer.send(QueuePodsistem1Primalac, objPoruka);
                        }
                        break;
                         
                        
                        
                        case 5:
                        {
                            String naziv = poruka.getPolje1();
                            Kategorija kategorija = new Kategorija(naziv);
                            em.getTransaction().begin();
                            em.persist(kategorija);
                            em.getTransaction().commit();
                            
                            Poruka novaPoruka = new Poruka(5);
                                
                            novaPoruka.setBroj1(kategorija.getIdKat());
                            novaPoruka.setPolje1(kategorija.getNaziv());
                         
                                 
                            System.out.println("POSLATO");
                            
                            ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                            producer.send(QueueRestServer, objPorukaServer);
                            
                            //SALJI SERVERU ODGOVOR
                                    
                        }
                        break;
                         
                         
                        case 6:
                        {
                            String naziv = poruka.getPolje1();
                            int trajanje = poruka.getBroj1();
                            int IdKor = poruka.getBroj2();
                            Integer dan = poruka.getBroj3();
                            Integer mesec = poruka.getBroj4();
                            Integer godina = poruka.getBroj5();
                            
                            Integer sat = poruka.getBroj6();
                            Integer minut = poruka.getBroj7();
                            Integer sekund = poruka.getBroj8();
                            
                            Korisnik korisnik = em.find(Korisnik.class,IdKor);
                            
                            if (korisnik==null)
                            {
                                Poruka slanje = new Poruka(-1);
                                slanje.setBroj1(-1);
                                ObjectMessage objPorukaServer = context.createObjectMessage(slanje);
                                producer.send(QueueRestServer, objPorukaServer);
                            }
                            
                            else
                            {
                                String datumS = godina.toString()+"-"+mesec.toString()+"-"+dan.toString();
                            
                                String vremeS = sat.toString()+":" +minut.toString() +":" +sekund.toString();

                                Date datum = Date.valueOf(datumS);
                                Time vreme = Time.valueOf(vremeS);

                                Video video = new Video(naziv, trajanje, korisnik, datum, vreme);

                                em.getTransaction().begin();
                                em.persist(video);
                                em.getTransaction().commit();

                                obavestiPodsistem3_zahtev6(naziv, trajanje, IdKor, dan,mesec,godina, sat,minut,sekund);
                            }
                            

                        }
                        break;
                        
                        case 206:
                        {   
                            primljeno_zahtev6++;
                            if (primljeno_zahtev6==1)
                            {
                                primljeno_zahtev6=0;
                                // Slanje ka serveru
                                
                            Poruka odgovor = new Poruka(206);
                            
                            
                            odgovor.setBroj1(poruka.getBroj1());
                            
                                System.out.println("POSLATO SERVERU");
       
                            ObjectMessage objPoruka = context.createObjectMessage(odgovor);
                            producer.send(QueueRestServer, objPoruka);
                                
                                
                                
                                
                            }
                              
                        }
                        break;
                         
                         
                        case 7:
                        {
                            String naziv = poruka.getPolje1();
                            int IdVid = poruka.getBroj1();
                            Video video = em.find(Video.class,IdVid);
                            
                            if (video==null)
                            {
                                Poruka slanje = new Poruka(-1);
                                slanje.setBroj1(-1);
                                ObjectMessage objPorukaServer = context.createObjectMessage(slanje);
                                producer.send(QueueRestServer, objPorukaServer);                                
                            }
                            
                            else
                            {
                                em.getTransaction().begin();
                                video.setNaziv(naziv);
                                em.getTransaction().commit();

                                obavestiPodsistem3_zahtev7(naziv, IdVid);                                
                            }

                            
                            
                   
                        }
                        break;
                        
                        case 207:
                        {   
                            primljeno_zahtev7++;
                            if (primljeno_zahtev7==1)
                            {
                                primljeno_zahtev7=0;
                                // Slanje ka serveru
                                
                            Poruka odgovor = new Poruka(207);
                            
                            
                            odgovor.setBroj1(poruka.getBroj1());
                            odgovor.setBroj2(poruka.getBroj2());
                            odgovor.setBroj3(poruka.getBroj3());
                            odgovor.setBroj4(poruka.getBroj4());
                            odgovor.setBroj5(poruka.getBroj5());
                            
                            System.out.println("POSLATO SERVERU");
       
                            ObjectMessage objPoruka = context.createObjectMessage(odgovor);
                            producer.send(QueueRestServer, objPoruka);
                            }
                              
                        }
                        break;
                        
                        
                         
                        case 8:
                        {
                            int IdVid = poruka.getBroj1();
                            int IdKat = poruka.getBroj2();
                            
                            Video video = em.find(Video.class,IdVid);
                            Kategorija kategorijaa = em.find(Kategorija.class,IdKat);
                            
                            if (video==null || kategorijaa==null)
                            {
                                Poruka slanje = new Poruka(-2);
                                slanje.setBroj1(-2);
                                ObjectMessage objPorukaServer = context.createObjectMessage(slanje);
                                producer.send(QueueRestServer, objPorukaServer); 
                            }
                            
                            else
                            {
                                TypedQuery<VideoKategorija> query = em.createQuery("SELECT k FROM VideoKategorija k", VideoKategorija.class);

                                List<VideoKategorija> results = query.getResultList();

                                    int nadjeno = 0;

                                //SLANJE SERVERU

                                for (VideoKategorija vk:results)
                                {

                                    if (vk.getKategorija().getIdKat()==IdKat && vk.getVideo().getIdVid()==IdVid)
                                    {
                                        nadjeno = 1;
                                        break;
                                    }


                                }






                               if (nadjeno==0)
                               {
                                       VideoKategorijaPK video_kategorijaPK = new VideoKategorijaPK(IdVid,IdKat);

                                VideoKategorija video_kategorija = new VideoKategorija(video_kategorijaPK);
                                video_kategorija.setKategorija(kategorijaa);
                                video_kategorija.setVideo(video);
                                video_kategorija.setPolje("polje");

                                em.getTransaction().begin();
                                em.persist(video_kategorija);
                                em.getTransaction().commit();

                                Poruka odgovor = new Poruka(8);
                                odgovor.setBroj1(8);

                                System.out.println("POSLATO SERVERU");

                                ObjectMessage objPoruka = context.createObjectMessage(odgovor);
                                producer.send(QueueRestServer, objPoruka);

                                //SLANJE KA SERVERU ODGOVOR
                               }
                               else
                               {

                                 Poruka odgovor = new Poruka(-1);
                                 odgovor.setBroj1(-1);

                                System.out.println("POSLATO SERVERU NEUSPESNO");

                                ObjectMessage objPoruka = context.createObjectMessage(odgovor);
                                producer.send(QueueRestServer, objPoruka);
                        
                            
                            
                                }
                            }
                            

                        }
                         break;
                         
                        case 16:
                        {                            
                            int IdVid = poruka.getBroj1();
                            int IdKor = poruka.getBroj2();
                            
                            
                            Video video = em.find(Video.class,IdVid);
                            Korisnik korisnik = em.find(Korisnik.class,IdKor);
                            
                            if (korisnik==null || video==null)
                            {
                                Poruka odgovor = new Poruka(-2);
                                odgovor.setBroj1(-2);
                            
                                System.out.println("POSLATO SERVERU NEUSPESNO");
       
                                ObjectMessage objPoruka = context.createObjectMessage(odgovor);
                                producer.send(QueueRestServer, objPoruka);
                            }
                            
                            else if(video.getKorisnik().getIdKor()==korisnik.getIdKor())
                            {

                                      
                                     //Brisanje ako postoje VIDEO_KATEGORIJA 
                                     
//                                     TypedQuery<VideoKategorija> query = em.createQuery("SELECT vk FROM VideoKategorija vk", VideoKategorija.class);
//                                     List<VideoKategorija> results = query.getResultList();
//                                      
//                                     for (VideoKategorija vk:results)
//                                     {
//                                         if (vk.getVideo().getIdVid()==IdVid)
//                                         {
//                                             em.getTransaction().begin();
//                                             em.remove(vk);
//                                             em.getTransaction().commit();
//                                         }
//                                     }
                                     
                                    em.getTransaction().begin();
                                    em.remove(video);
                                    em.getTransaction().commit();
                                    System.out.println("POSLATO SERVERU USPESNO");
                                      //SLANJE KA SERVERU ODGOVOR
                                      
                                    obavestiPodsistem3_zahtev16(IdVid,IdKor);
                                    
                                    
                                    
                                    
                            }
                            
                            else
                            {
                              
                                Poruka odgovor = new Poruka(-1);
                                odgovor.setBroj1(-1);
                            
                            System.out.println("POSLATO SERVERU NEUSPESNO");
       
                            ObjectMessage objPoruka = context.createObjectMessage(odgovor);
                            producer.send(QueueRestServer, objPoruka);
                            }
                    
                        }
                         break;
                         
                         
                        case 2016:
                        {
                            primljeno_zahtev16++;
                            if (primljeno_zahtev16==1)
                            {
                                primljeno_zahtev16=0;
                                // Slanje ka serveru
                                
                                                              
                                Poruka odgovor = new Poruka(16);
                                odgovor.setBroj1(16);
                            
                                System.out.println("POSLATO SERVERU");
       
                                ObjectMessage objPoruka = context.createObjectMessage(odgovor);
                                producer.send(QueueRestServer, objPoruka);
                            }
                        }
                         break;
                         
                         
                        case 19:
                         {
                            TypedQuery<Kategorija> query = em.createQuery("SELECT k FROM Kategorija k", Kategorija.class);
                            List<Kategorija> results = query.getResultList();
                            Poruka nova = new Poruka(19);
                            nova.setBroj1(results.size());
                            ObjectMessage objPoruka = context.createObjectMessage(nova);
                            
                            
                            
                            producer.send(QueueRestServer, objPoruka);
                            
                            //SLANJE SERVERU
                            
                            for (Kategorija kategorija:results)
                            {
                           
                                Poruka novaPoruka = new Poruka(19);
                                novaPoruka.setBroj1(kategorija.getIdKat());
                                novaPoruka.setPolje1(kategorija.getNaziv());
                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                
                                System.out.println("SALJE KAT");
                                
                                
                                producer.send(QueueRestServer, objPorukaServer);
                                
                                //SLANJE SERVERU
                            }
                         }
                         break;
                         
                        case 20:
                         {                         
                            TypedQuery<Video> query = em.createQuery("SELECT v FROM Video v", Video.class);
                            List<Video> results = query.getResultList();
                            Poruka nova = new Poruka(20);
                            nova.setBroj1(results.size());
                            ObjectMessage objPoruka = context.createObjectMessage(nova);
                            
                            producer.send(QueueRestServer, objPoruka);
                            
                            //SLANJE SERVERU
                            
                            for (Video video:results)
                            {
                           
                                Poruka novaPoruka = new Poruka(20);
                                novaPoruka.setBroj1(video.getIdVid());
                                novaPoruka.setPolje1(video.getNaziv());
                                novaPoruka.setBroj2(video.getTrajanje());
                                novaPoruka.setBroj3(video.getKorisnik().getIdKor());
                                
                                String dateFormatPattern = "yyyy-MM-dd";
                                
                                String vreme = video.getVreme().toString();
                                
                                DateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
                                
                                System.out.println(video.getDatum());
                                
                                String datum = dateFormat.format(video.getDatum());
                                
                                System.out.println(datum);
                                
                                String godinas ="";
                                godinas+=datum.charAt(0);
                                godinas+=datum.charAt(1);
                                godinas+=datum.charAt(2);
                                godinas+=datum.charAt(3);

                                String mesecS ="";
                                mesecS+=datum.charAt(5);
                                mesecS+=datum.charAt(6);

                                System.out.println(vreme);

                                String danS ="";
                                danS+=datum.charAt(8);
                                danS+=datum.charAt(9);

                                String satS ="";
                                satS+=vreme.charAt(0);
                                satS+=vreme.charAt(1);

                                String minutS ="";
                                minutS+=vreme.charAt(3);
                                minutS+=vreme.charAt(4);

                                String sekundS ="";
                                sekundS+=vreme.charAt(6);
                                sekundS+=vreme.charAt(7);
                                
                                int datumInt = Integer.parseInt(godinas)*10000 + Integer.parseInt(mesecS)*100 + Integer.parseInt(danS);
 
                                int vremeInt = Integer.parseInt(satS)*10000 + Integer.parseInt(minutS)*100 + Integer.parseInt(sekundS);
                                
                                novaPoruka.setBroj4(datumInt);
                                novaPoruka.setBroj5(vremeInt);
                                
                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                
                                //SLANJE SERVERU
                                
                                System.out.println("SALJE VIDEO");
                                
                                
                                producer.send(QueueRestServer, objPorukaServer);
                                
                                
                                
                                
                                
                            }
                         }
                         break;
                         
                        case 21:
                        {
                            TypedQuery<VideoKategorija> query = em.createNamedQuery("VideoKategorija.findByIdVid",VideoKategorija.class);
                            query.setParameter("idVid",poruka.getBroj1());
                            List<VideoKategorija> results = query.getResultList();
                            Poruka nova = new Poruka(21);
                            nova.setBroj1(results.size());
                            ObjectMessage objPoruka = context.createObjectMessage(nova);
                            
                            producer.send(QueueRestServer, objPoruka);
                            
                            //SLANJE SERVERU
                            
                            for (VideoKategorija video_kat:results)
                            {
                           
                                Poruka novaPoruka = new Poruka(21);
                                novaPoruka.setBroj1(video_kat.getKategorija().getIdKat());
                                
                                novaPoruka.setPolje1(video_kat.getKategorija().getNaziv());

                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                
                                System.out.println(video_kat.getKategorija().getNaziv());
                                
                                producer.send(QueueRestServer, objPorukaServer);
                                
                                //SLANJE SERVERU
                            }
                            
                            
                            
                        }break;
                        
                        
                        
                       
                     }
                    
                }
            }
        });
        
        
        while (true)
        {
            
        }
        
        
        
        
       
    }

  
    public static int KreirajKorisnika(String ime,String email,int godiste,String pol,int mesto)
    {
        Korisnik k = new Korisnik(ime,email,godiste,pol,mesto);

        em.getTransaction().begin();
        em.persist(k);
        em.getTransaction().commit();
        System.out.println("OK poslato\n");
        return k.getIdKor();
    }
    
    
    public static void obavestiPodsistem3_zahtev6(String naziv,int trajanje, int korisnik, int dan,int mesec,int godina,int sat,int minut,int sekund)
    {
        Poruka poruka = new Poruka(6);
        poruka.setPolje1(naziv);
        poruka.setBroj1(trajanje);
        poruka.setBroj2(korisnik);
        poruka.setBroj3(dan);
        poruka.setBroj4(mesec);
        poruka.setBroj5(godina);
        poruka.setBroj6(sat);
        poruka.setBroj7(minut);
        poruka.setBroj8(sekund);

       
        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem3Primalac, objPoruka);
        
    }
    
    public static void obavestiPodsistem3_zahtev7(String naziv,int IdVid)
    {
        Poruka poruka = new Poruka(7);
        poruka.setBroj1(IdVid);
        poruka.setPolje1(naziv);

       
        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem3Primalac, objPoruka);
    }
   
        
    public static void obavestiPodsistem3_zahtev16(int IdVid,int IdKor)
    {
        Poruka poruka = new Poruka(16);
        poruka.setBroj1(IdVid);
        poruka.setBroj2(IdKor);

       
        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem3Primalac, objPoruka);
    }
}
