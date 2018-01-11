/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autobackup;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.UIManager;

/**
 *
 * @author JBlevins
 */
public class AutoBackup_Main extends javax.swing.JFrame {

    /**
     * Creates new form AutoBackup_Main
     */
    public static AutoBackup_Main form;
    
    public AutoBackup_Main() {
        form = this;
        try { 
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); 
        } catch (Exception ex) { 
            ex.printStackTrace(); 
        }
        initComponents();
        this.setLocationRelativeTo(null);
        
        //If is a new install - Set lastBackup if not set before
        if(INIReader.getSetting("lastBackup").equals("")){
            INIReader.saveSettings("");
            INIReader.setSetting("lastBackup", G.getTime()+"");
            INIReader.setSetting("minimizeToTray", "True");
            INIReader.setSetting("backupInterval", "24");
        }
        
        //Set minimize to tray setting if not set yet
        if(INIReader.getSetting("minimizeToTray").equals("")){
            INIReader.setSetting("minimizeToTray", "True");
        }
        
        //Set checkBox value based on settings
        if(INIReader.getSetting("minimizeToTray").equals("True")){
            jCheckBox1.setSelected(true);
        }else{
            jCheckBox1.setSelected(false);
        }
        
        //Start thrad for automatic backups
        new Thread(){
            public void run(){
                while(true){
                    System.out.println("Looping");
                    
                    //Update Jlabel with time until next backup
                    if(((Long.parseLong(INIReader.getSetting("lastBackup")) / 60) + (G.getBackupInterval() * 60)) - (G.getTime()/60) <= 0 && !INIReader.getSetting("websiteName").equals("")){
                        jLabel1.setText("Backup is currently in progress...");
                    }else{
                        jLabel1.setText("Next backup starts in " + (((Long.parseLong(INIReader.getSetting("lastBackup")) / 60) + (G.getBackupInterval() * 60)) - (G.getTime()/60)) + " minutes.");
                    }
                    
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    
                    //Check if backup is ready
                    if(((Long.parseLong(INIReader.getSetting("lastBackup")) / 60) + (G.getBackupInterval() * 60)) - (G.getTime()/60) <= 0 && !INIReader.getSetting("websiteName").equals("")){
                        System.out.println("Backing up");
                        //Check if more than one website is being backed up
                        String[] websiteNames = new String[0];
                        boolean oneWebsite = false;
                        
                        try{
                            websiteNames = INIReader.getSetting("websiteName").split(", ");
                        }catch(Exception e){
                            //If there is only one website
                            oneWebsite = true;
                            new WebsiteDumper(INIReader.getSetting("websiteName"), INIReader.getSetting("ftpUsername"), INIReader.getSetting("ftpPassword"), INIReader.getSetting("sqlUsername"), INIReader.getSetting("sqlPassword")).start();
                        }
                        
                        String[] ftpUsernames = INIReader.getSetting("ftpUsername").split("/::/");
                        String[] ftpPasswords = INIReader.getSetting("ftpPassword").split("/::/");
                        String[] sqlUsernames = INIReader.getSetting("sqlUsername").split("/::/");
                        String[] sqlPasswords = INIReader.getSetting("sqlPassword").split("/::/");
                        
                        //If there is more than one website
                        for(int i = 0; i < websiteNames.length && !oneWebsite; i++){
                            new WebsiteDumper(websiteNames[i], ftpUsernames[i], ftpPasswords[i], sqlUsernames[i], sqlPasswords[i]).start();
                        }
                    }
                    
                    //Wait 5 seconds before checking if backups are ready
                    try{
                        Thread.sleep(5000);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AutoBackup");
        setResizable(false);

        jButton1.setText("Open Settings");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Minimize to app tray.");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Next backup starts in");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addGap(0, 194, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if(jCheckBox1.isSelected()){
            INIReader.setSetting("minimizeToTray", "True");
        }else{
            INIReader.setSetting("minimizeToTray", "False");
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Settings settings = new Settings();
        settings.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AutoBackup_Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
