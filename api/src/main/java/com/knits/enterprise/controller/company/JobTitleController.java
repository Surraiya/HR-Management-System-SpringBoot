package com.knits.enterprise.controller.company;

import com.knits.enterprise.dto.common.PaginatedResponseDto;
import com.knits.enterprise.dto.company.JobTitleDto;
import com.knits.enterprise.dto.search.JobTitleSearchDto;
import com.knits.enterprise.service.company.JobTitleService;
import com.knits.enterprise.validations.OnCreate;
import com.knits.enterprise.validations.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Slf4j
public class JobTitleController {

    @Autowired
    private JobTitleService jobTitleService;

    @Operation(summary = "Create a new JobTitle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created the JobTitle",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobTitleDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content) })
    @Validated(OnCreate.class)
    @PostMapping(value = "/saveJobTitle", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<JobTitleDto> create(@Parameter(description = "JobTitle to create") @RequestBody @Valid JobTitleDto jobTitleDto) {
        log.debug("REST request to create jobTitle");
        return ResponseEntity
                .ok()
                .body(jobTitleService.saveNewJobTitle(jobTitleDto));
    }

    @Operation(summary = "Get a JobTitle By its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JobTitle is found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobTitleDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "JobTitle not found",
                    content = @Content) })
    @GetMapping(value = "/viewJobTitle/{id}", produces = {"application/json"})
    public ResponseEntity<JobTitleDto> findById(@Parameter(description = "id of jobTitle to be searched") @PathVariable(value = "id") final Long id) {
        log.debug("REST request to view jobTitle of ID: {}", id);
        JobTitleDto jobTitleFound = jobTitleService.findJobTitleById(id);
        return ResponseEntity
                .ok()
                .body(jobTitleFound);
    }

    @Operation(summary = "Update an existing jobTitle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the jobTitle",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobTitleDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "JobTitle not found",
                    content = @Content)})
    @Validated(OnUpdate.class)
    @PatchMapping(value = "/updateJobTitle/{id}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<JobTitleDto> update(@Parameter(description = "id of jobTitle to be searched") @PathVariable(value = "id") final Long id,
                                                      @Parameter(description = "JobTitle to update") @RequestBody @Valid JobTitleDto jobTitleDto) {
        log.debug("REST request to update jobTitle of ID: {}", id);
        JobTitleDto updatedJobTitle = jobTitleService.partialUpdate(id, jobTitleDto);
        return ResponseEntity
                .ok()
                .body(updatedJobTitle);
    }

    @Operation(summary = "Delete an existing jobTitle by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted the jobTitle",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobTitleDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "JobTitle not found",
                    content = @Content)})
    @DeleteMapping("/deleteJobTitle/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "id of jobTitle to be searched") @PathVariable(value = "id") final Long id) {
        log.debug("REST request to delete JobTitle of ID: {}", id);
        jobTitleService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "Get the paginated list of jobTitles,optionally filtered by its name, createdBy, creationDateFrom and/or creationDateTo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the paginated jobTitles",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginatedResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid date or name supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Paginated JobTitles not found",
                    content = @Content)})
    @GetMapping(value = "/searchJobTitles", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<PaginatedResponseDto<JobTitleDto>> search(@Parameter(description = "JobTitles search option") @RequestBody JobTitleSearchDto searchDto){
        log.debug("Rest Request to search JobTitles by sorting and pagination");
        PaginatedResponseDto<JobTitleDto> paginatedResponse = jobTitleService.findBySortingAndPagination(searchDto);
        return ResponseEntity
                .ok()
                .body(paginatedResponse);
    }
}