package com.knits.enterprise.service.company;
import com.knits.enterprise.config.Constants;
import com.knits.enterprise.dto.common.PaginatedResponseDto;
import com.knits.enterprise.dto.company.JobTitleDto;
import com.knits.enterprise.dto.search.JobTitleSearchDto;
import com.knits.enterprise.exceptions.UserException;
import com.knits.enterprise.mapper.company.JobTitleMapper;
import com.knits.enterprise.mapper.company.JobTitleMapperImpl;
import com.knits.enterprise.model.company.JobTitle;
import com.knits.enterprise.model.security.User;
import com.knits.enterprise.repository.company.JobTitleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
@ExtendWith(MockitoExtension.class)
class JobTitleServiceTest {
    @Mock
    private JobTitleRepository jobTitleRepository;
    @Spy
    private JobTitleMapper jobTitleMapper = new JobTitleMapperImpl();
    @Captor
    private ArgumentCaptor<JobTitle> jobTitleArgumentCaptor;
    @Captor
    private ArgumentCaptor<Long> idArgumentCaptor;
    @InjectMocks
    private JobTitleService jobTitleService;

    private User user;
    private String exceptionMessage;
    private Long id;

    @BeforeEach
    void setUp() {
        exceptionMessage = "User#" + id + " not found";
        id = 2L;
        user = User.builder()
                .id(1L)
                .firstName("A test firstName")
                .lastName("A test lastName")
                .password("A test password")
                .active(true)
                .email("test@test.com")
                .build();
    }

