package ru.antonorlov.yandex.repositiry;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface PublishedProductRepository  extends PublishedProductRepository<PublishedProduct, Integer> {
}