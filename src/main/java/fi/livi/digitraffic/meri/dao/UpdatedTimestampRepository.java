package fi.livi.digitraffic.meri.dao;

import java.time.Instant;
import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.util.dao.SqlRepository;

@Repository
public interface UpdatedTimestampRepository extends SqlRepository {
    enum UpdatedName {
        PORT_CALLS, PORT_METADATA, VESSEL_DETAILS, WINTER_NAVIGATION_PORTS, WINTER_NAVIGATION_SHIPS, WINTER_NAVIGATION_DIRWAYS
    }

    @Query(value = "select cast(updated_time as date) from updated_timestamp where updated_name = :name", nativeQuery = true)
    Instant getLastUpdated(@Param("name") final String name);

    @Modifying
    @Query(value = "insert into updated_timestamp(updated_name, updated_time, updated_by) values(:name, :time, :by)\n" +
        "on conflict (updated_name)\n" +
        "do update set\n" +
        "   updated_time = :time,\n" +
        "   updated_by = :by", nativeQuery = true)
    //@Query(value = "merge into updated_timestamp ut using (select :name as updated_name from dual) utold"
      //      + " on (ut.updated_name = utold.updated_name) "
        //    + " when matched then update set ut.updated_time = :time, ut.updated_by = :by"
          //  + " when not matched then insert(ut.updated_name, ut.updated_time, ut.updated_by) values (:name, :time, :by)", nativeQuery =
        //true)
    void setUpdated(@Param("name")final String name, @Param("time")final Date time, @Param("by")final String by);
}
