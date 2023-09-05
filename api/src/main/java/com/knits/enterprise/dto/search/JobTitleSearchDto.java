package com.knits.enterprise.dto.search;

import com.knits.enterprise.model.company.JobTitle;
import com.knits.enterprise.model.security.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.List;

import static com.knits.enterprise.config.Constants.DATE_FORMATTER;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class JobTitleSearchDto extends GenericSearchDto<JobTitle>{

    private String name;
    private String createdBy;
    private String creationDateFrom;
    private String creationDateTo;

    protected void addFilters(Root<JobTitle> root,
                              CriteriaQuery<?> query,
                              CriteriaBuilder criteriaBuilder,
                              List<Predicate> filters){
        log.debug("Entering addFilters");

        if (name != null) {
            log.debug("Filtering on jobTitle name: {}", name);
            filters.add(
                    criteriaBuilder.equal(criteriaBuilder.lower(root.get("name")), name.toLowerCase())
            );
        }

        if (createdBy != null) {
            log.debug("Filtering on creator's userName: {}", createdBy);
            Join<JobTitle, User> userJoin = root.join("createdBy");
            filters.add(
                    criteriaBuilder.equal(criteriaBuilder.lower(userJoin.get("createdBy")), createdBy.toLowerCase())
            );
        }

        if (creationDateFrom != null) {
            log.debug("Filtering on jobTitle's creation Date from: {}", creationDateFrom);
            filters.add(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), LocalDate.parse(creationDateFrom, DATE_FORMATTER))
            );
        }

        if (creationDateTo != null) {
            log.debug("Filtering on jobTitle's creation Date to: {}", creationDateTo);
            filters.add(
                    criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), LocalDate.parse(creationDateTo, DATE_FORMATTER))
            );
        }

        if (creationDateFrom != null && creationDateTo != null) {
            log.debug("Filtering on jobTitle's creation Date to: {} and creation Date from: {}", creationDateTo, creationDateFrom);
            filters.add(
                    criteriaBuilder.between(
                            root.get("startDate"),
                            LocalDate.parse(creationDateFrom, DATE_FORMATTER),
                            LocalDate.parse(creationDateTo, DATE_FORMATTER)
                    )
            );
        }
    }
}

