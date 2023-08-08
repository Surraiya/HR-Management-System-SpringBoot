package com.knits.enterprise.service.company;


import com.knits.enterprise.dto.common.PaginatedResponseDto;
import com.knits.enterprise.dto.company.JobTitleDto;
import com.knits.enterprise.dto.search.GenericSearchDto;
import com.knits.enterprise.exceptions.JobTitleException;
import com.knits.enterprise.mapper.company.JobTitleMapper;
import com.knits.enterprise.model.company.JobTitle;
import com.knits.enterprise.model.security.User;
import com.knits.enterprise.repository.company.JobTitleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        jobTitle.setStartDate(LocalDateTime.now());
        jobTitle.setActive(true);
        User currentUser = new User();
        currentUser.setId(1L);
        jobTitle.setCreatedBy(currentUser);
        JobTitle savedJobTitle =jobTitleRepository.save(jobTitle);
        return jobTitleMapper.toDto(savedJobTitle);
    }

    public JobTitleDto findJobTitleById(Long id) {
        JobTitle jobTitle = jobTitleRepository.findById(id)
                .orElseThrow(() -> new JobTitleException(String.format("Job Title with ID %d not found",id)));
        return jobTitleMapper.toDto(jobTitle);
    }

    public JobTitleDto partialUpdate(Long id, JobTitleDto jobTitleDto) {
        JobTitle jobTitle = jobTitleRepository.findById(id)
                .orElseThrow(() -> new JobTitleException(String.format("Job Title with Id %d not found", id)));
        jobTitleMapper.partialUpdate(jobTitle, jobTitleDto);
        jobTitleRepository.save(jobTitle);
        return jobTitleMapper.toDto(jobTitle);
    }

    public void deleteJobTitle(Long id) {
        JobTitle jobTitle = jobTitleRepository.findById(id)
                .orElseThrow(() -> new JobTitleException(String.format("Job Title with Id %d does not exist", id)));
        jobTitleRepository.delete(jobTitle);
    }

    public PaginatedResponseDto<JobTitleDto> listAll(GenericSearchDto<JobTitle> searchDto) {

        Page<JobTitle> jobTitlesPage = jobTitleRepository.findAll(searchDto.getSpecification(),searchDto.getPageable());
        List<JobTitleDto> jobTitleDtos = jobTitleMapper.toDtos(jobTitlesPage.getContent());

        return PaginatedResponseDto.<JobTitleDto>builder()
                .page(searchDto.getPage())
                .size(jobTitleDtos.size())
                .sortingFields(searchDto.getSort())
                .sortDirection(searchDto.getDir().name())
                .data(jobTitleDtos)
                .build();
    }
}
