import org.springframework.data.jpa.repository.JpaRepository;

import com.ramon.gerenciadorfinancas.model.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
