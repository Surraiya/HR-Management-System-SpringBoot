package com.knits.enterprise.repository.company;

import com.knits.enterprise.model.company.JobTitle;
import com.knits.enterprise.repository.common.ActiveEntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface JobTitleRepository extends ActiveEntityRepository<JobTitle> {

    @Query("select j from JobTitle  j  where j.id IN (:ids) AND j.active = true")
    Set<JobTitle> findAllById(@Param("ids") Set<Long> ids);

}
