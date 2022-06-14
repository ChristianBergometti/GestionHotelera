package Hotel.demo.servicios;

import Hotel.demo.entidades.Habitacion;
import Hotel.demo.entidades.Reserva;
import Hotel.demo.entidades.Usuario;
import Hotel.demo.errores.ErrorServicio;
import Hotel.demo.repositorios.HabitacionRepositorio;
import Hotel.demo.repositorios.ReservaRepositorio;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaServicio {

    @Autowired
    private ReservaRepositorio reservaRepositorio;

    @Autowired
    private HabitacionRepositorio habitacionRepositorio;

    @Transactional
    public void crearReserva(Double precio, Date ingreso, Date egreso, Usuario usuario, List<Habitacion> habitaciones, Integer personas) throws ErrorServicio {

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

    public void validarFechas(Date ingreso, Date egreso) throws ErrorServicio {
        Date hoy = new Date();

        if (ingreso.before(hoy) || egreso.before(hoy) || ingreso.equals(hoy) || egreso.equals(hoy)) {
            throw new ErrorServicio("La fechas no pueden ser anteriores al día de hoy.");
        }

        if (diferenciaDeDias(ingreso, egreso) < 2) {
            throw new ErrorServicio("La reserva no puede ser menor a 2 días de hospedaje.");
        }
    }

    public void validarCapacidad(Integer personas, List<Habitacion> habitaciones) throws ErrorServicio {

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

        return (diferenciaDeDias(ingreso, egreso) * precioTotal);
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

        if (reservas.isEmpty()) {
            throw new ErrorServicio("No existen reservas que mostrar.");
        } else {
            return reservas;
        }
    }

    public Long diferenciaDeDias(Date ingreso, Date egreso) {
        LocalDate ingresoHotel = ingreso.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate egresoHotel = egreso.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return DAYS.between(ingresoHotel, egresoHotel);
    }

    public Date convertirStringADate(String fechaString) throws ParseException {

        Date fechaDate = new SimpleDateFormat("yyyy-MM-dd").parse(fechaString);

        return fechaDate;
    }

    public List<List<Habitacion>> listarHabitacionesDisponibles(Date ingreso, Date egreso) throws ErrorServicio {
        //List<Habitacion> habitacionesDisponibles = habitacionRepositorio.listarPorPeriodo(ingreso, egreso);
        List<Habitacion> habitacionesDisponibles = habitacionRepositorio.findByAltaTrue();

        if (habitacionesDisponibles.isEmpty()) {
            throw new ErrorServicio("No hay habitaciones disponibles en el período seleccionado.");
        } else {

            List<Habitacion> habitacionesDobles = new ArrayList();
            List<Habitacion> habitacionesCuadruples = new ArrayList();
            List<Habitacion> habitacionesSextuples = new ArrayList();

            for (Habitacion habitacion : habitacionesDisponibles) {
                
                switch (habitacion.getCapacidad()) {
                    case 2:
                        habitacionesDobles.add(habitacion);
                        break;
                    case 4:
                        habitacionesCuadruples.add(habitacion);
                        break;
                    case 6:
                        habitacionesSextuples.add(habitacion);
                        break;
                }
            }
            
            List<List<Habitacion>> listaTotalHabitaciones = new ArrayList();
            listaTotalHabitaciones.add(0,habitacionesDobles);
            listaTotalHabitaciones.add(1,habitacionesCuadruples);
            listaTotalHabitaciones.add(2,habitacionesSextuples);
            
            
            return listaTotalHabitaciones;
        }
        
        
    }
}
