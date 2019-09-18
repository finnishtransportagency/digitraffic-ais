CREATE TABLE BRIDGELOCK_DISRUPTION(
    bridgelock_id BIGINT PRIMARY KEY NOT NULL,
    bridgelock_type_id BIGINT NOT NULL,
    start_date TIMESTAMP(0) WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP(0) WITH TIME ZONE NOT NULL,
    geometry GEOMETRY, -- requires PostGIS
    description_fi TEXT,
    description_sv TEXT,
    description_en TEXT,
    additional_info_fi TEXT,
    additional_info_sv TEXT,
    additional_info_en TEXT
);
