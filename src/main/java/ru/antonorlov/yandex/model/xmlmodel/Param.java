package ru.antonorlov.yandex.model;

/**
 * Example <param name="Максимальный формат">А4</param>
 */
public class Param{
	
	  @JacksonXmlProperty(isAttribute=true)
	private String name; 

	  @JacksonXmlProperty	
	private String value;

}