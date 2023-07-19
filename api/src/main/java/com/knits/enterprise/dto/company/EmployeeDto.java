package com.knits.enterprise.dto.company;

import com.knits.enterprise.dto.common.OrganizationDto;

import com.knits.enterprise.model.enums.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder=true)
public class EmployeeDto extends AbstractActiveDto {

    private Long id;
    @NotNull()
    private String firstName;
    @NotNull()
    private String lastName;

    private String email;

    @NotNull()
    private String birthDate;

    @NotNull()
    private Gender gender;

    @NotNull()
    private String startDate;

    @NotNull()
    private String endDate;

    @NotNull()
    private String companyPhone;

    @NotNull()
    private String companyMobileNumber;

    @NotNull()
    private String role;

    @NotNull()
    private BusinessUnitDto businessUnit;

    @NotNull()
    private OrganizationDto organization;

    @NotNull()
    private OfficeDto office;

    @NotNull()
    private JobTitleDto jobTitle;

    @NotNull()
    private DepartmentDto department;

    @NotNull()
    private DivisionDto division;

    @NotNull()
    private EmployeeDto solidLineManager;
}

