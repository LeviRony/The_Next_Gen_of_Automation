package utilities;

import db.DbClient;
import db.DbConfig;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class TestBaseDbServer {
    protected DbClient db;

    @BeforeClass
    public void init() {
        // Loading the properties file from common/src/main/resources/qa.properties
        String env = System.getProperty("env", "qa");
        db = new DbClient(DbConfig.load(env));
    }

    @AfterClass
    public void tearDown(){ db.close(); }

}
