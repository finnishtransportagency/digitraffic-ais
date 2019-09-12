package fi.livi.digitraffic.meri.service.bridgelock;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.domain.bridgelock.BridgeLockDisruption;
import fi.livi.digitraffic.meri.dao.bridgelock.BridgeLockDisruptionRepository;

@Service
public class BridgeLockDisruptionService {

    private final BridgeLockDisruptionRepository repository;

    public BridgeLockDisruptionService(BridgeLockDisruptionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<BridgeLockDisruption> findAll() {
        return repository.findAll();
    }

}
