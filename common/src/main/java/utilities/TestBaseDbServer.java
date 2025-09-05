package utilities;

import db.DbClient;
import db.DbConfig;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class TestBaseDbServer {
    protected DbClient db;

    @BeforeClass
    public void init() {
        String env = System.getProperty("env", "stg"); // align with your EnvironmentType
        db = new DbClient(DbConfig.load(env));
    }

    @AfterClass
    public void tearDown(){ db.close(); }

}
