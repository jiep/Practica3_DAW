package api.wikiroutes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {

	// Busca rutas por id de usuario
	List<Route> findByUserId(Long id);
	
	// Busca rutas públicas o privadas
	List<Route> findByIsPrivate(boolean isPrivate);
	
	// Busca rutas id de comentario 
	Route findByCommentsId(Long id);
	
	// Busca rutas por id de usuario y por privacidad
	List<Route> findByUserIdAndIsPrivate(Long id, boolean isPrivate);
}

