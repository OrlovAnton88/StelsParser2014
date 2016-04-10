package ru.antonorlov.veloline;

import ru.antonorlov.entities.Bicycle;

import java.util.List;

public interface VeloLineParser {

    /**
     * todo:
     * final all 2016 models http://www.velo-line.ru/index.php?searchstring=stels%202016&show_all=yes
     * add:
     * --link
     * --image
     * --state - есть ли на складе
     * price
     */
    void fillWithPublishInfo(final List<Bicycle> list);
}
