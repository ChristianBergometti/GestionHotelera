package Hotel.demo.controladores;

import Hotel.demo.entidades.Habitacion;
import Hotel.demo.entidades.Usuario;
import Hotel.demo.errores.ErrorServicio;
import Hotel.demo.servicios.HabitacionServicio;
import Hotel.demo.servicios.ReservaServicio;
import Hotel.demo.servicios.UsuarioServicio;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private ReservaServicio reservaServicio;
    @Autowired
    private HabitacionServicio habitacionServicio;

    @GetMapping("/")
    public String landing() {
        return "landingHotel.html";
    }

    @GetMapping("/index")
    public String index(ModelMap model) {
//        model.put("sinUser", true);
        return "index.html";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registroUsuarioHotel";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model) {
        if (error != null) {
            model.put("error", "Nombre de usuario o clave incorrectos.");
        }
        return "loginHotel";
    }

    @PostMapping("/registrar")
    public String registrar(ModelMap modelo, @RequestParam String NombreCompleto, @RequestParam String CorreoElectronico, @RequestParam String DocumentoDeIdentidad, @RequestParam String Clave1, @RequestParam String Clave2) {
        try {
            usuarioServicio.crearUsuario(NombreCompleto, DocumentoDeIdentidad, CorreoElectronico, Clave1, Clave2);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("NombreCompleto", NombreCompleto);
            modelo.put("CorreoElectronico", CorreoElectronico);
            modelo.put("DocumentoDeIdentidad", DocumentoDeIdentidad);
            return "registroUsuarioHotel";
        }
        modelo.put("mensaje", "Editorial cargada con éxito");
        return "index.html";
    }

    @GetMapping("/habitaciones")
    public String habitaciones(@RequestParam(required = false) String error, ModelMap model) {
        return "Habitaciones";
    }



    @PostMapping("/fechas")
    public String fechas(ModelMap modelo, @RequestParam String Checkin, @RequestParam String Checkout) throws ParseException {

        List<List<Habitacion>> habitacionesDisponibles;
        List<Object> fechas;
        try {
            fechas = reservaServicio.convertir2StringADates(Checkin, Checkout);
            reservaServicio.validarFechas((Date) fechas.get(0), (Date) fechas.get(1));
            habitacionesDisponibles = reservaServicio.listarHabitacionesDisponibles((Date) fechas.get(0), (Date) fechas.get(1));
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "reservaHotel";
        } catch (ParseException ex) {
            modelo.put("error", "Colocar ambas fechas");
            return "reservaHotel";
        }

        return reservas2(modelo, fechas, habitacionesDisponibles);
    }

    @GetMapping("/reservas2")
    public String reservas2(ModelMap modelo, List<Object> fechas, List<List<Habitacion>> habitacionesDisponibles) {
        modelo.addAttribute("disponibles2", habitacionesDisponibles.get(0).size());
        modelo.addAttribute("disponibles4", habitacionesDisponibles.get(1).size());
        modelo.addAttribute("disponibles6", habitacionesDisponibles.get(2).size());
        modelo.addAttribute("CheckIn", (String) fechas.get(2));
        modelo.addAttribute("CheckOut", (String) fechas.get(3));
        modelo.addAttribute("cantidadDePersonas", habitacionServicio.calcularCantidadMaximaDePersonas(habitacionesDisponibles));

        return "reservaHotel2";
    }

    @PostMapping("/personas")
    public String personas(ModelMap modelo, @RequestParam String Checkin, @RequestParam String Checkout,
            @RequestParam Integer CantidadPersonas, @RequestParam Integer Habitacion2Personas,
            @RequestParam Integer Habitacion4Personas, @RequestParam Integer Habitacion6Personas) throws ParseException, ErrorServicio {

        List<Object> fechas = reservaServicio.convertir2StringADates(Checkin, Checkout);
        try {

            List<List<Habitacion>> habitacionesDisponibles = reservaServicio.listarHabitacionesDisponibles((Date) fechas.get(0), (Date) fechas.get(1));
            List<Habitacion> habitacionesAReservar = habitacionServicio.crearListaHabitaciones(habitacionesDisponibles, Habitacion2Personas, Habitacion4Personas, Habitacion6Personas);
            reservaServicio.validarCapacidad(CantidadPersonas, habitacionesAReservar);
            return reservasFinal(modelo, fechas, CantidadPersonas, habitacionesAReservar, Habitacion2Personas, Habitacion4Personas, Habitacion6Personas);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            List<List<Habitacion>> habitacionesDisponibles = reservaServicio.listarHabitacionesDisponibles((Date) fechas.get(0), (Date) fechas.get(1));
            return reservas2(modelo, fechas, habitacionesDisponibles);
        }
    }

    @GetMapping("/reservasFinal")
    public String reservasFinal(ModelMap model, List<Object> fechas, Integer CantidadPersonas, List<Habitacion> habitacionesAReservar, Integer Habitacion2Personas,
            Integer Habitacion4Personas, Integer Habitacion6Personas) {
        model.addAttribute("CheckIn", (String) fechas.get(2));
        model.addAttribute("CheckOut", (String) fechas.get(3));
        model.addAttribute("CantidadPersonas", CantidadPersonas);
        model.addAttribute("pagar", reservaServicio.calcularHospedaje(habitacionesAReservar, (Date) fechas.get(0), (Date) fechas.get(1)));
        model.addAttribute("habitacion2", Habitacion2Personas);
        model.addAttribute("habitacion4", Habitacion4Personas);
        model.addAttribute("habitacion6", Habitacion6Personas);
        return "reservafinal";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/confirmar")
    public String confirmar(ModelMap modelo, HttpSession session, @RequestParam Double pagar, @RequestParam String Checkin, @RequestParam String Checkout, @RequestParam Integer Habitacion2Personas,
            @RequestParam Integer Habitacion4Personas, @RequestParam Integer Habitacion6Personas, @RequestParam Integer CantidadPersonas) throws ParseException, ErrorServicio {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "login";
        } else {
            List<Object> fechas = reservaServicio.convertir2StringADates(Checkin, Checkout);
            List<List<Habitacion>> habitacionesDisponibles = reservaServicio.listarHabitacionesDisponibles((Date) fechas.get(0), (Date) fechas.get(1));
            List<Habitacion> habitacionesAReservar = habitacionServicio.crearListaHabitaciones(habitacionesDisponibles, Habitacion2Personas, Habitacion4Personas, Habitacion6Personas);

            reservaServicio.crearReserva(pagar, (Date) fechas.get(0), (Date) fechas.get(1), login, habitacionesAReservar, CantidadPersonas);

            modelo.addAttribute("exito", "La reserva ha sido realizada.");

            return "reservaHotel";
        }

    }
}
