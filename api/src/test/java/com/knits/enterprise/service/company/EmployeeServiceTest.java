package com.knits.enterprise.service.company;

import com.knits.enterprise.dto.company.EmployeeDto;
import com.knits.enterprise.mapper.company.EmployeeMapper;
import com.knits.enterprise.mapper.company.EmployeeMapperImpl;
import com.knits.enterprise.model.company.Employee;
import com.knits.enterprise.model.enums.Gender;
import com.knits.enterprise.repository.company.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void findEmployeeById() {

    }

    @Test
    @DisplayName("find ById employee Not Found")
    void findEmployeeByIdNotFound() {

    }


    @Test
    @DisplayName("partial update employee Success")
    void partialEmployee() {

    }

    @Test
    @DisplayName("delete employee Success")
    void deleteEmployee() {
    }

}