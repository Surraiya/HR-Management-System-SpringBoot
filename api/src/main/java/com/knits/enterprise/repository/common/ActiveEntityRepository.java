package com.knits.enterprise.repository.common;

import com.knits.enterprise.model.common.AbstractActiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@NoRepositoryBean
public interface ActiveEntityRepository <T extends AbstractActiveEntity> extends JpaRepository <T,Long>, JpaSpecificationExecutor<T> {

    @Override
    @Transactional
    @Modifying
    @Query("update #{#entityName} t SET active = false where t.id =:id")
    void deleteById(@Param("id") Long id);

    @Override
    @Transactional
    @Modifying
    @Query("update #{#entityName} e SET active = false where e.id IN (:ids)")
    void deleteAllByIdInBatch(@Param("ids") Iterable<Long> ids);


    @Override
    @Transactional
    default void delete (T entity){

        String entityClassName =entity.getClass().getName();
        Objects.requireNonNull(entity, String.format("Entity from %s  cant be null",entityClassName));

        T entityToDelete =findById(entity.getId()).orElseThrow(() ->
                new RuntimeException(String.format("Entity from class %s and id %s Nout found",entityClassName,entity.getId())));
        entityToDelete.setActive(false);
        save(entityToDelete);
    }

    @Override
    @Query("select t from #{#entityName} t where t.id IN (:ids) AND t.active = true")
    List<T> findAllById(@Param("ids") Iterable<Long> ids);

    @Override
    @Query("select t from #{#entityName} t where t.id =:id and t.active = true")
    Optional<T> findById (@Param("id")  Long id);


    @Override
    @Transactional
    default void deleteAllInBatch(Iterable<T> entities) {
        entities.forEach(t -> delete(t));
    }

    @Override
    @Query("select count(t) from #{#entityName} t where t.active = true")
    long count();

    @Override
    default boolean existsById(Long id){
        return findById(id).isPresent();
    }

    @Override
    default List<T> findAll(){
        throw new UnsupportedOperationException("Provide 2 different implementations for active and all. Include in query all minimal fetches on entities");
    }
    @Query("select t from #{#entityName} t where t.active = true")
    List<T> findAllActive();
}
