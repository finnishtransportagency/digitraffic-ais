package fi.livi.digitraffic.meri.dao.bridgesluice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.domain.bridgesluice.BridgeSluiceDisruption;

@Repository
public interface BridgeSluiceDisruptionRepository extends JpaRepository<BridgeSluiceDisruption, Long> {
}
