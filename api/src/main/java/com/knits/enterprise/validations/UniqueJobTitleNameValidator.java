package com.knits.enterprise.validations;

import com.knits.enterprise.repository.company.JobTitleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueJobTitleNameValidator implements ConstraintValidator<UniqueJobTitleName, String> {

    @Autowired
    private JobTitleRepository jobTitleRepository;

    @Override
    public void initialize(UniqueJobTitleName constraintAnnotation){
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context){
        if(name == null){
            return false;
        }
        return jobTitleRepository.findByName(name) == null;
    }
}
