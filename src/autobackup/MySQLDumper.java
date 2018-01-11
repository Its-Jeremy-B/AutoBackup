package autobackup;
import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class MySQLDumper {
	Scanner in = new Scanner(System.in);
	String HOST = "";
	String USER = "";
	String PASS = "";
	String BACKUPLOC = "";
	
	String IGNORESCHEMAS[] = {"information_schema", "mysql", "performance_schema", "phpmyadmin", "test"};
	ArrayList<String> SCHEMAS = new ArrayList<String>();
	
	Connection conn;
	ResultSet rS;
	
	public MySQLDumper(String host, String user, String pass, String backupLocation){
		HOST = host;
		USER = user;
		PASS = pass;
		BACKUPLOC = backupLocation;
	}
	
	public void startDump(){
		try{
			mySQLconnect();
			
			executeQuery("SHOW DATABASES");
			
			while (rS.next()) {
				SCHEMAS.add(rS.getString(1));
			}
			
			for(int i = 0; i < SCHEMAS.size(); i++){
				if(isSchemaValid(SCHEMAS.get(i))){
					backupSchema(SCHEMAS.get(i));
				}
			}
			
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void mySQLconnect(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://"+HOST, USER, PASS);
			System.out.println("Connected.");
		} catch (Exception e) {
			new MessageBox("Sorry, but your connection to your MySQL database failed.\nPlease check your MySQL credentials and try again.");
		}
	}
	
	private void executeQuery(String query){
		// Statements allow to issue SQL queries to the database
		Statement statement;
		try {
			statement = conn.createStatement();
		  // Result set get the result of the SQL query
		  rS = statement.executeQuery(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void backupSchema(String db){
		try {
            G.CreateDirectory(BACKUPLOC);
			File f = new File(BACKUPLOC+G.slash+db+".sql");
			PrintWriter out = new PrintWriter(f);
			System.out.println("Backing up DataBase: "+db);
			ArrayList<String> TABLES = new ArrayList<String>();

			executeQuery("SHOW TABLES FROM "+db);
			
			while(rS.next()){
				TABLES.add(rS.getString(1));
			}
			
			executeQuery("USE "+db);
			
			for(int i = 0; i < TABLES.size(); i++){
				out.print("CREATE TABLE "+TABLES.get(i)+" (");
				executeQuery("SELECT * FROM "+TABLES.get(i));
				
				int cols = rS.getMetaData().getColumnCount();
				//Printing all data
				for(int x = 0; x < cols; x++){
					out.print(rS.getMetaData().getColumnName(x+1));
					String name = rS.getMetaData().getColumnTypeName(x+1);
					if(name.equalsIgnoreCase("VARCHAR")){
						name = "TEXT";
					}
					int size = rS.getMetaData().getColumnDisplaySize(x+1);
					int maxSize = 3000;
					if(size > maxSize){
						size = maxSize;
					}
					out.print(" "+name);
					if(!name.equalsIgnoreCase("TEXT")){
						out.print("("+size+")");
					}
					if(x < cols-1){
						out.print(", ");
					}
				}
				out.print(");\n");
				
				while(rS.next()){
					out.print("INSERT INTO "+rS.getMetaData().getTableName(1)+" VALUES (");
					for(int x = 0; x < cols; x++){
						String data = rS.getString(x+1);
						if(data != null && data.contains("'")){
							data = data.replaceAll("'", "\\\\'");
							G.Print("ESCAPED STRING: "+data);
						}
						out.print("'"+data);
						if(x < cols-1){
							out.print("', ");
						}else{
							out.print("'");
						}
					}
					out.print(");\n");
				}
			}
			out.close();
			System.out.println("Finished backing up DataBase: "+db);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean isSchemaValid(String str){
		for(int i = 0; i < IGNORESCHEMAS.length; i++){
			if(str.equalsIgnoreCase(IGNORESCHEMAS[i])){
				return false;
			}
		}
		return true;
	}
}
