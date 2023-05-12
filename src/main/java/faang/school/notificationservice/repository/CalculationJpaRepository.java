package faang.school.notificationservice.repository;


import java.util.List;

import faang.school.notificationservice.model.CalculationJpa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculationJpaRepository extends CrudRepository<CalculationJpa, Integer> {
    @Override
    List<CalculationJpa> findAll();
}
