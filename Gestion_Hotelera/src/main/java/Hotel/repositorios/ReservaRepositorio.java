package Hotel.repositorios;

import Hotel.entidades.Reserva;
import Hotel.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepositorio extends JpaRepository<Reserva, String> {

    @Query("SELECT i FROM Reserva i WHERE i.id = id")
    Reserva buscarPorID(@Param("id") String id);


    @Query("SELECT r FROM Reserva r WHERE r.usuario = usuario")
    Reserva buscarPorUsuario(@Param("usuario") Usuario usuario);
}
