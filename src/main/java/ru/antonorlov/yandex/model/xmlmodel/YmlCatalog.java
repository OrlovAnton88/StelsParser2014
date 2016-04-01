package ru.antonorlov.yandex.model;

@JacksonXmlRootElement(localname="yml_catalog")
public class YmlCatalog{
	/**
	 * <yml_catalog date="2010-04-01 17:00">
	 */
	
	@JacksonXmlProperty(isAttribute = true)
	private Date date;
	@JacksonXmlProperty
	private Shop shop;
}