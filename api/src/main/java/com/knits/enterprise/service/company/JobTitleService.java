package com.knits.enterprise.service.company;


import com.knits.enterprise.dto.common.PaginatedResponseDto;
import com.knits.enterprise.dto.company.JobTitleDto;
import com.knits.enterprise.dto.search.GenericSearchDto;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class JobTitleService {

    private final JobTitleMapper jobTitleMapper;
    private final JobTitleRepository jobTitleRepository;

    @Transactional
    public JobTitleDto saveNewJobTitle(JobTitleDto jobTitleDto) {
        JobTitle jobTitle = jobTitleMapper.toEntity(jobTitleDto);
        jobTitle.setStartDate(LocalDateTime.now());
        jobTitle.setActive(true);
        User currentUser = new User();
        currentUser.setId(1L);
        jobTitle.setCreatedBy(currentUser); //todo what to do here
        JobTitle savedJobTitle =jobTitleRepository.save(jobTitle);
        return jobTitleMapper.toDto(savedJobTitle );
    }

    @Transactional
    public JobTitleDto findJobTitleById(Long id) {
        JobTitle jobTitle = jobTitleRepository.findById(id).orElseThrow(() -> new UserException("Job Title #" + id + " not found"));
        return jobTitleMapper.toDto(jobTitle);
    }

    @Transactional
    public JobTitleDto partialUpdate(JobTitleDto jobTitleDto) {
        JobTitle jobTitle = jobTitleRepository.findById(jobTitleDto.getId()).orElseThrow(() -> new UserException("Job Title#" + jobTitleDto.getId() + " not found"));
        jobTitleMapper.partialUpdate(jobTitle, jobTitleDto);
        jobTitleRepository.save(jobTitle);
        return jobTitleMapper.toDto(jobTitle);
    }

    @Transactional
    public JobTitleDto deleteJobTitle(Long id) {
        JobTitle jobTitle = jobTitleRepository.findById(id).get();
        jobTitleRepository.delete(jobTitle);
        return jobTitleMapper.toDto(jobTitle);

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