    @Test
    @DisplayName("create jobTitle success")
    void saveNewJobTitle() {
        JobTitleDto jobTitleDto = JobTitleDto.builder()
                .name("A test name")
                .description("A test description")
                .startDate("04/09/2017")
                .active(true)
                .build();

        when(jobTitleRepository.save(Mockito.any(JobTitle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        JobTitleDto savedDto = jobTitleService.saveNewJobTitle(jobTitleDto);

        verify(jobTitleRepository).save(jobTitleArgumentCaptor.capture());
        verify(jobTitleRepository, times(1)).save(jobTitleArgumentCaptor.getValue());
        assertEquals(jobTitleDto, savedDto);
    }

    @Test
    @DisplayName("Find JobTitle by Id - Success")
    void findJobTitleById_Success() {
        Long jobId = 1L;
        JobTitle savedJobTitle = JobTitle.builder()
                .id(jobId)
                .name("A test name")
                .description("A test description")
                .startDate(LocalDate.parse("04/09/2023", Constants.DATE_FORMATTER))
                .active(true)
                .createdBy(user)
                .build();

        when(jobTitleRepository.findById(any())).thenReturn(Optional.of(savedJobTitle));

        JobTitleDto foundJobTitle = jobTitleService.findJobTitleById(jobId);

        verify(jobTitleRepository).findById(eq(jobId));
        assertNotNull(foundJobTitle);
        assertEquals(jobTitleMapper.toDto(savedJobTitle), foundJobTitle);
    }

    @Test
    @DisplayName("find jobTitle by Id fail : is not found")
    void findJobTitleByIdFail() {
        JobTitle jobTitle = JobTitle.builder()
                .id(1L)
                .name("A test name")
                .description("A test description")
                .startDate(LocalDate.parse("04/09/2023", Constants.DATE_FORMATTER))
                .active(true)
                .createdBy(user)
                .build();

        when(jobTitleRepository.findById(any())).thenThrow(new UserException(exceptionMessage));
        UserException exception = assertThrows(UserException.class,
                () -> jobTitleService.findJobTitleById(jobTitle.getId()));
        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Update JobTitle - Success")
    void partialUpdate_Success() {
        Long jobId = 1L;
        String updatedDescription = "Updated job description";
        JobTitleDto jobTitleDto = JobTitleDto.builder()
                .description(updatedDescription)
                .build();
        JobTitle savedJobTitle = JobTitle.builder()
                .id(jobId)
                .name("A test name")
                .description("A test description")
                .startDate(LocalDate.parse("04/09/2023", Constants.DATE_FORMATTER))
                .active(true)
                .createdBy(user)
                .build();

        when(jobTitleRepository.findById(any())).thenReturn(Optional.of(savedJobTitle));
        when(jobTitleRepository.save(any(JobTitle.class))).thenReturn(savedJobTitle);

        JobTitleDto updatedJobTitle = jobTitleService.partialUpdate(jobId, jobTitleDto);

        verify(jobTitleRepository).findById(eq(jobId));
        verify(jobTitleRepository).save(jobTitleArgumentCaptor.capture());

        JobTitle updatedJobTitleEntity = jobTitleArgumentCaptor.getValue();
        assertEquals(updatedDescription, updatedJobTitleEntity.getDescription());
        assertEquals(jobTitleDto, updatedJobTitle);
    }

    @Test
    @DisplayName("update jobTitle fail : id not found")
    void partialUpdateFail() {
        Long jobId = 1L;
        String updatedDescription = "Updated job description";
        JobTitleDto jobTitleDto = JobTitleDto.builder()
                .description(updatedDescription)
                .build();

        when(jobTitleRepository.findById(eq(jobId))).thenThrow(new UserException(exceptionMessage));
        UserException exception = assertThrows(UserException.class,
                () -> jobTitleService.partialUpdate(jobId, jobTitleDto));

        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    @DisplayName("delete jobTitle by id success")
    void deleteJobTitleSuccess() {
        JobTitle jobTitle = JobTitle.builder()
                .id(1L)
                .name("A test name")
                .description("A test description")
                .startDate(LocalDate.parse("04/09/2023", Constants.DATE_FORMATTER))
                .active(true)
                .createdBy(user)
                .build();
        when(jobTitleRepository.findById(any())).thenReturn(Optional.of(jobTitle));
        jobTitleService.delete(jobTitle.getId());

        verify(jobTitleRepository, times(1)).findById(jobTitle.getId());
        verify(jobTitleRepository, times(1)).delete(jobTitle);
    }

    @Test
    @DisplayName("Delete JobTitle by ID - Fail: ID Not Found")
    void deleteJobTitleByIdFail_IdNotFound() {
        when(jobTitleRepository.findById(any())).thenThrow(new UserException(exceptionMessage));

        UserException exception = assertThrows(UserException.class,
                () -> jobTitleService.delete(1L));

        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Search JobTitles by Pagination and sorting - Success")
    void searchJobTitles() {
        JobTitleDto firstJobTitleDto = JobTitleDto.builder()
                .name("A test name")
                .description("A test description")
                .startDate("04/09/2023")
                .active(true)
                .build();

        JobTitleDto secondJobTitleDto = JobTitleDto.builder()
                .name("A second test name")
                .description("A second test description")
                .startDate("04/09/2023")
                .build();

        JobTitle firstJobTitle = JobTitle.builder()
                .id(1L)
                .name("A test name")
                .description("A test description")
                .startDate(LocalDate.parse("04/09/2023", Constants.DATE_FORMATTER))
                .active(true)
                .createdBy(user)
                .build();

        JobTitle secondJobTitle = JobTitle.builder()
                .id(1L)
                .name("A second test name")
                .description("A second test description")
                .startDate(LocalDate.parse("04/09/2023", Constants.DATE_FORMATTER))
                .active(true)
                .createdBy(user)
                .build();

        List<JobTitle> jobTitleList = List.of(firstJobTitle, secondJobTitle);
        List<JobTitleDto> jobTitleDtoList = List.of(firstJobTitleDto, secondJobTitleDto);

        Page<JobTitle> jobTitlePage = new PageImpl<>(jobTitleList);
        JobTitleSearchDto searchDto = new JobTitleSearchDto();
        searchDto.setLimit(2);

        when(jobTitleRepository.findAll(ArgumentMatchers.<Specification<JobTitle>>any(), (Pageable) any())).thenReturn(jobTitlePage);
        when(jobTitleMapper.toDtos(jobTitlePage.getContent())).thenReturn(jobTitleDtoList);

        PaginatedResponseDto<JobTitleDto> allJobTitles = jobTitleService.findBySortingAndPagination(searchDto);

        verify(jobTitleRepository).findAll(ArgumentMatchers.<Specification<JobTitle>>any(), (Pageable) any());
        verify(jobTitleMapper, times(2)).toDtos(jobTitlePage.getContent());

        assertEquals(searchDto.getPage(), allJobTitles.getPage());
        assertEquals(searchDto.getDir().name(), allJobTitles.getSortDirection());
        assertEquals(searchDto.getSort(), allJobTitles.getSortingFields());
        assertEquals(searchDto.getLimit(), allJobTitles.getSize());
        assertEquals(jobTitleDtoList, allJobTitles.getData());
    }

    @Test
    @DisplayName("Search JobTitles by Pagination and Sorting - Failure")
    void searchJobTitlesFailure() {
        JobTitleSearchDto searchDto = new JobTitleSearchDto();
        searchDto.setLimit(2);

        when(jobTitleRepository.findAll(ArgumentMatchers.<Specification<JobTitle>>any(), (Pageable) any()))
                .thenThrow(new RuntimeException("Failed to retrieve job titles"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> jobTitleService.findBySortingAndPagination(searchDto));

        assertEquals("Failed to retrieve job titles", exception.getMessage());
    }
}