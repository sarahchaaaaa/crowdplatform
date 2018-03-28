package commons;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.sql.DataSource;

public class DatabaseUtil {
    public static final String DRIVER = "com.mysql.jdbc.Driver";
	private static MyConnectionPool connectionPool = null;

	private static DataSource dataSource = null;
	
	static{
	    try {
			Class.forName(MyConnectionPool.DRIVER).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	

	/*
	public static void loadDriver() {
    	try {
    	    System.out.println("Loading driver...");
    	    Class.forName("com.mysql.jdbc.Driver");
    	    System.out.println("Driver loaded!");
    	} catch (ClassNotFoundException e) {
    	    throw new RuntimeException("Cannot find the driver in the classpath!", e);
    	}
	}
	*/
	
	public static void initializeDataConnection() {
		try {
			if(connectionPool == null){
				connectionPool = new MyConnectionPool();
			    if(dataSource == null){
				    dataSource = connectionPool.setUp();
				    connectionPool.printStatus();		    	
			    }
			}
		
		} catch (Exception e) {
			// TODO: handle exception
		}		
	}
	
	public static Connection connectToDatabase() {
		String dbUrl, username, password;
		Connection connection = null;
		try {
			/**System.out.println(new File(".").getAbsolutePath());
			File file = new File("WEB-INF/databaseConfig.properties");
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();

			Enumeration enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				String value = properties.getProperty(key);
				System.out.println(key + ": " + value);
			}
			**/
			
			
			
		    InputStream propertiesInputStream = null;
            Properties properties = new Properties();
            propertiesInputStream = DatabaseUtil.class.getClassLoader().getResourceAsStream("../system.properties");
            properties.load(propertiesInputStream);
			dbUrl=properties.getProperty("db_host")+"/"+properties.getProperty("db_schema");
			username =properties.getProperty("db_username");
			password = properties.getProperty("db_password");
			
			System.out.println("Connecting database..., url:"+ dbUrl + ", username:"+ username+", password:"+ password);
            connection = DriverManager.getConnection(dbUrl,username,password);
            
			
			
			/*
			connection = dataSource.getConnection();
			connectionPool.printStatus();
			*/
			
		} catch(Exception e ){
			e.printStackTrace();
		}
		
		return connection;
	}

}
