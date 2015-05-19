package api.wikiroutes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchRestController {
	@Autowired
	RouteRepository routes;
	
	@RequestMapping
	public List<Route> getRoutesByName(@RequestParam("name") String name){
		return routes.findByNameContainingAndIsPrivate(name,false);
	}
	
}
