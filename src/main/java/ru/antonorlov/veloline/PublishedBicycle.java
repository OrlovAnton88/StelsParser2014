package ru.antonorlov.veloline;

/**
 * $("body>table>tbody>tr>td>div:nth-child(3)>table>tbody>tr>td:last-child")
 * 
 * td.hdbtop span.oki | span.error
 */
public class PublishedBicycle{
	
	/**
	 * $("div.boxleft a:last-child").text
	 */
	private String name; 
	
	private int price; 
	
	/**
	 * Наличие на складе
	 * "На складе:" 
	 * нет
	 * есть
	 * под заказ
	 * 
	 */
	private state;
	
	
	/**
	 * imboxr>a>img - exists 
	 * no image
	 * <img src="data/empty.gif" alt="no photo">
	 */
	private booleanImageExists; 
}