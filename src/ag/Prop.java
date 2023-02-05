/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import java.io.File;
import java.io.Serializable;

/**
 * Se incarca la pornirea aplicatiei si se salveaza la iesire.
 * Pastreaza calea catre ultimul fisier salvat si memoreaza daca a fost validat codul de Inregistrare;
 * 

 */
public class Prop implements Serializable{
    File f;
    boolean regStatus;
    

 

    public File getF() {
        return f;
    }

    public String getAcale() {
        return this.f.getAbsolutePath();
    }


    public void setF(File f) {
        this.f = f;
    }

    public boolean isRegStatus() {
        return regStatus;
    }

    public void setRegStatus(boolean regStatus) {
        this.regStatus = regStatus;
    }

}
