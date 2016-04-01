package ru.antonorlov.velm;

@Service
public class VelmParserImpl{
	
	
	 private static String[] urls = {
	 	"http://www.velomotors.ru/bicycles/bikes/mountain_bikes/",
	 	"http://www.velomotors.ru/bicycles/bikes/full_suspension_mountain/",
"http://www.velomotors.ru/bicycles/bikes/women_bikes/",
"http://www.velomotors.ru/bicycles/bikes/sity_tour_bikes/",
"http://www.velomotors.ru/bicycles/bikes/junior_bikes/",
"http://www.velomotors.ru/bicycles/bikes/folding_bikes/",
"http://www.velomotors.ru/bicycles/bikes/children_bikes/"
	 	};
	
	public void fillBikesWithParams(List<Bicycle> list){
		
		final Map<String, Bicycle> bicycleMap; 
		
		//todo: transform into map
				
		for(int i=0; i< urls.length; i++){
			final url = urls[i]; 
			
			//parse page with jsoup 
			
			//grab all product page links $(".photo a").attr("href")
			
			
			 // get table on page: catalog table tbody
			
		}
	}
	
	//todo: reusable component
	private Optional<Bicycle> findBikeByAlias(String bikeName){
		return Options.empty();
	}
	
	private Map<BicycleParamEnum,String> getParams(final Object paramTableNode){
		Map<BicycleParamEnum,String> params = new HashMap(); 
		//itegare over <TR> 
		//название параметра td:first-child
		//значние парам td::nth-child(2)
		
		return params; 
	}
}