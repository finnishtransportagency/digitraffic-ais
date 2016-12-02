package fi.livi.digitraffic.meri.dao;

import java.sql.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.AbstractIntegrationTest;

public class UpdatedTimestampRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private UpdatedTimestampRepository updatedTimestampRepository;

    @Test
    @Transactional
    public void testUpdate() {
        updatedTimestampRepository.setUpdated("test", new Date(12345), "testi");
    }
}
