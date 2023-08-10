package com.knits.enterprise.dto.company;

import com.knits.enterprise.dto.security.UserDto;
import com.knits.enterprise.validations.OnCreate;
import com.knits.enterprise.validations.OnUpdate;
import com.knits.enterprise.validations.UniqueJobTitleName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder=true)
public class AbstractOrganizationStructureDto extends AbstractActiveDto {

    @NotNull(groups = {OnUpdate.class}, message = "id is mandatory for update")
    @Null(groups = {OnCreate.class}, message = "id is not allowed for create")
    private Long id;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Invalid Name: Name is required")
    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Invalid Name: Empty Name")
    @Size(groups = {OnCreate.class, OnUpdate.class}, min = 3, max = 60, message = "Invalid Name: Must be of 3 - 60 characters")
    @UniqueJobTitleName(groups = {OnCreate.class})
    private String name;

    @Size(groups = {OnCreate.class, OnUpdate.class}, min = 3, max = 150, message = "Invalid Name: Must be of 3 - 150 characters")
    private String description;

    @Pattern(regexp = "dd/MM/yyyy", message = "Invalid pattern: date should be in ss/MM/yyyy pattern")
    @NotNull(groups = {OnCreate.class}, message = "startDate is mandatory for create")
    private String startDate;

    @Pattern(regexp = "dd/MM/yyyy", message = "Invalid pattern: date should be in ss/MM/yyyy pattern")
    private String endDate;

    @NotNull(groups = {OnCreate.class}, message = "createdBy is mandatory for create")
    private UserDto createdBy;
}
