package com.knits.enterprise.controller.company;

import com.knits.enterprise.dto.company.JobTitleDto;
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

    @PostMapping(value = "/jobTitles", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<JobTitleDto> createNewJobTitle(@RequestBody JobTitleDto jobTitleDto) {
        log.debug("REST request to create jobTitle");
        return ResponseEntity
                .ok()
                .body(jobTitleService.saveNewJobTitle(jobTitleDto));
    }

    @GetMapping(value = "/jobTitles/{id}", produces = {"application/json"})
    public ResponseEntity<JobTitleDto> getjobTitleById(@PathVariable(value = "id") final Long id) {
        log.debug("REST request to get jobTitle : {}", id);
        JobTitleDto jobTitleFound = jobTitleService.findJobTitleById(id);
        return ResponseEntity
                .ok()
                .body(jobTitleFound);

    }

    @PatchMapping(value = "/jobTitles", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<JobTitleDto> updatejobTitle(@PathVariable(value = "id") @RequestBody JobTitleDto jobTitleDto) {
        JobTitleDto jobTitleFound = jobTitleService.partialUpdate(jobTitleDto);
        return ResponseEntity
                .ok()
                .body(jobTitleFound);
    }

    @PutMapping(value = "/jobTitles", produces = {"application/json"})
    public ResponseEntity<JobTitleDto> deletejobTitle(@PathVariable(value = "id") final Long id) {
        log.debug("REST request to delete jobTitle : {}", id);
        JobTitleDto jobTitleFound = jobTitleService.deleteJobTitle(id);
        return ResponseEntity
                .ok()
                .body(jobTitleFound);
    }

}
