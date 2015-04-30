package api.wikiroutes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	// Busca comentarios por id de usuario
	List<Comment> findByUserId(Long id);
}
