package ru.antonorlov.service;

import ru.antonorlov.entities.PriceList;

import java.util.List;
import java.util.Optional;

/**
 * Created by antonorlov on 28/03/16.
 */
public interface PriceListService {

    void  savePriceList(final PriceList priceList);

    List<PriceList> gerAllPriceLists();

    Optional<PriceList> getPriceList(final Integer id);
}
