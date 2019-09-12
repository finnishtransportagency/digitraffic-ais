package fi.livi.digitraffic.meri.dao.bridgelock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.bridgelock.BridgeLockDisruption;

@Repository
public interface BridgeLockDisruptionRepository extends JpaRepository<BridgeLockDisruption, Long> {
}
