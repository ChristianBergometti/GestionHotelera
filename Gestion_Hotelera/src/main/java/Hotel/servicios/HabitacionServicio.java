package Hotel.servicios;

import Hotel.entidades.Habitacion;
import Hotel.errores.ErrorServicio;
import Hotel.repositorios.HabitacionRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HabitacionServicio {
    
    @Autowired
    HabitacionRepositorio habitacionRepositorio;
    
    @Transactional
    public void altaBaja(Habitacion habitacion) {
        if(habitacion.getAlta() == true) {
            habitacion.setAlta(false);
        } else {
            habitacion.setAlta(true);
        }
        
        habitacionRepositorio.save(habitacion);
    }
    
    public List<Habitacion> listarHabitaciones() {
        return habitacionRepositorio.findAll();
    }
    
    public List<Habitacion> buscarPorPrecio(Double precio) {
        return habitacionRepositorio.habitacionesPorPrecio(precio);
    }
    
    public Habitacion buscarPorId(String id) throws ErrorServicio {
        Optional<Habitacion> habitacion = habitacionRepositorio.findById(id);
        
        if(habitacion.isPresent()) {
            return habitacion.get();
        } else {
            throw new ErrorServicio("No se ha encontrado habitación con el ID ingresado.");
        }
    }
    
}
