package ru.antonorlov.yandex;

public class PublishedBicycle{
	
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
	
	@Column
	private String url; 
	
	@OneToOne
	private Bicycle bicycle;
	
	
}