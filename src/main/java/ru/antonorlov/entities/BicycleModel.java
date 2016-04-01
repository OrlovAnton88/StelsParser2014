package ru.antonorlov.entities;

public class BicycleModel {
	//todo: add to base2016 csv "Navigator-530 V 26"".16";Navigator 530 2016;26
@Id
private Integer id; 

@OneToMany
private List<Alias> aliases; 

@Column
private Year year;

}