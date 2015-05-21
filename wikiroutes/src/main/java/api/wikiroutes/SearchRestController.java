package api.wikiroutes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.wikiroutes.TypeRoute.Type;

@RestController
@RequestMapping("/search")
public class SearchRestController {
	@Autowired
	RouteRepository routes;
	
	@RequestMapping
	public List<Route> getRoutesByName(@RequestParam("name") String name){
		return routes.findByNameContainingAndIsPrivate(name,false);
	}
	
	@RequestMapping("/category/{route_type}")
	public List<Route> findByType(Type type, boolean isPrivate, @PathVariable String route_type){
		
		Type t = Type.WALK;		
		switch(route_type){
		case "walk" : t = Type.WALK; break;
		case "bike" : t = Type.BIKE; break;
		case "horse" : t = Type.HORSE; break;
		case "boat" : t = Type.BOAT; break;
		}
		
		
		return routes.findByTypeAndIsPrivate(t, false);
		
	}
	
}
