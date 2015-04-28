package api.wikiroutes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {

	// Busca rutas por id de usuario
	List<Route> findByUserId(Long id);
	
	// Busca ruta públicas o privadas
	List<Route> findByIsPrivate(boolean isPrivate);
}

