
package autobackup;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class PHPDumper {
	String HOST = "";
	String USER = "";
	String PASS = "";
	String BACKUPLOC = "";
	
	String IGNOREFOLDERS[] = {"mail", "etc", "logs"};
	
	public PHPDumper(String host, String user, String pass, String backupLocation){
		HOST = host;
		USER = user;
		PASS = pass;
		BACKUPLOC = backupLocation;
	}
	
	public void startDump(){
		//Download directories via FTP 
	    FTPClient ftpClient = new FTPClient();
	    
	    try {
	        ftpClient.connect(HOST, 21);
	        boolean success = ftpClient.login(USER, PASS);
	        
	        if(!success){        		
                    new MessageBox("Sorry, but your connection to your FTP server failed.\nPlease check your FTP credentials and try again.");
	        }
	        
	        ftpClient.enterLocalPassiveMode();
	        ftpClient.setControlKeepAliveTimeout(300);

	        String remoteDirPath = "/";

	        downloadDirectory(ftpClient, remoteDirPath, "", BACKUPLOC);
	        
	        ftpClient.logout();
	        ftpClient.disconnect();

	        System.out.println("Disconnected");
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	}
	

    
    public void downloadDirectory(FTPClient ftpClient, String parentDir, String currentDir, String saveDir) throws IOException{
	    String dirToList = parentDir;
	    if (!currentDir.equals("")) {
	        dirToList += G.slash + currentDir;
	    }
	 
	    FTPFile[] subFiles = ftpClient.listFiles(dirToList);
	 
	    if (subFiles != null && subFiles.length > 0) {
	    	if(arrayContains(subFiles, "public_html")){
	    		G.Print("Set location to public_html");
	            String newDirPath = saveDir + parentDir + G.slash + currentDir + G.slash + "public_html";
	            if (currentDir.equals("")) {
	                newDirPath = saveDir + parentDir + G.slash + "public_html";
	            }
	            G.CreateDirectory(newDirPath);
	    		downloadDirectory(ftpClient, dirToList, "public_html", saveDir);
	    	}else{
		        for (FTPFile aFile : subFiles) {
		            String currentFileName = aFile.getName();
		            if (currentFileName.equals(".") || currentFileName.equals("..")) {
		                // skip parent directory and the directory itself
		                continue;
		            }
		            String filePath = parentDir + G.slash + currentDir + G.slash + currentFileName;
		            if (currentDir.equals("")) {
		                filePath = parentDir + G.slash + currentFileName;
		            }
		 
		            String newDirPath = saveDir + parentDir + G.slash + currentDir + G.slash + currentFileName;
		            if (currentDir.equals("")) {
		                newDirPath = saveDir + parentDir + G.slash + currentFileName;
		            }
		 
		            if (aFile.isDirectory()) {
		        		if(!isFolderValid(currentFileName) || currentFileName.indexOf('.') == 0){
		        			System.out.println("Return: "+currentFileName);
		        			continue;
		        		}
		                // create the directory in saveDir
		                File newDir = new File(newDirPath);
		                boolean created = newDir.mkdirs();
		                if (created) {
		                    System.out.println("CREATED the directory: " + newDirPath);
		                } else {
		                    System.out.println("COULD NOT create the directory: " + newDirPath);
		                }
		 
		                // download the sub directory
		                downloadDirectory(ftpClient, dirToList, currentFileName, saveDir);
		            } else {
		                // download the file
		                boolean success = downloadSingleFile(ftpClient, filePath, newDirPath);
		                if (success) {
		                    System.out.println("DOWNLOADED the file: " + filePath);
		                } else {
		                    System.out.println("COULD NOT download the file: " + filePath);
		                }
		            }
		        }
	    	}
	    }
	}
	
	public boolean downloadSingleFile(FTPClient ftpClient, String remoteFilePath, String savePath) throws IOException{
	    File downloadFile = new File(savePath);
	     
	    File parentDir = downloadFile.getParentFile();
	    if (!parentDir.exists()) {
	        parentDir.mkdir();
	    }
	         
	    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
	    try {
	        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	        return ftpClient.retrieveFile(remoteFilePath, outputStream);
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (outputStream != null) {
	            outputStream.close();
	        }
	    }
	}
	
	private boolean isFolderValid(String str){
		for(int i = 0; i < IGNOREFOLDERS.length; i++){
			if(str.equalsIgnoreCase(IGNOREFOLDERS[i])){
				return false;
			}
		}
		return true;
	}
	
	private boolean arrayContains(FTPFile str[], String con){
		for(int i = 0; i < str.length; i++){
			if(str[i].getName().equalsIgnoreCase(con)){
				return true;
			}
		}
		return false;
	}
}
