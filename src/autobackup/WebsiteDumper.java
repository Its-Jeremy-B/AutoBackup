
package autobackup;


public class WebsiteDumper {
    public String host, ftpUsername, ftpPassword, sqlUsername, sqlPassword;
    
    public WebsiteDumper(String host, String ftpUsername, String ftpPassword, String sqlUsername, String sqlPassword){
        this.host = host;
        this.ftpUsername = ftpUsername;
        this.ftpPassword = ftpPassword;
        this.sqlUsername = sqlUsername;
        this.sqlPassword = sqlPassword;
    }
    
    public void start(){
        //Download SQL databases
        MySQLDumper sqldumper = new MySQLDumper(host, sqlUsername, sqlPassword, INIReader.getSetting("backupLocation")+G.slash+host);
        sqldumper.startDump();

        //Download FTP files
        PHPDumper phpdumper = new PHPDumper(host, ftpUsername, ftpPassword, INIReader.getSetting("backupLocation")+G.slash+host);
        phpdumper.startDump();

        //Set last backup
        INIReader.setSetting("lastBackup", G.getTime()+"");
    }
}
