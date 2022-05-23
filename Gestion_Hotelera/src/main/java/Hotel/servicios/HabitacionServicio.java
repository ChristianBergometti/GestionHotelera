package Hotel.servicios;

import Hotel.entidades.Habitacion;
import Hotel.errores.ErrorServicio;
import Hotel.repositorios.HabitacionRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    @Transactional(readOnly = true)
    public List<Habitacion> listarHabitaciones() {
        return habitacionRepositorio.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Habitacion> buscarPorPrecio(Double precio) {
        return habitacionRepositorio.habitacionesPorPrecio(precio);
    }
    
    @Transactional(readOnly = true)
    public Habitacion buscarPorId(String id) throws ErrorServicio {
        Optional<Habitacion> habitacion = habitacionRepositorio.findById(id);
        
        if(habitacion.isPresent()) {
            return habitacion.get();
        } else {
            throw new ErrorServicio("No se ha encontrado habitación con el ID ingresado.");
        }
    }
    
}
