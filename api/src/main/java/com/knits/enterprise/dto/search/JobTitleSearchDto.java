package com.knits.enterprise.dto.search;

import com.knits.enterprise.model.company.JobTitle;
import com.knits.enterprise.model.security.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

        if (StringUtils.isNotEmpty(name)) {
            log.debug("Filtering on jobTitle name: {}", name);
            String namePattern = "%" + name.toLowerCase() + "%";
            Expression<String> nameLower = criteriaBuilder.lower(root.get("name"));
            Predicate nameAsPredicate = criteriaBuilder.equal(nameLower, namePattern);
            filters.add(nameAsPredicate);
        }

        if (StringUtils.isNotEmpty(createdBy)) {
            log.debug("Filtering on creator's userName: {}", createdBy);
            String namePattern = "%" + createdBy.toLowerCase() + "%";
            Join<JobTitle, User> userJoin = root.join("createdBy");
            Expression<String> createdByLower = criteriaBuilder.lower(userJoin.get("createdBy"));
            Predicate createdByPredicate = criteriaBuilder.equal(createdByLower, namePattern);
            filters.add(createdByPredicate);
        }

        if (StringUtils.isNotEmpty(creationDateFrom)) {
            log.debug("Filtering on jobTitle's creation Date from: {}", creationDateFrom);
            LocalDateTime formattedDateTime = LocalDate.parse(creationDateFrom, DATE_FORMATTER).atStartOfDay();
            Predicate creationDateFromPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), formattedDateTime);
            filters.add(creationDateFromPredicate);
        }

        if (StringUtils.isNotEmpty(creationDateTo)) {
            log.debug("Filtering on jobTitle's creation Date to: {}", creationDateTo);
            LocalDateTime formattedDateTime = LocalDate.parse(creationDateTo, DATE_FORMATTER).atStartOfDay();
            Predicate creationDateToPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), formattedDateTime);
            filters.add(creationDateToPredicate);
        }
        if (StringUtils.isNotEmpty(creationDateFrom) && StringUtils.isNotEmpty(creationDateTo)) {
            log.debug("Filtering on jobTitle's creation Date to: {} and creation Date from: {}", creationDateTo, creationDateFrom);
            LocalDateTime formattedDateFromTime = LocalDate.parse(creationDateFrom, DATE_FORMATTER).atStartOfDay();
            LocalDateTime formattedDateToTime = LocalDate.parse(creationDateTo, DATE_FORMATTER).atStartOfDay();
            Predicate creationDateBetweenPredicate = criteriaBuilder.between(root.get("startDate"), formattedDateFromTime, formattedDateToTime);
            filters.add(creationDateBetweenPredicate);
        }
    }
}

