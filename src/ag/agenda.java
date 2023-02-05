/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ag;

import java.awt.SplashScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author costareea
 */
public class agenda extends javax.swing.JFrame {
    
    DefaultListModel<Contact> dml;
    Contacte c = new Contacte();
    Prop ild = new Prop();
    boolean stat = false;
    final File salvare = new File("s.sav");
    final int regc = 5421;

    /**
     * Creates new form agenda
     */
    public agenda() {
        initComponents();
        ild = Istatus();
        incarcModel(ild.getF());
        stat = ild.isRegStatus();
        this.setLocationRelativeTo(null);
        btnchildadauga.addActionListener(new agenda.HandlerButoane());
        btnstergere.addActionListener(new agenda.HandlerButoane());
        btnediteaza.addActionListener(new agenda.HandlerButoane());
        btnordoneaza.addActionListener(new agenda.HandlerButoane());
        cmbtipfiltru.addActionListener(new agenda.HandlerButoane());
        btnfiltreaza.addActionListener(new agenda.HandlerButoane());
        
        UpdateList();
        
        ActionListener banner = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!stat) {
                    int pim = (int) (Math.random() * 3) + 1;
                    String nimg = "image" + (pim) + ".png";
                    limagini.setIcon(new ImageIcon(getClass().getResource(nimg)));
                } else {
                //    jPanel1.remove(limagini);
                //    MainPanel.remove(jPanel1);
                //    MainPanel.invalidate();
                //    MainPanel.validate();
                   limagini.setIcon(new ImageIcon(getClass().getResource("splash.jpg")));
                }
            }
        };
        javax.swing.Timer tmrBanner = new javax.swing.Timer(0, banner);
        
        tmrBanner.start();
        tmrBanner.setDelay(1500);        
        
        if (stat) {
            menuInregistrare.setEnabled(false);
            menuDeschidere.setEnabled(true);
            menuSalvare.setEnabled(true);
            
            tmrBanner.stop();
          //  jPanel1.remove(limagini);
           // MainPanel.remove(jPanel1);
           // MainPanel.invalidate();
           // MainPanel.validate();
            limagini.setIcon(new ImageIcon(getClass().getResource("splash.jpg")));
        } else {
            menuInregistrare.setEnabled(true);
            menuDeschidere.setEnabled(false);
            menuSalvare.setEnabled(false);
            
        }
        
        Thread s = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(agenda.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (ild.getF() != null && ild.getF().canRead()) {
                    while (ild.getF() != null && ild.getF().canRead()) {
                        File f = new File(ild.getF().getPath());
                        try {
                            FileOutputStream fos = new FileOutputStream(f);
                            ObjectOutputStream oos = new ObjectOutputStream(fos);
                            oos.writeObject(c);
                            oos.close();
                            fos.close();
                            ild = new Prop();
                            ild.setF(f);
                            System.out.println("Salvare intermediara");
                            try {
                                Thread.sleep(60000);
                            } catch (InterruptedException ex) {
                                System.out.println("eroare salvare:");
                            }
                        } catch (Exception e) {
                            System.out.println("Eroare!");
                        }
                    }
                }
            }
        });
        
        new Thread(s).start();
        aniversari();
    }
    
    private void UpdateList() {
        dml = new DefaultListModel<Contact>();
        
        c.getClst().forEach((ct) -> {
            dml.addElement(ct);
        });
        lstcontacte.setModel(dml);
    }

    private void adauga() {
        if (btnchildadauga.getText().equalsIgnoreCase("Adauga")) {
            if (rbtelfix.isSelected()) {
                Contact ctct = new Contact(txtnume.getText(), txtprenume.getText(), txtdata.getText(), new NrFix(txtnr.getText()));
                if ((ctct.getData().getDayOfMonth() == (LocalDate.now().getDayOfMonth())) && (ctct.getData().getMonth().equals(LocalDate.now().getMonth()))) {
                    JOptionPane.showMessageDialog(this, "La multi ani!", "Aniversare!", JOptionPane.INFORMATION_MESSAGE);
                }
                long l
                        = c.getClst().stream().filter(p -> p.equals(ctct)).count();
                if (l == 0) {
                    c.getClst().add(ctct);
                    dml.addElement(ctct);
                    //golesteSuflete();
                } else {
                    throw new IllegalArgumentException("Contact deja existent");
                }
            } else {
                Contact ctct = new Contact(txtnume.getText(), txtprenume.getText(), txtdata.getText(), new NrMobil(txtnr.getText()));
                if ((ctct.getData().getDayOfMonth() == (LocalDate.now().getDayOfMonth())) && (ctct.getData().getMonth().equals(LocalDate.now().getMonth()))) {
                    JOptionPane.showMessageDialog(this, "La multi ani!", "Aniversare!", JOptionPane.INFORMATION_MESSAGE);
                }
                long l
                        = c.getClst().stream().filter(p -> p.equals(ctct)).count();
                if (l == 0) {
                    c.getClst().add(ctct);
                    dml.addElement(ctct);
                    golesteSuflete();
                    
                } else {
                    throw new IllegalArgumentException("Contact deja existent");
                }
            }
        } else {
            Contact ctct;
            if (rbmobil.isSelected()) {
                ctct = new Contact(txtnume.getText(), txtprenume.getText(), txtdata.getText(), new NrMobil(txtnr.getText()));
            } else {
                ctct = new Contact(txtnume.getText(), txtprenume.getText(), txtdata.getText(), new NrFix(txtnr.getText()));                
            }
            if (c.getClst().stream().filter(p -> p.equals(ctct)).count() == 0) {
                c.getClst().get(lstcontacte.getSelectedIndex()).setNume(txtnume.getText());
                c.getClst().get(lstcontacte.getSelectedIndex()).setPrenume(txtprenume.getText());
                c.getClst().get(lstcontacte.getSelectedIndex()).setDataNasterii(txtdata.getText());
                if (rbmobil.isSelected()) {
                    c.getClst().get(lstcontacte.getSelectedIndex()).setNumar(new NrMobil(txtnr.getText()));
                } else {
                    c.getClst().get(lstcontacte.getSelectedIndex()).setNumar(new NrFix(txtnr.getText()));
                }
                
                dml.setElementAt(ctct, lstcontacte.getSelectedIndex());
                golesteSuflete();
                Dadauga.setVisible(false);
                
            } else {
                throw new IllegalArgumentException("Contact deja existent");
            }
            btnchildadauga.setText("Adauga");
            Dadauga.setVisible(false);
        }
        
    }

    public void iesire() {
        if (JOptionPane.showConfirmDialog(this, "Doriti sa parasiti aplicatia?", "Confirmare Iesire", JOptionPane.YES_NO_OPTION) == 1) {
        } else {
            try {
                if (ild.getF() != null) {
                    FileOutputStream gos = new FileOutputStream(salvare);
                    ObjectOutputStream ros = new ObjectOutputStream(gos);
                    ild.setRegStatus(stat);
                    ros.writeObject(ild);
                    ros.close();
                    gos.close();
                    File f = ild.getF();
                    try {
                        FileOutputStream fos = new FileOutputStream(f);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(c);
                        oos.close();
                        fos.close();
                        this.setTitle("Agenda Telefonica: " + ild.getF().getName());
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Eroare la salvarea datelor!", "EROARE", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    if (c.getClst().size() > 0) {
                        if (JOptionPane.showConfirmDialog(this, "Salvati datele?", "Salveaza", JOptionPane.YES_NO_OPTION) == 1) {
                        } else {
                            mSave();
                            FileOutputStream gos = new FileOutputStream(salvare);
                            ObjectOutputStream ros = new ObjectOutputStream(gos);
                            ild.setRegStatus(stat);
                            ros.writeObject(ild);
                            ros.close();
                            gos.close();
                        }
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Eroare la salvarea datelor!", "EROARE", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
        }
    }
    
    private void aniversari() {
        LocalDate ld = LocalDate.now();
        DefaultListModel<Contact> model = new DefaultListModel<>();
        for (Contact s : c.getClst()) {            
            if ((s.getData().getDayOfMonth() == (ld.getDayOfMonth())) && (s.getData().getMonth().equals(ld.getMonth()))) {
                if (!model.contains(s)) {
                    model.addElement(s);
                }
            } else {
                if (model.contains(s)) {
                    model.removeElement(s);
                }
            }
        }
        if (model.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nu sunt aniversari astazi!", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            lstaniversare.setModel(model);
            NascutiAstazi.setUndecorated(false);
            NascutiAstazi.pack();
            NascutiAstazi.setLocationRelativeTo(null);
            NascutiAstazi.setVisible(true);
        }        
    }

    private void golesteSuflete() {
        txtnume.setText("");
        txtprenume.setText("");
        txtnr.setText("");
        txtdata.setText("");
    }

    private void sterge() {
        //DefaultListModel mdll= (DefaultListModel)lstcontacte.getModel();
        int selectedIndex = lstcontacte.getSelectedIndex();
        if (selectedIndex > -1) {
            int n = JOptionPane.showConfirmDialog(this, "Sunteti sigur ca doriti sa stergeti contactul", "Confirmare", JOptionPane.OK_CANCEL_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                c.getClst().remove(selectedIndex);
                dml.remove(selectedIndex);
            } else {
                
            }
            
        }
    }

    private void ordoneaza() {
        switch (cmbordonare.getSelectedIndex()) {
            case 0:
                c.sortDupaNume();
                break;
            case 1:
                c.sortDupaPrenume();
                break;
            case 2:
                c.sortData();
                break;
            default:
                c.sortNrTel();
        }
        
        UpdateList();
    }

    private void editeaza() {
        
        int selectedIndex = lstcontacte.getSelectedIndex();
        if (selectedIndex > -1) {
            
            txtnume.setText(c.getClst().get(selectedIndex).getNume());
            txtprenume.setText(c.getClst().get(selectedIndex).getPrenume());
            txtdata.setText(c.getClst().get(selectedIndex).getDataNasterii().toString());
            txtnr.setText(c.getClst().get(selectedIndex).getNumar().toString());
            
        }
    }
    
    private void filtreaza() {
        String filter = txtfiltru.getText();
        
        filterModel((DefaultListModel<Contact>) lstcontacte.getModel(), filter, cmbtipfiltru.getSelectedIndex());
    }

    public void incarcModel(File f) {
        
        try {
            if (f.exists()) {
                try {
                    
                    FileInputStream fis = new FileInputStream(f);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    c = ((Contacte) ois.readObject());
                    // c = mdl;
                    ois.close();
                    fis.close();
                    this.setTitle("Agenda Telefonica: " + ild.getF().getName());
                    UpdateList();
                } catch (FileNotFoundException fnf) {
                    JOptionPane.showMessageDialog(this, "Ultimul fisier salvat nu mai este disponibil!", "EROARE", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Eroare la restaurarea datelor din sesiunea anterioara!", "EROARE", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Dadauga = new javax.swing.JDialog();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        rbtelfix = new javax.swing.JRadioButton();
        rbmobil = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        txtnume = new javax.swing.JTextField();
        txtprenume = new javax.swing.JTextField();
        txtdata = new javax.swing.JTextField();
        txtnr = new javax.swing.JTextField();
        btnchildadauga = new javax.swing.JButton();
        btnanuleaza = new javax.swing.JButton();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jfc = new javax.swing.JFileChooser();
        NascutiAstazi = new javax.swing.JDialog();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstaniversare = new javax.swing.JList<>();
        dAbout = new javax.swing.JDialog();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        bclose = new javax.swing.JButton();
        MainPanel = new javax.swing.JPanel();
        btnfiltreaza = new javax.swing.JButton();
        cmbtipfiltru = new javax.swing.JComboBox<>();
        cmbordonare = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        txtfiltru = new javax.swing.JTextField();
        btnediteaza = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        limagini = new javax.swing.JLabel();
        btnordoneaza = new javax.swing.JButton();
        btnadauga = new javax.swing.JButton();
        btnstergere = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstcontacte = new javax.swing.JList<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuSalvare = new javax.swing.JMenuItem();
        menuDeschidere = new javax.swing.JMenuItem();
        menuIesire = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        menuInregistrare = new javax.swing.JMenuItem();
        menuDespre = new javax.swing.JMenuItem();

        Dadauga.setMinimumSize(new java.awt.Dimension(620, 181));
        Dadauga.setResizable(false);
        Dadauga.setSize(new java.awt.Dimension(571, 154));
        Dadauga.setType(java.awt.Window.Type.POPUP);

        jLabel4.setText("Nume:");

        jLabel5.setText("Prenume:");

        jLabel6.setText("Numar telefon:");

        buttonGroup1.add(rbtelfix);
        rbtelfix.setText("Telefon Fix");

        buttonGroup1.add(rbmobil);
        rbmobil.setSelected(true);
        rbmobil.setText("Telefon Mobil");

        jLabel7.setText("Data Naterii:");

        btnchildadauga.setText("Adauga");
        btnchildadauga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnchildadaugaActionPerformed(evt);
            }
        });

        btnanuleaza.setText("Anuleaza");
        btnanuleaza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnanuleazaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout DadaugaLayout = new javax.swing.GroupLayout(Dadauga.getContentPane());
        Dadauga.getContentPane().setLayout(DadaugaLayout);
        DadaugaLayout.setHorizontalGroup(
            DadaugaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DadaugaLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(DadaugaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DadaugaLayout.createSequentialGroup()
                        .addComponent(btnanuleaza, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(btnchildadauga, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(23, 23, 23))
                    .addGroup(DadaugaLayout.createSequentialGroup()
                        .addGroup(DadaugaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(DadaugaLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtdata))
                            .addGroup(DadaugaLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(txtnume, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(12, 12, 12)
                        .addGroup(DadaugaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(DadaugaLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtprenume)
                                .addGap(18, 18, 18)
                                .addComponent(rbtelfix)
                                .addGap(22, 22, 22))
                            .addGroup(DadaugaLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtnr, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(rbmobil)
                                .addContainerGap())))))
        );
        DadaugaLayout.setVerticalGroup(
            DadaugaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DadaugaLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(DadaugaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(rbtelfix)
                    .addComponent(jLabel5)
                    .addComponent(txtnume, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtprenume, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(DadaugaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbmobil)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(txtdata, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtnr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(DadaugaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnchildadauga)
                    .addComponent(btnanuleaza))
                .addContainerGap())
        );

        NascutiAstazi.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        NascutiAstazi.setTitle("Aniversari");
        NascutiAstazi.setAlwaysOnTop(true);
        NascutiAstazi.setLocationByPlatform(true);
        NascutiAstazi.setMinimumSize(new java.awt.Dimension(405, 239));
        NascutiAstazi.setModal(true);
        NascutiAstazi.setName("Aniversari"); // NOI18N
        NascutiAstazi.setResizable(false);
        NascutiAstazi.setType(java.awt.Window.Type.POPUP);

        jLabel8.setText("Persoanele nascute astazi:");

        jScrollPane3.setViewportView(lstaniversare);

        javax.swing.GroupLayout NascutiAstaziLayout = new javax.swing.GroupLayout(NascutiAstazi.getContentPane());
        NascutiAstazi.getContentPane().setLayout(NascutiAstaziLayout);
        NascutiAstaziLayout.setHorizontalGroup(
            NascutiAstaziLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NascutiAstaziLayout.createSequentialGroup()
                .addGap(111, 111, 111)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NascutiAstaziLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        NascutiAstaziLayout.setVerticalGroup(
            NascutiAstaziLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NascutiAstaziLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                .addContainerGap())
        );

        dAbout.setMinimumSize(new java.awt.Dimension(339, 339));
        dAbout.setModal(true);
        dAbout.setResizable(false);

        jLabel9.setFont(new java.awt.Font("Papyrus", 1, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Agenda Telefonica");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Autor: Andreea Constantin");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Proiect final - Info Academy");

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("JDK 1.8");

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("clasa: 14 iulie 2018");

        bclose.setText("Close");
        bclose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dAboutLayout = new javax.swing.GroupLayout(dAbout.getContentPane());
        dAbout.getContentPane().setLayout(dAboutLayout);
        dAboutLayout.setHorizontalGroup(
            dAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dAboutLayout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(60, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dAboutLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(dAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dAboutLayout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(103, 103, 103))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dAboutLayout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dAboutLayout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dAboutLayout.createSequentialGroup()
                        .addGroup(dAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(bclose, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(52, 52, 52))))
        );
        dAboutLayout.setVerticalGroup(
            dAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dAboutLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(jLabel10)
                .addGap(20, 20, 20)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(bclose)
                .addGap(29, 29, 29))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(784, 730));
        setMinimumSize(new java.awt.Dimension(784, 730));

        btnfiltreaza.setText("Filtreaza");

        cmbtipfiltru.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "personalizat", "cu numar fix", "cu numar mobil", "data nastere astazi", "nascuti luna curenta dupa astazi" }));
        cmbtipfiltru.setToolTipText("");

        cmbordonare.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "dupa Nume", "dupa Prenume", "dupa Data Nasterii", "dupa Telefon" }));

        jLabel1.setText("Filtrare");

        txtfiltru.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtfiltruCaretUpdate(evt);
            }
        });

        btnediteaza.setText("Editeaza Contact Selectat");

        jLabel2.setText("Ordonare");

        limagini.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(limagini, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(limagini, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnordoneaza.setText("Ordoneaza");

        btnadauga.setText("Adauga Contact");
        btnadauga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnadaugaActionPerformed(evt);
            }
        });

        btnstergere.setText("Stergere Contact Selectat");

        jLabel3.setText("Filtru");

        lstcontacte.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(lstcontacte);

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                        .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(MainPanelLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(32, 32, 32)))
                        .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(MainPanelLayout.createSequentialGroup()
                                .addComponent(cmbtipfiltru, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtfiltru, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbordonare, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(MainPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnfiltreaza, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5))
                            .addGroup(MainPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnordoneaza)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addComponent(btnadauga, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addComponent(btnstergere, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnediteaza, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        MainPanelLayout.setVerticalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbtipfiltru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtfiltru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnfiltreaza)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbordonare, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(btnordoneaza))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnediteaza)
                    .addComponent(btnstergere)
                    .addComponent(btnadauga))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        jMenu1.setText("Fisiere");

        menuSalvare.setText("Salvare");
        menuSalvare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSalvareActionPerformed(evt);
            }
        });
        jMenu1.add(menuSalvare);

        menuDeschidere.setText("Deschidere");
        menuDeschidere.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDeschidereActionPerformed(evt);
            }
        });
        jMenu1.add(menuDeschidere);

        menuIesire.setText("Iesire");
        menuIesire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuIesireActionPerformed(evt);
            }
        });
        jMenu1.add(menuIesire);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Ajutor");

        menuInregistrare.setText("Inregistrare");
        menuInregistrare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuInregistrareActionPerformed(evt);
            }
        });
        jMenu2.add(menuInregistrare);

        menuDespre.setText("Despre");
        menuDespre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDespreActionPerformed(evt);
            }
        });
        jMenu2.add(menuDespre);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(MainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(MainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuDeschidereActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDeschidereActionPerformed
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fl = jfc.getSelectedFile();
        
                incarcModel(fl);
           
      }        // TODO add your handling code here:
    }//GEN-LAST:event_menuDeschidereActionPerformed

    private void btnadaugaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnadaugaActionPerformed
        Dadauga.setVisible(true);
    }//GEN-LAST:event_btnadaugaActionPerformed

    private void btnchildadaugaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnchildadaugaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnchildadaugaActionPerformed

    private void btnanuleazaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnanuleazaActionPerformed
        golesteSuflete();
        if (btnchildadauga.getText().equals("Editeaza")) {
            btnchildadauga.setText("Adauga");
        }
        Dadauga.setVisible(false);
    }//GEN-LAST:event_btnanuleazaActionPerformed

    private void menuSalvareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSalvareActionPerformed
        mSave();
    }//GEN-LAST:event_menuSalvareActionPerformed

    private void menuInregistrareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuInregistrareActionPerformed
        String infop = JOptionPane.showInputDialog("Introduceti codul de inregistrare");
        if (infop == null || !infop.matches(Integer.toString(regc))) {
            
            JOptionPane.showMessageDialog(this, "Codul este invalid!", "Inregistrare esuata", JOptionPane.ERROR_MESSAGE);
        } else {
            int f = Integer.parseInt(infop);
            
            menuInregistrare.setEnabled(false);
            menuDeschidere.setEnabled(true);
            menuSalvare.setEnabled(true);
            ild.setRegStatus(true);
            stat = true;
            JOptionPane.showMessageDialog(this, "Inregistrarea a fost facuta cu succes!", "Confirmare inregistrare", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_menuInregistrareActionPerformed

    private void menuIesireActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuIesireActionPerformed
        iesire();
    }//GEN-LAST:event_menuIesireActionPerformed

    private void bcloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcloseActionPerformed
        dAbout.setVisible(false);
    }//GEN-LAST:event_bcloseActionPerformed

    private void menuDespreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDespreActionPerformed
        dAbout.setLocationRelativeTo(null);
        dAbout.setVisible(true);
    }//GEN-LAST:event_menuDespreActionPerformed

    private void txtfiltruCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtfiltruCaretUpdate
                 
                try {                    
                    int i = cmbtipfiltru.getSelectedIndex();
                    if (i == 0) {
                    String filter = txtfiltru.getText();
                    filterModel((DefaultListModel<Contact>) lstcontacte.getModel(), filter, i);    
                    } else {
                      
                    }
                    
                    
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(agenda.this, e.getMessage(), "UPS EROARE", JOptionPane.ERROR_MESSAGE);
                }
            
    }//GEN-LAST:event_txtfiltruCaretUpdate

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
 /*
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(agenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(agenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(agenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(agenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } */
        //</editor-fold>
        
        showSplash();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new agenda().setVisible(true);
                
            }
        });
    }

    public class HandlerButoane implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == btnchildadauga) {
                try {
                    adauga();
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(agenda.this, e.getMessage(), "UPS EROARE", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (ae.getSource() == btnstergere) {
                try {
                    sterge();
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(agenda.this, e.getMessage(), "UPS EROARE", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (ae.getSource() == btnediteaza) {
                try {
                    Dadauga.setVisible(true);
                    btnchildadauga.setText("Editeaza");
                    editeaza();
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(agenda.this, e.getMessage(), "UPS EROARE", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (ae.getSource() == btnordoneaza) {
                try {
                    ordoneaza();
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(agenda.this, e.getMessage(), "UPS EROARE", JOptionPane.ERROR_MESSAGE);
                }
            }            
            if (ae.getSource() == btnfiltreaza) {
                try {                    
                    int i = cmbtipfiltru.getSelectedIndex();
                    String filter = txtfiltru.getText();
                    filterModel((DefaultListModel<Contact>) lstcontacte.getModel(), filter, i);
                    
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(agenda.this, e.getMessage(), "UPS EROARE", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (ae.getSource() == cmbtipfiltru) {
                try {                    
                    int i = cmbtipfiltru.getSelectedIndex();
                    if (i == 0) {
                        txtfiltru.setEnabled(true);
                    } else {
                        txtfiltru.setEnabled(false);
                    }
                    String filter = txtfiltru.getText();
                    filterModel((DefaultListModel<Contact>) lstcontacte.getModel(), filter, i);
                    
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(agenda.this, e.getMessage(), "UPS EROARE", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            //To change body of generated methods, choose Tools | Templates.
        }
    }

    public static void showSplash() {
        if (SplashScreen.getSplashScreen().getImageURL() != null) {
            try {
                Thread.currentThread().sleep(1000L);
            } catch (Exception e) {
            }
            SplashScreen.getSplashScreen().close();
        }
    }

    public void mSave() {
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            
            File f = jfc.getSelectedFile();
            String nume = f.getName();
            if (nume.indexOf('.') == -1) {
                // nume += ".ab";
                f = new File(f.getParentFile(), nume);
            }
            try {
                try (FileOutputStream fos = new FileOutputStream(f)) {
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(c);
                    oos.close();
                }
                
                ild.setF(f);
                this.setTitle("Agenda Telefonica: " + ild.getF().getName());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Eroare la salvarea datelor!", "EROARE", JOptionPane.ERROR_MESSAGE);
            }
        }
    }    
    
    public void filterModel(DefaultListModel<Contact> model, String filter, int i) {        
        
        switch (i) {
            case 1:
                String f1 = "02";
                String f2 = "03";
                for (Contact s : c.getClst()) {                    
                    if ((s.getNumar().toString().startsWith(f1)) || (s.getNumar().toString().startsWith(f2))) {
                        if (!model.contains(s)) {
                            model.addElement(s);
                        }
                    } else {
                        if (model.contains(s)) {
                            model.removeElement(s);
                        }
                    }
                }
                break;
            
            case 2:
                filter = "07";
                
                for (Contact s : c.getClst()) {                    
                    if ((s.getNumar().toString().startsWith(filter))) {
                        if (!model.contains(s)) {
                            model.addElement(s);
                        }
                    } else {
                        if (model.contains(s)) {
                            model.removeElement(s);
                        }
                    }
                }
                break;            
            case 3:
                LocalDate ld = LocalDate.now();
                
                for (Contact s : c.getClst()) {                    
                    if ((s.getData().getDayOfMonth() == (ld.getDayOfMonth())) && (s.getData().getMonth().equals(ld.getMonth()))) {
                        if (!model.contains(s)) {
                            model.addElement(s);
                        }
                    } else {
                        if (model.contains(s)) {
                            model.removeElement(s);
                        }
                    }
                }
                break;            
            case 4:
                LocalDate ldd = LocalDate.now();
                
                for (Contact s : c.getClst()) {                    
                    if ((s.getData().getDayOfMonth() > (ldd.getDayOfMonth())) && (s.getData().getMonth().equals(ldd.getMonth()))) {
                        if (!model.contains(s)) {
                            model.addElement(s);
                        }
                    } else {
                        if (model.contains(s)) {
                            model.removeElement(s);
                        }
                    }
                }
                break;            
            case 0:
                for (Contact s : c.getClst()) {
                    if (!s.toStringFilter().toLowerCase().contains(filter.toLowerCase())) {
                        if (model.contains(s)) {
                            model.removeElement(s);
                        }
                    } else {
                        if (!model.contains(s)) {
                            model.addElement(s);
                        }
                    }
                }
        }
    }

    public Prop Istatus() {
        try {
            if (salvare.exists()) {
                FileInputStream fis = new FileInputStream(salvare);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Prop ld = ((Prop) ois.readObject());
                ois.close();
                fis.close();
                return ld;
            } else {
                FileOutputStream gos = new FileOutputStream(salvare);
                ObjectOutputStream ros = new ObjectOutputStream(gos);
                ild.setRegStatus(stat);
                ros.writeObject(ild);
                ros.close();
                gos.close();
                return ild;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eroare la incarcarea datelor!", "EROARE", JOptionPane.ERROR_MESSAGE);
            return ild;
        }
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog Dadauga;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JDialog NascutiAstazi;
    private javax.swing.JButton bclose;
    private javax.swing.JButton btnadauga;
    private javax.swing.JButton btnanuleaza;
    private javax.swing.JButton btnchildadauga;
    private javax.swing.JButton btnediteaza;
    private javax.swing.JButton btnfiltreaza;
    private javax.swing.JButton btnordoneaza;
    private javax.swing.JButton btnstergere;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbordonare;
    private javax.swing.JComboBox<String> cmbtipfiltru;
    private javax.swing.JDialog dAbout;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JFileChooser jfc;
    private javax.swing.JLabel limagini;
    private javax.swing.JList<Contact> lstaniversare;
    private javax.swing.JList<Contact> lstcontacte;
    private javax.swing.JMenuItem menuDeschidere;
    private javax.swing.JMenuItem menuDespre;
    private javax.swing.JMenuItem menuIesire;
    private javax.swing.JMenuItem menuInregistrare;
    private javax.swing.JMenuItem menuSalvare;
    private javax.swing.JRadioButton rbmobil;
    private javax.swing.JRadioButton rbtelfix;
    private javax.swing.JTextField txtdata;
    private javax.swing.JTextField txtfiltru;
    private javax.swing.JTextField txtnr;
    private javax.swing.JTextField txtnume;
    private javax.swing.JTextField txtprenume;
    // End of variables declaration//GEN-END:variables
}
