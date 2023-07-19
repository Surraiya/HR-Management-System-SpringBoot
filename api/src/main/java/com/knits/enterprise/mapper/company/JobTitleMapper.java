package com.knits.enterprise.mapper.company;

import com.knits.enterprise.config.Constants;
import com.knits.enterprise.mapper.common.EntityMapper;
import com.knits.enterprise.model.company.JobTitle;
import com.knits.enterprise.dto.company.JobTitleDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobTitleMapper extends AbstractOrganizationStructureMapper<JobTitle, JobTitleDto> {

    @Override
    @Mapping(source = "startDate", target = "startDate", dateFormat = Constants.DATE_FORMAT_DD_MM_YYYY)
    @Mapping(source = "endDate", target = "endDate", dateFormat = Constants.DATE_FORMAT_DD_MM_YYYY)
    @Mapping(target = "createdBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    JobTitleDto toDto(JobTitle entity);

    @Override
    @Mapping(source = "startDate", target = "startDate", dateFormat = Constants.DATE_FORMAT_DD_MM_YYYY)
    @Mapping(source = "endDate", target = "endDate", dateFormat = Constants.DATE_FORMAT_DD_MM_YYYY)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    JobTitle toEntity(JobTitleDto dto);

    @Override
    @Mapping(source = "startDate", target = "startDate", dateFormat = Constants.DATE_FORMAT_DD_MM_YYYY)
    @Mapping(source = "endDate", target = "endDate", dateFormat = Constants.DATE_FORMAT_DD_MM_YYYY)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<JobTitle> toEntities(List<JobTitleDto> entityList);

}
