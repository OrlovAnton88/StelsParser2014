package ru.antonorlov.entities;


public class ModelAlias{
	
@Id
private Integer id; 

@Column
@Enumerated(value = EnumType.STRING)
private AliasTypeEnum type; 

@Column
private String value; 
}


}