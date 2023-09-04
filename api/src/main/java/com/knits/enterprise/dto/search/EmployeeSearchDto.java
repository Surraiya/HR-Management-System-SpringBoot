package com.knits.enterprise.dto.search;

import com.knits.enterprise.config.Constants;
import com.knits.enterprise.model.company.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class EmployeeSearchDto extends GenericSearchDto<Employee>{

    private String firstName;
    private String lastName;
    private String hireDateFrom;
    private String hireDateTo;
    private Long businessUnitId;
    private Long departmentId;
    private Long jobTitleId;
    private Long locationId;
    private Long countryId;

    protected void addFilters(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, List<Predicate> filters) {

        if (firstName != null) {
            log.debug("Filtering on Employee first name: {}", firstName);
            filters.add(criteriaBuilder.equal((criteriaBuilder.lower(root.get("firstName"))),firstName)
            );
        }

        if (lastName != null) {
            log.debug("Filtering on Employee last name: {}", lastName);
            filters.add(criteriaBuilder.equal((criteriaBuilder.lower(root.get("lastName"))),lastName)
            );
        }

        if (hireDateFrom != null) {
            log.debug("Filtering on Employee's start date: {}", hireDateFrom);
            LocalDate from = LocalDate.parse(hireDateFrom, Constants.DATE_FORMATTER);
            filters.add(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), from)
            );
        }

        if (hireDateTo != null) {
            log.debug("Filtering on Employee's end date: {}", hireDateTo);
            LocalDate to = LocalDate.parse(hireDateTo, Constants.DATE_FORMATTER);
            filters.add(
                    criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), to)
            );
        }

        if (businessUnitId != null) {
            log.debug("Filtering on Employee's business unit: {}", businessUnitId);
            filters.add(
                    criteriaBuilder.equal(root.get("businessUnit").get("id"), businessUnitId)
            );
        }

        if (departmentId != null) {
            log.debug("Filtering on Employee's department: {}", departmentId);
            filters.add(
                    criteriaBuilder.equal(root.get("department").get("id"), departmentId)
            );
        }

        if (jobTitleId != null) {
            log.debug("Filtering on Employee's jobTitle: {}", jobTitleId);
            filters.add(
                    criteriaBuilder.equal(root.get("jobTitle").get("id"), jobTitleId)
            );
        }

        if (locationId != null) {
            log.debug("Filtering on Employee's office location: {}", locationId);
            filters.add(
                    criteriaBuilder.equal(root.get("office").get("id"), locationId)
            );
        }

        if (countryId != null) {
            log.debug("Filtering on Employee's office(country): {}", countryId);
            filters.add(
                    criteriaBuilder.equal(root.get("office").get("address").get("country").get("id"), countryId)
            );
        }
    }
}
