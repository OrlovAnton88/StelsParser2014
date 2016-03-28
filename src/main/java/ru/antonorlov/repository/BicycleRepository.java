package ru.antonorlov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.antonorlov.entities.Bicycle;

/**
 * Created by antonorlov on 28/03/16.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface BicycleRepository extends JpaRepository<Bicycle, Integer> {
}
