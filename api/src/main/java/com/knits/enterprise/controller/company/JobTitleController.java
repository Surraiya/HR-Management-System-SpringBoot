package com.knits.enterprise.controller.company;

import com.knits.enterprise.dto.common.PaginatedResponseDto;
import com.knits.enterprise.dto.company.JobTitleDto;
import com.knits.enterprise.dto.search.JobTitleSearchDto;
import com.knits.enterprise.service.company.JobTitleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Slf4j
public class JobTitleController {

    @Autowired
    private JobTitleService jobTitleService;

    @PostMapping(value = "/saveJobTitle", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<JobTitleDto> createNewJobTitle(@RequestBody JobTitleDto jobTitleDto) {
        log.debug("REST request to create jobTitle");
        return ResponseEntity
                .ok()
                .body(jobTitleService.saveNewJobTitle(jobTitleDto));
    }

    @GetMapping(value = "/viewJobTitle/{id}", produces = {"application/json"})
    public ResponseEntity<JobTitleDto> getjobTitleById(@PathVariable(value = "id") final Long id) {
        log.debug("REST request to view jobTitle of ID: {}", id);
        JobTitleDto jobTitleFound = jobTitleService.findJobTitleById(id);
        return ResponseEntity
                .ok()
                .body(jobTitleFound);
    }

    @PatchMapping(value = "/updateJobTitle/{id}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<JobTitleDto> updateJobTitle(@PathVariable(value = "id") final Long id, @RequestBody JobTitleDto jobTitleDto) {
        log.debug("REST request to update jobTitle of ID: {}", id);
        JobTitleDto updatedJobTitle = jobTitleService.partialUpdate(id, jobTitleDto);
        return ResponseEntity
                .ok()
                .body(updatedJobTitle);
    }

    @DeleteMapping("/deleteJobTitle/{id}")
    public ResponseEntity<Void> deleteJobTitle(@PathVariable(value = "id") final Long id) {
        log.debug("REST request to delete JobTitle of ID: {}", id);
        jobTitleService.deleteJobTitle(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping(value = "/searchJobTitles", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<PaginatedResponseDto<JobTitleDto>> searchJobTitles(@RequestBody JobTitleSearchDto searchDto){
        log.debug("Rest Request to search JobTitles by sorting and pagination");
        PaginatedResponseDto<JobTitleDto> paginatedResponse = jobTitleService.findJobTitlesBySortingAndPagination(searchDto);
        return ResponseEntity
                .ok()
                .body(paginatedResponse);
    }
}