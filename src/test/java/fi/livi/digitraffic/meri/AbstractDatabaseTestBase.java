package fi.livi.digitraffic.meri;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDatabaseTestBase extends AbstractTestBase {

    @Autowired
    protected ObjectMother objectMother;

    @Before
    public void before() {
        objectMother.truncateDatabase();
    }

    @After
    public void after() {
        objectMother.truncateDatabase();
    }

}
