package ru.antonorlov.yandex.model;

public class Offer{
	
	/**
	 * Attribute
	 */
	   @JacksonXmlProperty(isAttribute = true)
	private int id; 
	
	/**
	 * Attribute
	 * Произвольный товар (vendor.model)
	 * type="vendor.model" 
	 */
	   @JacksonXmlProperty(isAttribute = true)
	private String type; 
	
	/**
	 * Attribute
	 */
	   @JacksonXmlProperty(isAttribute = true)
	private boolean available
	  @JacksonXmlProperty
	private int price;
	  @JacksonXmlProperty
	private Currency currency; 
	  @JacksonXmlProperty
	private Category category;
	
	/**
	 * Элемент, отражающий возможность доставки соответствующего товара.
«false» — товар не может быть доставлен («самовывоз»).
«true» — доставка товара осуществлятся в регионы, указанные во вкладке «Магазин» 
в разделе «Товары и цены». Стоимость доставки описывается в теге <local_delivery_cost>.
	 */
		  @JacksonXmlProperty
	private boolean delivery;
	
		  @JacksonXmlProperty
	private String vendor;
	
	
		  @JacksonXmlProperty
	private String model;
	
		  @JacksonXmlProperty
	private String  description;
	
	/**
	 * Стоимость доставки данного товара в своем регионе. xml : local_delivery_cost
	 */
		  @JacksonXmlProperty
	private int localDeliveryCost;
	
	
	/**
	 * manufacturer_warranty
	 */
		  @JacksonXmlProperty
	private boolean manufacturerWarranty;
	
	
	/**
	 * country_of_origin
	 * add Россия если получится
	 */
		  @JacksonXmlProperty
	private String countryOfOrigin;
	
	/**
	 * Todo: check if works
	 */
		  @JacksonXmlProperty
	private String picture;
}
