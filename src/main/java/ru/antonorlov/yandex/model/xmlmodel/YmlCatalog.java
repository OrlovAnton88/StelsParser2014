package ru.antonorlov.yandex.model;

/**
 * Example: https://yandex.st/market-export/1.0-17/partner/help/YML.xml
 */
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