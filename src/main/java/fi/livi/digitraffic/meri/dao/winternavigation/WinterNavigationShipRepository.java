package fi.livi.digitraffic.meri.dao.winternavigation;

import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;

import java.util.stream.Stream;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationShip;

public interface WinterNavigationShipRepository extends JpaRepository<WinterNavigationShip, String> {
    @QueryHints({ @QueryHint(name = HINT_FETCH_SIZE, value = "300") })
    @EntityGraph(attributePaths = { "shipState", "shipVoyage", "shipActivities", "shipPlannedActivities" })
    Stream<WinterNavigationShip> findDistinctByOrderByVesselPK();
}
