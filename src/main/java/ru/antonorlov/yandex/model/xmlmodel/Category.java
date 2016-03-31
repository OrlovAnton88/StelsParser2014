package ru.antonorlov.yandex.model;

/**
 * 		<category id="10" parentId="1">Принтеры</category>
 */
public class Category {
	
	@NotNull
		  @JacksonXmlProperty(isAttribute=true)
	private int id; 
	@NotNull
		  @JacksonXmlProperty
	private String name;
	@Nullable
		  @JacksonXmlProperty(isAttribute=true)
	private int	parentId;
}