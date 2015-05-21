package api.wikiroutes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import api.wikiroutes.TypeRoute.Type;

public interface RouteRepository extends JpaRepository<Route, Long> {

	// Busca rutas por id de usuario
	List<Route> findByUserId(Long id);
	
	// Busca rutas públicas o privadas
	List<Route> findByIsPrivate(boolean isPrivate);
	
	// Busca rutas id de comentario 
	Route findByCommentsId(Long id);
	
	// Busca rutas por id de usuario y por privacidad
	List<Route> findByUserIdAndIsPrivate(Long id, boolean isPrivate);
	
	// Busca rutas por nombre de ruta y que sean publicas
	List<Route> findByNameContainingAndIsPrivate(String name, boolean isPrivate);
	
	// Busca rutas por tipo y por privacidad(público/privado)
	List<Route> findByTypeAndIsPrivate(Type type, boolean isPrivate);
	
	// Busca rutas por id y por privacidad(público/privado)
	Route findByIdAndIsPrivate(Long id, boolean isPrivate);
}

