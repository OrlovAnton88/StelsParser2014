package ru.antonorlov.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by antonorlov on 26/03/16.
 */
@Entity
public class PriceList {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    /**
     * Имя прайса
     */
    @Column(nullable = false)
    private String name;

    /**
     * Дата прайса
     */
    @Column(nullable = false)
    private String date;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Bicycle> bicycles;

    public PriceList() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Bicycle> getBicycles() {
        return bicycles;
    }

    public void setBicycles(List<Bicycle> bicycles) {
        this.bicycles = bicycles;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PriceList{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", date=").append(date);
        sb.append(", bicycles=").append(bicycles);
        sb.append('}');
        return sb.toString();
    }
}
