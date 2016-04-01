package ru.antonorlov.yandex.model;

/**
 * <currency id="RUR" rate="1" plus="0"/>
 */
public class Currency {
	  @JacksonXmlProperty(isAttribute=true)
	private String id;
	  @JacksonXmlProperty(isAttribute=true)
	private int rate; 
	  @JacksonXmlProperty(isAttribute=true)
	private int plus;
	
	
	public Currency static getRur(){
		return new Currency("RUR",1,0);
	}
}