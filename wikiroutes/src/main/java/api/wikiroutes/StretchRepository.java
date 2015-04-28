package api.wikiroutes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StretchRepository extends JpaRepository<Stretch, Long> {

	// Busca rutas por id de ruta
	List<Stretch> findByRouteId(Long id);
}
