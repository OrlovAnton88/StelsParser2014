package ru.antonorlov.yandex;


public interface YandexYmlService{
	
	YmlCatalog getUml(final ShopInfo shopInfo, final PublishedBicycle publBicycle);
	
	void publish(final YmlCatalog catalog); 
}