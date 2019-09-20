CREATE TABLE BRIDGELOCK_DISRUPTION(
    bridgelock_id BIGINT,
    bridgelock_type_id BIGINT,
    start_date TIMESTAMP(0) WITH TIME ZONE,
    end_date TIMESTAMP(0) WITH TIME ZONE NOT NULL,
    geometry GEOMETRY, -- requires PostGIS
    description_fi TEXT,
    description_sv TEXT,
    description_en TEXT,
    additional_info_fi TEXT,
    additional_info_sv TEXT,
    additional_info_en TEXT,
    PRIMARY KEY (bridgelock_id, bridgelock_type_id, start_date)
);
