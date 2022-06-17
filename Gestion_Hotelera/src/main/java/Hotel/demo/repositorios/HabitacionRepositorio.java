package Hotel.demo.repositorios;

import Hotel.demo.entidades.Habitacion;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitacionRepositorio extends JpaRepository<Habitacion, String> {

    @Query("SELECT c FROM Habitacion c WHERE c.numero = :numeroHabitacion")
    public List<Habitacion> habitacionPorNumero(@Param("numeroHabitacion") String numeroHabitacion);

    @Query("SELECT c FROM Habitacion c WHERE c.precio = :precioHabitacion")
    public List<Habitacion> habitacionesPorPrecio(@Param("precioHabitacion") Double precio);

    public List<Habitacion> findByAltaTrue();
    
    /*Necesitamos que la query 
    1. Descarte las habitaciones relacionadas con una Reserva que coincida con las fechas de ingreso y
    egreso.
    2. Descarte las habitaciones cuya alta esté seteada en false
    3. Liste el resto de las habitaciones
    
    @Query(value = "SELECT * FROM usuario WHERE email = :email", nativeQuery = true);*/
    
    @Query(value = "SELECT * FROM habitacion h INNER JOIN reserva r ON r.h.id = h.id"
            + " WHERE((:ingreso NOT BETWEEN r.ingreso AND r.egreso AND "
            + "(:egreso NOT BETWEEN r.ingreso AND r.egreso) AND "
            + "(:ingreso NOT LIKE r.ingreso AND r.egreso) AND "
            + "(:egreso NOT LIKE r.ingreso AND r.egreso))AND "
            + "(h.alta LIKE true)", nativeQuery = true) 
    public List<Habitacion> listarPorPeriodo(@Param("ingreso") Date ingreso, @Param("egreso") Date egreso);
}
