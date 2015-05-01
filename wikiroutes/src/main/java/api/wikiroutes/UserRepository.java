package api.wikiroutes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	// Busca usuarios por id
	User findById(Long id);
	
	// Busca usuarios por userName
	User findByUserName(String username);
	
	// Busca usuarios por apiKey
	User findByApiKey(String apiKey);
	
	// Busca usuarios por id de comentario
	User findByCommentsId(Long id);
	
}
