package com.knits.enterprise.controller.company;

import com.knits.enterprise.dto.common.PaginatedResponseDto;
import com.knits.enterprise.dto.company.EmployeeDto;
import com.knits.enterprise.dto.search.EmployeeSearchDto;
import com.knits.enterprise.service.company.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;


@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping(value = "/employees", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<EmployeeDto> create(@RequestBody EmployeeDto employeeDto) {
        log.debug("REST request to create Employee");
        return ResponseEntity
                .ok()
                .body(employeeService.saveNewEmployee(employeeDto));
    }

    @GetMapping(value = "/employees/{id}", produces = {"application/json"})
    public ResponseEntity<EmployeeDto> getById(@PathVariable(value = "id") final Long id) {
        log.debug("REST request to get Employee : {}", id);
        EmployeeDto employeeFound = employeeService.findEmployeeById(id);
        return ResponseEntity
                .ok()
                .body(employeeFound);

    }

    @PatchMapping(value = "/employees", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<EmployeeDto> update(@PathVariable(value = "id") @RequestBody EmployeeDto employeeDto) {
        EmployeeDto employeeFound = employeeService.partialUpdate(employeeDto);
        return ResponseEntity
                .ok()
                .body(employeeFound);
    }

    @PutMapping(value = "/employees", produces = {"application/json"})
    public ResponseEntity<EmployeeDto> delete(@PathVariable(value = "id") final Long id) {
        log.debug("REST request to delete Employee : {}", id);
        EmployeeDto employeeFound = employeeService.deleteEmployee(id);
        return ResponseEntity
                .ok()
                .body(employeeFound);
    }

    @GetMapping(value = "/searchEmployees", produces = {"application/json"})
    public ResponseEntity<PaginatedResponseDto<EmployeeDto>> search(@RequestBody EmployeeSearchDto searchDto) {
        log.debug("Rest Request to search Employees by filtering and pagination");
        PaginatedResponseDto<EmployeeDto> paginatedResponse = employeeService.search(searchDto);
        return ResponseEntity
                .ok()
                .body(paginatedResponse);
    }

    @GetMapping(value = "/exportEmployeesAsExcel", consumes = {"application/json"}, produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<ByteArrayResource> exportAsExcel(@RequestBody EmployeeSearchDto searchDto) {
        ByteArrayInputStream excelData = employeeService.employeesToExcel(searchDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Employees-.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new ByteArrayResource(excelData.readAllBytes()));
    }
}
