
package Hotel.demo.repositorios;

import Hotel.demo.entidades.Reserva;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepositorio extends JpaRepository<Reserva, String> {

    @Query("SELECT r FROM Reserva r WHERE r.id= :usuario_id")
    List<Reserva> buscarPorIdUsuario(@Param("usuario_id") String idUsuario);

}

