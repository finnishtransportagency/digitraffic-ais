package fi.livi.digitraffic.meri;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDatabaseTestBase extends AbstractTestBase {

    @Autowired
    protected ObjectMother om;

    @Before
    public void before() {
        om.truncateDatabase();
    }

    @After
    public void after() {
        om.truncateDatabase();
    }

}
