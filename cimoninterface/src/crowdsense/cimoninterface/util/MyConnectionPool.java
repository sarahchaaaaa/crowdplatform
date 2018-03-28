package crowdsense.cimoninterface.util;

import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

public class MyConnectionPool {
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static String URL = null;
    public static String USERNAME = null;
    public static String PASSWORD = null;

    private GenericObjectPool connectionPool = null;

    public DataSource setUp() throws Exception {
        // Load JDBC Driver class.
        Class.forName(MyConnectionPool.DRIVER).newInstance();

        // Creates an instance of GenericObjectPool that holds our
        // pool of connections object.
        connectionPool = new GenericObjectPool();
        //connectionPool.setMaxActive(10);
        
        
	    InputStream propertiesInputStream = null;
        Properties properties = new Properties();
        propertiesInputStream = DatabaseUtil.class.getClassLoader().getResourceAsStream("../databaseConfig.properties");
        properties.load(propertiesInputStream);
		URL=properties.getProperty("host")+"/"+properties.getProperty("database");
		USERNAME =properties.getProperty("username");
		PASSWORD = properties.getProperty("password");
       

        // Creates a connection factory object which will be use by
        // the pool to create the connection object. We passes the
        // JDBC url info, username and password.
        ConnectionFactory cf = new DriverManagerConnectionFactory(
                MyConnectionPool.URL,
                MyConnectionPool.USERNAME,
                MyConnectionPool.PASSWORD);

        // Creates a PoolableConnectionFactory that will wraps the
        // connection object created by the ConnectionFactory to add
        // object pooling functionality.
        PoolableConnectionFactory pcf =
                new PoolableConnectionFactory(cf, connectionPool,
                        null, null, false, true);
        return new PoolingDataSource(connectionPool);
    }

    public GenericObjectPool getConnectionPool() {
        return connectionPool;
    }

    /*
    public static void main(String[] args) throws Exception {
        MyConnectionPool demo = new MyConnectionPool();
        DataSource dataSource = demo.setUp();
        demo.printStatus();

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = dataSource.getConnection();
            demo.printStatus();

            stmt = conn.prepareStatement("SELECT * FROM m_users");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Username: " + rs.getString("username"));
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        demo.printStatus();
    }
	*/
    
    /**
     * Prints connection pool status.
     */
    public void printStatus() {
        System.out.println("Max   : " + getConnectionPool().getMaxActive() + "; " +
                "Active: " + getConnectionPool().getNumActive() + "; " +
                "Idle  : " + getConnectionPool().getNumIdle());
    }
}