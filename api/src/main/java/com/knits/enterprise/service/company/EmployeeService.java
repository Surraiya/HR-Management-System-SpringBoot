package com.knits.enterprise.service.company;


import com.knits.enterprise.dto.common.PaginatedResponseDto;
import com.knits.enterprise.dto.company.EmployeeDto;
import com.knits.enterprise.dto.search.EmployeeSearchDto;
import com.knits.enterprise.exceptions.UserException;
import com.knits.enterprise.mapper.company.EmployeeMapper;
import com.knits.enterprise.model.company.Employee;
import com.knits.enterprise.repository.company.EmployeeRepository;
import com.knits.enterprise.utils.ExcelHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.*;


@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;

    public EmployeeDto saveNewEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeMapper.toEntity(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDto(savedEmployee);
    }

    public EmployeeDto findEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new UserException("User#" + id + " not found"));
        return employeeMapper.toDto(employee);
    }

    public List<EmployeeDto> findAll(){
        List<Employee> employees = employeeRepository.findAll();
        return employeeMapper.toDtos(employees);
    }

    public EmployeeDto partialUpdate(EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(employeeDto.getId()).orElseThrow(() -> new UserException("User#" + employeeDto.getId() + " not found"));

        employeeMapper.partialUpdate(employee, employeeDto);
        employeeRepository.save(employee);
        return employeeMapper.toDto(employee);
    }

    public EmployeeDto deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).get();
        employeeRepository.delete(employee);
        return employeeMapper.toDto(employee);

    }

    public PaginatedResponseDto<EmployeeDto> search(EmployeeSearchDto searchDto) {
        List<EmployeeDto> employeeDtos = findSelectedEmployees(searchDto);
        if(employeeDtos.isEmpty())
            throw new UserException("No result found");
        return PaginatedResponseDto.<EmployeeDto>builder()
                .page(searchDto.getPage())
                .size(employeeDtos.size())
                .sortingFields(searchDto.getSort())
                .sortDirection(searchDto.getDir().name())
                .data(employeeDtos)
                .build();
    }

    public List<EmployeeDto> findSelectedEmployees(EmployeeSearchDto searchDto){
        Page<Employee> employeesPage = employeeRepository.findAll(searchDto.getSpecification(), searchDto.getPageable());
        return employeeMapper.toDtos(employeesPage.getContent());
    }

    public ByteArrayInputStream employeesToExcel(EmployeeSearchDto searchDto) {
        List<EmployeeDto> employees = findSelectedEmployees(searchDto);
        String[] headers = { "First Name", "Last Name", "Email", "BirthDate", "Gender", "Start Date", "End Date", "Company Phone Number", "Business Unit name", "Office Country", "Department name", "Job title", "Organization Country" };
        Map<String, List<Object>> employeeData = mapHeaderToCellValue(employees, headers);
        return ExcelHelper.toExcel(employeeData);
    }

    public Map<String, List<Object>> mapHeaderToCellValue(List<EmployeeDto> employees, String[] headers) {
        Map<String, List<Object>> result = new LinkedHashMap<>();

        for (String header : headers) {
            result.put(header, new ArrayList<>());
        }

        for (EmployeeDto employee : employees) {
            for (String header : headers) {
                switch (header) {
                    case "First Name":
                        result.get(header).add(employee.getFirstName());
                        break;
                    case "Last Name":
                        result.get(header).add(employee.getLastName());
                        break;
                    case "Email":
                        result.get(header).add(employee.getEmail());
                        break;
                    case "BirthDate":
                        result.get(header).add(employee.getBirthDate());
                        break;
                    case "Gender":
                        result.get(header).add(employee.getGender());
                        break;
                    case "Start Date":
                        result.get(header).add(employee.getStartDate());
                        break;
                    case "End Date":
                        result.get(header).add(employee.getEndDate());
                        break;
                    case "Company Phone Number":
                        result.get(header).add(employee.getCompanyPhone());
                        break;
                    case "Business Unit name":
                        result.get(header).add(employee.getBusinessUnit());
                        break;
                    case "Office Country":
                        result.get(header).add(employee.getOffice());
                        break;
                    case "Department name":
                        result.get(header).add(employee.getDepartment());
                        break;
                    case "Job title":
                        result.get(header).add(employee.getJobTitle());
                        break;
                    case "Organization Country":
                        result.get(header).add(employee.getOrganization());
                        break;
                }
            }
        }

        return result;
    }
}


