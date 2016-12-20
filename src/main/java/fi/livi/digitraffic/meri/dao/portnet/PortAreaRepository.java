package fi.livi.digitraffic.meri.dao.portnet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.portnet.PortArea;
import fi.livi.digitraffic.meri.domain.portnet.PortAreaKey;
import fi.livi.digitraffic.meri.model.portnet.metadata.PortAreaJson;

@Repository
public interface PortAreaRepository extends JpaRepository<PortArea, PortAreaKey> {
    List<PortAreaJson> findAllProjectedBy();

    List<PortAreaJson> findByPortAreaKeyLocode(final String locode);
}