/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autobackup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 *
 * @author JBlevins
 */
public class INIReader {
    static String fileLocation = "./Settings.ini";
    
    public static String getSetting(String key){
        try{
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileLocation);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                if(line.startsWith(key+"::")){
                    return line.split("::")[1];
                }
            }   

            // Always close files.
            bufferedReader.close();  
        }catch(Exception e){
            System.out.println("Error found, returning blank string");
        }
        return "";
    }
    
    public static void setSetting(String key, String value){
        try{
            //String to contain settings file contents
            String settingsFile = "";
            
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileLocation);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            String line = null;
            boolean keyFound = false;
            while((line = bufferedReader.readLine()) != null) {
                if(line.startsWith(key+"::")){
                    keyFound = true;
                    settingsFile += key+"::"+value + "\r\n";
                }else{
                    settingsFile += line + "\r\n";
                }
            }   
            
            //If the key was not found during the reading, add key to file
            if(!keyFound){
                settingsFile += key+"::"+value + "\r\n";
            }

            // Always close files.
            bufferedReader.close();
            saveSettings(settingsFile);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    static void saveSettings(String str){
        try{
            PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");
            writer.print(str);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
