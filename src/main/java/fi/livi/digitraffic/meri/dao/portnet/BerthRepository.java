package fi.livi.digitraffic.meri.dao.portnet;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.portnet.Berth;
import fi.livi.digitraffic.meri.domain.portnet.BerthKey;

@Repository
@ConditionalOnWebApplication
public interface BerthRepository extends JpaRepository<Berth, BerthKey> {
    List<Berth> findByBerthKeyLocode(final String locode);
}
