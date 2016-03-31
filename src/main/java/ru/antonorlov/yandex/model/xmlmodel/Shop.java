package ru.antonorlov.yandex.model;

public class Shop {
	
	  @JacksonXmlProperty
	private String name;
	  @JacksonXmlProperty
	private String company;
  @JacksonXmlProperty
	private String url;
	
	/**
	 * local_delivery_cost
	 */
	  @JacksonXmlProperty
	private localDeliveryCost;
	  @JacksonXmlProperty(localname="category")
	  @XmlElementWrapper(localname="categories")
	private List<Category> categories;
	
	  @JacksonXmlProperty(localname="currency")
	  @XmlElementWrapper(localname="currencies")
	private List<Currency> currencies;
	
	  @JacksonXmlProperty(localname="offer")
	  @XmlElementWrapper(localname="offers")
	private List<Offer> offers;
	
	

}