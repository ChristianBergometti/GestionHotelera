package Hotel.repositorios;

import Hotel.entidades.Habitacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HabitacionRepositorio extends JpaRepository<Habitacion, String> {

    @Query("SELECT c FROM Habitacion c WHERE c.numero = :numeroHabitacion")
    public List<Habitacion> habitacionPorNumero(@Param("numeroHabitacion") String numeroHabitacion);
    
    @Query("SELECT c FROM Habitacion c WHERE c.precio = :precioHabitacion")
    public List<Habitacion> habitacionesPorPrecio(@Param("precioHabitacion") Double precio);
    
}
