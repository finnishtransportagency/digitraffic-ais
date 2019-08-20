package fi.livi.digitraffic.meri.service.bridgesluice;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.domain.bridgesluice.BridgeSluiceDisruption;
import fi.livi.digitraffic.meri.dao.bridgesluice.BridgeSluiceDisruptionRepository;

@Service
public class BridgeSluiceDisruptionService {

    private final BridgeSluiceDisruptionRepository repository;

    public BridgeSluiceDisruptionService(BridgeSluiceDisruptionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<BridgeSluiceDisruption> findAll() {
        return repository.findAll();
    }

}
