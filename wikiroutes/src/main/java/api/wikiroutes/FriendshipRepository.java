package api.wikiroutes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

	// Busca rutas por id de ruta
	List<Friendship> findBySourceUserId(Long id);
}
