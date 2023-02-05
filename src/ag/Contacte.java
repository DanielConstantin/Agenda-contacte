/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import javax.swing.JOptionPane;

/**
 *
 * @author costareea
 */
public class Contacte implements Serializable{
   private LinkedList<Contact> clst;

    public Contacte() {
        this.clst=new LinkedList<>();
    }

    public LinkedList<Contact> getClst() {
        return clst;
    }

    public void setClst(LinkedList<Contact> clst) {
        this.clst = clst;
    }
    
   public void sortDupaNume(){
       Collections.sort(this.clst, 
                        (o1, o2) -> o1.getNume().compareTo(o2.getNume()));    
   }
   public void sortDupaPrenume(){
       Collections.sort(this.clst, 
                        (o1, o2) -> o1.getPrenume().compareTo(o2.getPrenume()));  
   }
     
   
       public void sortNrTel(){
       Collections.sort(this.clst, 
                        (o1, o2) -> o1.getNumar().toString().compareTo(o2.getNumar().toString()));
       
   }
      public void sortData(){
       Collections.sort(this.clst, 
                        (o1, o2) -> o1.getData().compareTo(o2.getData()));
       
   }
}
 class Contact implements Serializable{
    private String nume;
    private String prenume;
    private LocalDate dataNasterii;
    private NrTel numar;
    public Contact(String n, String pn, String dn,NrTel nr){
        long num=
                n.chars()
                .mapToObj(i->i)
                .filter(i-> Character.isLetter(i))
                .count();
        
        if(n==null||num<2){
            throw new IllegalArgumentException("Numele trebuie sa contina minim 2 caractere"); 
        }
        long pnum=
                pn.chars()
                .mapToObj(i->i)
                .filter(i-> Character.isLetter(i))
                .count();
        
        if(pn==null||pnum<2){
            throw new IllegalArgumentException("Prenumele trebuie sa contina minim 2 litere");
        }
        if(validareData(dn)){
            
        DateTimeFormatter ft=DateTimeFormatter.ofPattern("d.MM.yyyy");
        LocalDate dt=LocalDate.parse(dn, ft);
        if(dt.isAfter(LocalDate.now())|| !dt.isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Te nasti azi?");    
        }
        this.dataNasterii=dt;
        }
        else{
            throw new IllegalArgumentException("Data nasterii nu e valida");
        }
        this.nume=n;
        this.prenume=pn;
        this.numar=nr;
    }

    boolean validareData(String d){
        try{
        DateTimeFormatter ft=DateTimeFormatter.ofPattern("d.MM.yyyy");
        LocalDate dt=LocalDate.parse(d, ft);
        return true;
        }
        catch(Exception e){
            return false;
        }
    }
    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getDataNasterii() {
        DateTimeFormatter ft=DateTimeFormatter.ofPattern("d.MM.yyyy");
        
        return dataNasterii.format(ft);
    }
     public LocalDate getData() {
        
        return this.dataNasterii;
    }

    public void setDataNasterii(String dataNasterii) {
       
        DateTimeFormatter ft=DateTimeFormatter.ofPattern("d.MM.yyyy");
        LocalDate dt=LocalDate.parse(dataNasterii, ft);
        if(dt.isAfter(LocalDate.now())|| dt.isEqual(LocalDate.now())){
            throw new IllegalArgumentException("Ia-ti telefon dupa ce te nasti!");    
        }
        
        this.dataNasterii=dt;
    
    }
    public NrTel getNumar() {
        return numar;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Contact other = (Contact) obj;
        if (!this.nume.equalsIgnoreCase( other.nume)) {
            return false;
        }
        if (!this.prenume.equalsIgnoreCase(other.prenume)) {
            return false;
        }
        if (!Objects.equals(this.dataNasterii, other.dataNasterii)) {
            return false;
        }
        if (!Objects.equals(this.numar, other.numar)) {
            return false;
        }
        return true;
    }
    
    public void setNumar(NrTel numar) {
        this.numar = numar;
    }

    @Override
    public String toString() {
        return nume+", "+ prenume+", "+this.getDataNasterii().toString()+", "+ numar.toString();
    }
     public String toStringFilter() {
        return nume+", "+ prenume+", "+ numar.toString();
    }
    
}
class NrFix extends NrTel implements Serializable {

    public NrFix(String n) {
        super(n);
    }

    @Override
    void validareNumar(String n) {
        if(n.startsWith("02")||n.startsWith("03")){}
        else{
            throw new IllegalArgumentException("Numar fix invalid");
        }
         //To change body of generated methods, choose Tools | Templates.
    long hn=
                n.chars()
                .mapToObj(i->i)
                .filter(i-> Character.isDigit(i))
                .count();
        if(hn!=10){
            throw new IllegalArgumentException("Numar telefon invalid");
        }
    }

    @Override
    public int compareTo(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
class NrMobil extends NrTel implements Serializable {

    public NrMobil(String n) {
        super(n);
        
    }

    @Override
    void validareNumar(String n){
        if(n.startsWith("07")){}
        else{
        throw new IllegalArgumentException("Numar mobil invalid"); //To change body of generated methods, choose Tools | Templates.
        }
        long pn=
                n.chars()
                .mapToObj(i->i)
                .filter(i-> Character.isDigit(i))
                .count();
        if(pn!=10){
            throw new IllegalArgumentException("Numar telefon invalid");
        }
    }

    @Override
    public int compareTo(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
abstract class NrTel implements Comparable, Serializable{
    String ntel;
    
      NrTel(String ntel){
          validareNumar(ntel);
          this.ntel=ntel;
          
      }

    abstract void validareNumar(String n);
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NrTel other = (NrTel) obj;
        if (!Objects.equals(this.ntel, other.ntel)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  ntel;
    }
    
    
}
