package Hotel.demo.servicios;

import Hotel.demo.entidades.Habitacion;
import Hotel.demo.errores.ErrorServicio;
import Hotel.demo.repositorios.HabitacionRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HabitacionServicio {
    
    @Autowired
    private HabitacionRepositorio habitacionRepositorio;
    
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
    
    public List<Habitacion> crearListaHabitaciones(List<List<Habitacion>> listaDeListasHabitacion, Integer habitacion2Personas, Integer habitacion4Personas, Integer habitacion6Personas) {
        
        List<Habitacion> habitaciones = new ArrayList();
        
        for (int i = 0; i < habitacion2Personas; i++) {
            habitaciones.add(listaDeListasHabitacion.get(0).get(i));
        }
        
        for (int i = 0; i < habitacion4Personas; i++) {
            habitaciones.add(listaDeListasHabitacion.get(1).get(i));
        }
        
        for (int i = 0; i < habitacion6Personas; i++) {
            habitaciones.add(listaDeListasHabitacion.get(2).get(i));
        }
        
        return habitaciones;
    }
    
    public Integer calcularCantidadMaximaDePersonas(List<List<Habitacion>> listaDeListasHabitacion) {
        Integer capacidadMaxima = 0;
        
        for (List<Habitacion>  lista : listaDeListasHabitacion) {
            for (Habitacion habitacion : lista) {
                capacidadMaxima += habitacion.getCapacidad();
            }
        }
        
        return capacidadMaxima;
    }
}
