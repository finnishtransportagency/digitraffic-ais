package fi.livi.digitraffic.meri;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractDatabaseTestBase extends AbstractTestBase {

    @Autowired
    protected EntityFactory ef;

    @Before
    public void before() {
        ef.truncateDatabase();
    }

    @After
    public void after() {
        ef.truncateDatabase();
    }

}
