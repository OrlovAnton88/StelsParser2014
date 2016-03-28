package ru.antonorlov.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.antonorlov.entities.PriceList;
import ru.antonorlov.repository.PriceListRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by antonorlov on 28/03/16.
 */
@Service
public class PriceListServiceImpl implements PriceListService {

    private static final Logger LOGGER = LogManager.getLogger(PriceListServiceImpl.class.getName());


    @Autowired
    private PriceListRepository repository;

    @Transactional
    @Override
    public void savePriceList(PriceList priceList) {
        LOGGER.info("Saving price list");
        repository.save(priceList);
        LOGGER.info("DONE");
    }

    @Transactional
    @Override
    public List<PriceList> gerAllPriceLists() {
        LOGGER.info("Getting all price lists");
        List<PriceList> all = repository.findAll();
        LOGGER.info(all.size() + " price lists found");
        return all;
    }

    @Transactional
    @Override
    public Optional<PriceList> getPriceList(Integer id) {
        LOGGER.info("Getting price lists with id [" + id + "]");
        PriceList one = repository.findOne(id);

        if (one != null) {
            LOGGER.info("FOUND");
            return Optional.of(one);
        } else {
            LOGGER.warn("NOT FOUND");
            return Optional.empty();
        }
    }
}
