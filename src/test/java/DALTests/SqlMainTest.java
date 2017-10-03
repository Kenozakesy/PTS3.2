package DALTests;

import DAL.SqlMain;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import static org.junit.Assert.*;

public class SqlMainTest {
    private String userId;
    private String password;
    private String url;
    private Connection connectie;
    private Properties properties;
    private SqlMain sqlMain;
    @Before
    public void setUp() throws Exception {
        userId = "dbi299244";
        password = "PTS3Groep1";
        url = "Server=mssql.fhict.local;Database=dbi299244;";
        properties = new Properties();
        properties.setProperty(userId, password);
        connectie = DriverManager.getConnection(url, properties);
        sqlMain = new SqlMain();
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void getConnection() throws Exception {
    }

    @Test
    public void setConnection() throws Exception {
    }

    @Test
    public void getStatement() throws Exception {
    }

    @Test
    public void setStatement() throws Exception {
    }

    @Test
    public void getResult() throws Exception {
    }

    @Test
    public void setResult() throws Exception {
    }

    @Test
    public void closeAll() throws Exception {
    }

}