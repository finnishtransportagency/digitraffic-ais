package fi.livi.digitraffic.meri.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.VesselMetadataRepository;
import fi.livi.digitraffic.meri.domain.VesselMetadata;

@Service
@Transactional(readOnly = true)
public class VesselMetadataService {
    private final VesselMetadataRepository vesselMetadataRepository;

    @Autowired
    public VesselMetadataService(final VesselMetadataRepository vesselMetadataRepository) {
        this.vesselMetadataRepository = vesselMetadataRepository;
    }

    public VesselMetadata findMetadataByMssi(final int mmsi) {
        return vesselMetadataRepository.findOne(mmsi);
    }
    
    public List<VesselMetadata> listAllVesselMetadata() {
        return vesselMetadataRepository.findAll();
    }
}
