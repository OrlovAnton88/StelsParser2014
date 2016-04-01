package ru.antonorlov.yandex;

/**
 * Набор товаров для создания uml
 */
@Entity
public class ProductSet{
	
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<PublichedBicycle> list; 
	
}