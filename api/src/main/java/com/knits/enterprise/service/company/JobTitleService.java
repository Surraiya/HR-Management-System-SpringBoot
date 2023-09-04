package com.knits.enterprise.service.company;


import com.knits.enterprise.dto.common.PaginatedResponseDto;
import com.knits.enterprise.dto.company.JobTitleDto;
import com.knits.enterprise.dto.search.JobTitleSearchDto;
import com.knits.enterprise.exceptions.UserException;
import com.knits.enterprise.mapper.company.JobTitleMapper;
import com.knits.enterprise.model.company.JobTitle;
import com.knits.enterprise.model.security.User;
import com.knits.enterprise.repository.company.JobTitleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class JobTitleService {

    private final JobTitleMapper jobTitleMapper;
    private final JobTitleRepository jobTitleRepository;

    public JobTitleDto saveNewJobTitle(JobTitleDto jobTitleDto) {
        JobTitle jobTitle = jobTitleMapper.toEntity(jobTitleDto);
        jobTitle = jobTitle.toBuilder()
                .startDate(LocalDate.now())
                .active(true)
                .createdBy(User.builder().id(1L).active(true).build())
                .build();
        JobTitle savedJobTitle = jobTitleRepository.save(jobTitle);
        return jobTitleMapper.toDto(savedJobTitle);
    }

    public JobTitleDto findJobTitleById(Long id) {
        JobTitle jobTitle = jobTitleRepository.findById(id)
                .orElseThrow(() -> new UserException(String.format("Job Title with ID %d not found",id)));
        return jobTitleMapper.toDto(jobTitle);
    }

    public JobTitleDto partialUpdate(Long id, JobTitleDto jobTitleDto) {
        JobTitle jobTitle = jobTitleRepository.findById(id)
                .orElseThrow(() -> new UserException(String.format("Job Title with Id %d not found", id)));
        jobTitleMapper.partialUpdate(jobTitle, jobTitleDto);
        jobTitleRepository.save(jobTitle);
        return jobTitleMapper.toDto(jobTitle);
    }

    public void delete(Long id) {
        JobTitle jobTitle = jobTitleRepository.findById(id)
                .orElseThrow(() -> new UserException(String.format("Job Title with Id %d does not exist", id)));
        jobTitleRepository.delete(jobTitle);
    }

    public PaginatedResponseDto<JobTitleDto> findBySortingAndPagination(JobTitleSearchDto searchDto) {

            Page<JobTitle> jobTitlesPage = jobTitleRepository.findAll(searchDto.getSpecification(), searchDto.getPageable());
            List<JobTitleDto> jobTitlesDtos = jobTitleMapper.toDtos(jobTitlesPage.getContent());
            if(jobTitlesDtos.isEmpty())
                throw new UserException("No result found");
            return PaginatedResponseDto.<JobTitleDto>builder()
                    .page(searchDto.getPage())
                    .size(jobTitlesDtos.size())
                    .sortingFields(searchDto.getSort())
                    .sortDirection(searchDto.getDir().name())
                    .data(jobTitlesDtos)
                    .build();
    }
}
