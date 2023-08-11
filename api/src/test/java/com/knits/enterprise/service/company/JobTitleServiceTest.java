package com.knits.enterprise.service.company;
import com.knits.enterprise.dto.common.PaginatedResponseDto;
import com.knits.enterprise.dto.company.JobTitleDto;
import com.knits.enterprise.dto.search.JobTitleSearchDto;
import com.knits.enterprise.exceptions.JobTitleException;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private JobTitleDto jobTitleDto;
    private JobTitle jobTitle;
    private String exceptionMessage;
    private Long id;
    private List<JobTitle> jobTitleList;
    private List<JobTitleDto> jobTitleDtoList;

    @BeforeEach
    void setUp() {
        exceptionMessage = "User#" + id + " not found";
        id = 2L;
        jobTitleDto = JobTitleDto.builder()
                .name("A test name")
                .description("A test description")
                .startDate(String.valueOf(LocalDateTime.now()))
                .active(true)
                .build();

        User user = User.builder()
                .id(1L)
                .firstName("A test firstName")
                .lastName("A test lastName")
                .password("A test password")
                .active(true)
                .email("test@test.com")
                .build();

        jobTitle = JobTitle.builder()
                .id(1L)
                .name("A test name")
                .description("A test description")
                .startDate(LocalDateTime.now())
                .active(true)
                .createdBy(user)
                .build();

        JobTitle secondJobTitle = JobTitle.builder()
                .id(1L)
                .name("A second test name")
                .description("A second test description")
                .startDate(LocalDateTime.now())
                .active(true)
                .createdBy(user)
                .build();

        JobTitleDto secondJobTitleDto = JobTitleDto.builder()
                .name("A second test name")
                .description("A second test description")
                .startDate(String.valueOf(LocalDateTime.now()))
                .build();

        jobTitleList = new ArrayList<>();
        jobTitleList.add(jobTitle);
        jobTitleList.add(secondJobTitle);

        jobTitleDtoList = new ArrayList<>();
        jobTitleDtoList.add(jobTitleDto);
        jobTitleDtoList.add(secondJobTitleDto);
    }

    @Test
    @DisplayName("create jobTitle success")
    void saveNewJobTitle() {

        when(jobTitleMapper.toEntity(any())).thenReturn(jobTitle);
        when(jobTitleRepository.save(any())).thenReturn(jobTitle);
        when(jobTitleMapper.toDto(any())).thenReturn(jobTitleDto);
        JobTitleDto savedDto = jobTitleService.saveNewJobTitle(jobTitleDto);
        verify(jobTitleRepository).save(jobTitleArgumentCaptor.capture());
        JobTitle toSaveEntity = jobTitleArgumentCaptor.getValue();
        verify(jobTitleMapper, times(1)).toDto(toSaveEntity);
        verify(jobTitleMapper, times(1)).toEntity(jobTitleDto);
        verify(jobTitleRepository, times(1)).save(toSaveEntity);
        assertEquals(jobTitleDto, savedDto);
    }

    @Test
    @DisplayName("find jobTitle by Id success")
    void findJobTitleById() {
        when(jobTitleRepository.findById(any())).thenReturn(Optional.of(jobTitle));
        JobTitleDto jobTitleById = jobTitleService.findJobTitleById(jobTitle.getId());
        verify(jobTitleRepository).findById(idArgumentCaptor.capture());
        Long id = idArgumentCaptor.getValue();
        assertEquals(jobTitle.getId(), id);
        assertEquals(jobTitleDto, jobTitleById);
    }

    @Test
    @DisplayName("find jobTitle by Id fail : is not found")
    void findJobTitleByIdFail() {
        when(jobTitleRepository.findById(any())).thenThrow(new JobTitleException(exceptionMessage));
        JobTitleException exception = assertThrows(JobTitleException.class,
                () -> jobTitleService.findJobTitleById(jobTitle.getId()));
        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    @DisplayName("update jobTitle success")
    void partialUpdate() {
        when(jobTitleRepository.findById(any())).thenReturn(Optional.of(jobTitle));
        when(jobTitleRepository.save(any())).thenReturn(jobTitle);
        when(jobTitleMapper.toDto(any())).thenReturn(jobTitleDto);
        JobTitleDto updatedJobTitle = jobTitleService.partialUpdate(jobTitle.getId(), jobTitleDto);
        verify(jobTitleRepository).findById(idArgumentCaptor.capture());
        verify(jobTitleRepository).save(jobTitleArgumentCaptor.capture());
        JobTitle value = jobTitleArgumentCaptor.getValue();
        verify(jobTitleMapper).toDto(value);
        assertEquals(jobTitleDto, updatedJobTitle);
    }

    @Test
    @DisplayName("update jobTitle fail : id not found")
    void partialUpdateFail() {
        when(jobTitleRepository.findById(any())).thenThrow(new JobTitleException(exceptionMessage));
        JobTitleException exception = assertThrows(JobTitleException.class,
                () -> jobTitleService.partialUpdate(jobTitleDto.getId(), jobTitleDto));
        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    @DisplayName("delete jobTitle by id success")
    void deleteJobTitleSuccess() {
        when(jobTitleRepository.findById(any())).thenReturn(Optional.of(jobTitle));
        jobTitleService.deleteJobTitle(jobTitle.getId());
        verify(jobTitleRepository, times(1)).findById(jobTitle.getId());
        verify(jobTitleRepository, times(1)).delete(jobTitle);
    }

    @Test
    @DisplayName("delete jobTitle by id fail : id not found")
    void deleteJobTitleFail() {
        when(jobTitleRepository.findById(any())).thenThrow(new JobTitleException(exceptionMessage));
        JobTitleException exception = assertThrows(JobTitleException.class,
                () -> jobTitleService.deleteJobTitle(jobTitle.getId()));
        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    void searchJobTitles() {

        Page<JobTitle> jobTitlePage = new PageImpl<>(jobTitleList);
        JobTitleSearchDto searchDto = new JobTitleSearchDto();
        searchDto.setLimit(2);

        when(jobTitleRepository.findAll(ArgumentMatchers.<Specification<JobTitle>>any(), (Pageable) any())).thenReturn(jobTitlePage);
        when(jobTitleMapper.toDtos(jobTitlePage.getContent())).thenReturn(jobTitleDtoList);
        PaginatedResponseDto<JobTitleDto> allJobTitles = jobTitleService.findJobTitlesBySortingAndPagination(searchDto);
        verify(jobTitleRepository).findAll(ArgumentMatchers.<Specification<JobTitle>>any(), (Pageable) any());
        verify(jobTitleMapper, times(2)).toDtos(jobTitlePage.getContent());

        assertEquals(searchDto.getPage(), allJobTitles.getPage());
        assertEquals(searchDto.getDir().name(), allJobTitles.getSortDirection());
        assertEquals(searchDto.getSort(), allJobTitles.getSortingFields());
        assertEquals(searchDto.getLimit(), allJobTitles.getSize());
        assertEquals(jobTitleDtoList.size(), allJobTitles.getSize());
        assertEquals(jobTitleDtoList, allJobTitles.getData());
    }
}