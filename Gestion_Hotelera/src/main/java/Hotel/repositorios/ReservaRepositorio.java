package Hotel.repositorios;

import Hotel.entidades.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepositorio extends JpaRepository<Reserva, String> {


    @Query("SELECT r FROM Reserva r WHERE r.usuario_id= :usuario_id")
    Reserva buscarPorIdUsuario(@Param("usuario_id") String idUsuario);
}
