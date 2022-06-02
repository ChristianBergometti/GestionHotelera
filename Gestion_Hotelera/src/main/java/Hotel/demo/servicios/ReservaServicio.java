package Hotel.demo.servicios;

import Hotel.demo.entidades.Habitacion;
import Hotel.demo.entidades.Reserva;
import Hotel.demo.entidades.Usuario;
import Hotel.demo.errores.ErrorServicio;
import Hotel.demo.repositorios.ReservaRepositorio;
import java.time.LocalDate;
import java.time.ZoneId;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaServicio {
    
    @Autowired
    private ReservaRepositorio reservaRepositorio;
    
    @Transactional
    public void crearReserva(Double precio, Date ingreso, Date egreso, Usuario usuario, List<Habitacion> habitaciones, Integer personas) throws ErrorServicio {
        validar(ingreso, egreso, personas, habitaciones);
        
        Reserva reserva = new Reserva();
        reserva.setIngreso(ingreso);
        reserva.setEgreso(egreso);
        reserva.setUsuario(usuario);
        reserva.setHabitaciones(habitaciones);
        reserva.setPersonas(personas);
        reserva.setAlta(true);
        reserva.setPrecio(calcularHospedaje(habitaciones, ingreso, egreso));
        
        reservaRepositorio.save(reserva);
    }
    
    public void validar(Date ingreso, Date egreso, Integer personas, List<Habitacion> habitaciones) throws ErrorServicio {
        
        if (egreso == ingreso) {
            throw new ErrorServicio("El check-in no puede ser igual al check-out.");
        }
        
        if (diferenciaDeDias(ingreso, egreso) < 2) {
            throw new ErrorServicio("La reserva no puede ser menor a 2 días de hospedaje.");
        }
        
        Integer capacidadTotal = 0;
        
        for (Habitacion habitacion : habitaciones) {
          capacidadTotal += habitacion.getCapacidad();
        }
        
        if (personas > capacidadTotal) {
            throw new ErrorServicio("La cantidad de personas ingresadas es mayor a la capacidad de las habitaciones seleccionadas.");
        }
    }
    
    public Double calcularHospedaje(List<Habitacion> habitaciones, Date ingreso, Date egreso) {
        
        Double precioTotal = 0d;
        
        for (Habitacion habitacion : habitaciones) {
            precioTotal += habitacion.getPrecio();
        }
        
        return ((egreso.getDate()-ingreso.getDate())*precioTotal);
    }
    
    @Transactional
    public void baja(Reserva reserva) {
        reserva.setAlta(false);
        
        reservaRepositorio.save(reserva);
    }
    
    @Transactional(readOnly = true)
    public List<Reserva> listarReservas() {
        return reservaRepositorio.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Reserva> consultarReservasPorIdUsuario(Usuario usuario) throws ErrorServicio {
        List<Reserva> reservas = reservaRepositorio.buscarPorIdUsuario(usuario.getId());
        
        if(reservas.isEmpty()) {
            throw new ErrorServicio("No existen reservas que mostrar.");
        } else {
            return reservas;
        }
    }
    
    public Long diferenciaDeDias(Date ingreso, Date egreso){
        LocalDate ingresoHotel = ingreso.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        LocalDate egresoHotel = egreso.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        return DAYS.between(ingresoHotel, egresoHotel);
    }
}
