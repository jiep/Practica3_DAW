package api.wikiroutes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/routes")
public class RouteRestController {

	
	@Autowired
	RouteRepository routes;
	
	@RequestMapping
	public List<Route> getRoutes(){
		return routes.findByIsPrivate(false);
	}
	

	
}
