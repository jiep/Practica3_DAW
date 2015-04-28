package api.wikiroutes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
	
	// Busca rutas por id de usuario
	List<Point> findByStretchId(Long id);
}
