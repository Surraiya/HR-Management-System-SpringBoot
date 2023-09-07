package com.knits.enterprise.service.company;

import com.knits.enterprise.config.Constants;
import com.knits.enterprise.dto.company.EmployeeDto;
import com.knits.enterprise.dto.company.JobTitleDto;
import com.knits.enterprise.exceptions.UserException;
import com.knits.enterprise.mapper.company.EmployeeMapper;
import com.knits.enterprise.mapper.company.EmployeeMapperImpl;
import com.knits.enterprise.model.company.Employee;
import com.knits.enterprise.model.company.JobTitle;
import com.knits.enterprise.model.enums.Gender;
import com.knits.enterprise.repository.company.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private EmployeeMapper employeeMapper= new EmployeeMapperImpl();

    @Captor
    private ArgumentCaptor<Employee> employeeArgumentCaptor;

    @InjectMocks
    private EmployeeService employeeService;

    private Long id;

    private String exceptionMessage;

    @BeforeEach
    void setUp() {
        exceptionMessage = "User#" + id + " not found";
        id = 2L;
    }

    @Test
    @DisplayName("Save new employee Success")
    void saveNewEmployee() {

        EmployeeDto toSaveDto = EmployeeDto.builder()
                .firstName("A Mock firstName")
                .lastName("A Mock firstName")
                .email("mockemail@gmail.com")
                .birthDate("10/10/2000")
                .gender(Gender.MALE)
                .startDate("10/11/2022")
                .companyPhone("123456789")
                .companyMobileNumber("123456789")
                .build();

        when(employeeRepository.save(Mockito.any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EmployeeDto savedDto = employeeService.saveNewEmployee(toSaveDto);

        verify(employeeRepository).save(employeeArgumentCaptor.capture());
        Employee toSaveEntity = employeeArgumentCaptor.getValue();

        verify(employeeMapper, times(1)).toDto(toSaveEntity);
        verify(employeeMapper, times(1)).toEntity(toSaveDto);
        verify(employeeRepository, times(1)).save(toSaveEntity);

        assertThat(toSaveDto).isEqualTo(savedDto);

    }

    @Test
    @DisplayName("Save employee Fail: Business Unit doesnt exist")
    void saveNewExceptionBusinessUnit() {

    }

    @Test
    @DisplayName("Save employee Fail: Office doesnt exist")
    void saveNewExceptionOffice() {

    }

    @Test
    @DisplayName("Save employee Fail: JobTitle doesnt exist")
    void saveNewExceptionJobTitle() {

    }

    @Test
    @DisplayName("Save employee Fail: Organization doesnt exist")
    void saveNewExceptionOrganization() {

    }

    @Test
    @DisplayName("Save employee Fail: Division doesnt exist")
    void saveNewExceptionDivision() {

    }

    @Test
    @DisplayName("Save employee Fail: Department doesnt exist")
    void saveNewExceptionDepartment() {

    }


    @Test
    @DisplayName("find ById employee Success")
    void findEmployeeByIdSuccess() {
        Long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .id(employeeId)
                .firstName("A Mock firstName")
                .lastName("A Mock firstName")
                .email("mockemail@gmail.com")
                .birthDate(LocalDate.parse("10/10/2000", Constants.DATE_FORMATTER))
                .gender(Gender.MALE)
                .startDate(LocalDate.parse("10/11/2022", Constants.DATE_FORMATTER))
                .companyPhone("123456789")
                .companyMobileNumber("123456789")
                .build();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(savedEmployee));

        EmployeeDto foundEmployee = employeeService.findEmployeeById(employeeId);

        verify(employeeRepository).findById(employeeId);
        verify(employeeMapper, times(1)).toDto(savedEmployee);

        assertNotNull(foundEmployee);
        assertEquals(foundEmployee, employeeMapper.toDto(savedEmployee));
    }

    @Test
    @DisplayName("find ById employee Not Found")
    void findEmployeeByIdNotFound() {
        when(employeeRepository.findById(id)).thenThrow(new UserException(exceptionMessage));
        UserException exception = assertThrows(UserException.class,
                () -> employeeService.findEmployeeById(id));
        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    @DisplayName("partial update employee Success")
    void partialEmployee() {

    }

    @Test
    @DisplayName("delete employee Success")
    void deleteEmployee() {
    }

    @Test
    void testMapHeaderToCellValue() {
        List<EmployeeDto> employees = new ArrayList<>();

        EmployeeDto employee1 = EmployeeDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .birthDate("01/01/1990")
                .gender(Gender.MALE)
                .startDate("01/01/2020")
                .endDate("12/31/2022")
                .companyPhone("123-456-7890")
                .businessUnit(null)
                .office(null)
                .department(null)
                .jobTitle(null)
                .organization(null)
                .build();

        EmployeeDto employee2 = EmployeeDto.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .birthDate("02/15/1995")
                .gender(Gender.FEMALE)
                .startDate("03/01/2021")
                .endDate(null)
                .companyPhone("987-654-3210")
                .businessUnit(null)
                .office(null)
                .department(null)
                .jobTitle(null)
                .organization(null)
                .build();

        employees.add(employee1);
        employees.add(employee2);

        String[] headers = {
                "First Name",
                "Last Name",
                "Email",
                "BirthDate",
                "Gender",
                "Start Date",
                "End Date",
                "Company Phone Number",
                "Business Unit name",
                "Office Country",
                "Department name",
                "Job title",
                "Organization Country"
        };

        Map<String, List<Object>> result = employeeService.mapHeaderToCellValue(employees, headers);
        System.out.println(result);

        assertEquals(2, result.get("First Name").size());
        assertEquals("John", result.get("First Name").get(0));
        assertEquals("Jane", result.get("First Name").get(1));
    }

}