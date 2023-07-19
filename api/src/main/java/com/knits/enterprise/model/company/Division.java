package com.knits.enterprise.model.company;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder=true)
@Table(name = "division")
public class Division extends AbstractOrganizationStructure implements Serializable {

    @OneToMany(mappedBy = "division")
    private List<Employee> employees;

}
