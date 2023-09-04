package com.knits.enterprise.model.company;

import com.knits.enterprise.model.common.AbstractActiveEntity;
import com.knits.enterprise.model.security.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder=true)
public abstract class AbstractOrganizationStructure extends AbstractActiveEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
}
