
package autobackup;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class G {
	//Important Variables
	static String OS = System.getProperty("os.name");
	static String slash = "/";
	static String startDate;
	static long startTime = 0;
	static String osName = System.getProperty("os.name");
	
	//Variables
	static boolean SettingValueFound = false;
	static int currentNameSize = 0;
	static int x = 0;
	
	
	public static String date(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public static long getTime(){
		return System.currentTimeMillis()/1000;
	}
        
        public static long getBackupInterval(){
            return (Long.parseLong(INIReader.getSetting("backupInterval")));
        }
	
	public static int getTimeUntilDate(long dateInMillis){
		return (int)(((startTime+dateInMillis)-getTime())/60);
	}
	
	public static void Print(String str){
		System.out.println(str);
	}
	
	public static void CreateDirectory(String str){
			boolean success = (new File(str)).mkdirs();
			if (!success) {
			    Print("Folder "+str+" could not be created.");
			}
	}
	
	public static String getUser(){
		return System.getProperty("user.name");
	}
	
	public static String getHost(String str){
		return str.replaceAll(" ", "").replaceAll("http", "").replaceAll("https", "").replaceAll("://", "");
	}
}
