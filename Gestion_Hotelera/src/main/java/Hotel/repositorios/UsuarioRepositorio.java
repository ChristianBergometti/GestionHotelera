package Hotel.repositorios;

import Hotel.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {


    @Query("SELECT n FROM Usuario n WHERE n.nombre = :nombre")
    Usuario buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT e FROM Usuario e WHERE e.email = :email")
    Usuario buscarPorEmail(@Param("email") String email);

    @Query("SELECT d FROM Usuario d WHERE d.DNI = :DNI")
    Usuario buscarPorDNI(@Param("DNI") String DNI);

}
