package ru.antonorlov.yandex.repositiry;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface ProductSetRepository  extends JpaRepository<ProductSet, Integer> {
}