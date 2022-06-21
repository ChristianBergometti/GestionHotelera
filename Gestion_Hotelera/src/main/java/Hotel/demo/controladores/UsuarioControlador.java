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
@PreAuthorize("hasAnyRole('ROLE_USER')")
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    private ReservaServicio reservaServicio;
    @Autowired
    private HabitacionServicio habitacionServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/inicioOk")
    public String inicioOk(HttpSession session, ModelMap model) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        model.put("nombreUsuario", login.getNombre());
        if (login == null) {
            return "redirect:/login";// si pasa tiempo y no hace nada para vuelva a inicio
        }
        return "inicioOk";
    }

    @GetMapping("/index")
    public String indexLogueado(HttpSession session, ModelMap model) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/login";// si pasa tiempo y no hace nada para vuelva a inicio
        }
        model.put("nombreUsuario", login.getNombre());
        return "index";
    }

    @GetMapping("/editarPerfil")
    public String editarPerfil(HttpSession session, ModelMap model) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        model.put("edicion", "perfil");
        model.put("NombreCompleto", login.getNombre());
        model.put("CorreoElectronico", login.getEmail());
        model.put("DocumentoDeIdentidad", login.getDNI());
        model.put("nombreUsuario", login.getNombre());
        return "editarPerfil";
    }

    @PostMapping("/editaPersonal")
    public String editaPersonal(HttpSession session, ModelMap modelo, @RequestParam String NombreCompleto, @RequestParam String DocumentoDeIdentidad) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        modelo.put("nombreUsuario", login.getNombre());
        try {
            usuarioServicio.editarDatosPersonales(login.getId(), NombreCompleto, DocumentoDeIdentidad);
        } catch (ErrorServicio ex) {
            modelo.put("edicion", "perfil");
            modelo.put("error", ex.getMessage());
            modelo.put("NombreCompleto", NombreCompleto);
            modelo.put("DocumentoDeIdentidad", DocumentoDeIdentidad);
            return "editarPerfil";
        }
        modelo.put("edicion", "perfil");
        modelo.put("mensaje", "Datos modificados con éxito");
        modelo.put("NombreCompleto", NombreCompleto);
        modelo.put("DocumentoDeIdentidad", DocumentoDeIdentidad);
        return "editarPerfil";
    }

    @GetMapping("/editarClave")
    public String editarClave(HttpSession session, ModelMap modelo) {
        modelo.put("edicion", "clave");
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        modelo.put("NombreCompleto", login.getNombre());
        modelo.put("CorreoElectronico", login.getEmail());
        modelo.put("DocumentoDeIdentidad", login.getDNI());
        return "editarPerfil";
    }

    @PostMapping("/editaClave")
    public String editaClave(HttpSession session, ModelMap modelo, @RequestParam String Clave1, @RequestParam String Clave2) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        modelo.put("nombreUsuario", login.getNombre());
        try {
            usuarioServicio.editarClave(login.getId(), Clave1, Clave2);
        } catch (ErrorServicio ex) {
            modelo.put("edicion", "clave");
            modelo.put("error", ex.getMessage());
            return "editarPerfil";
        }
        modelo.put("nombreUsuario", login.getNombre());
        modelo.put("edicion", "clave");
        modelo.put("mensaje", "Datos modificados con éxito");
        return "editarPerfil";
    }

    @PostMapping("/confirmar")
    public String confirmar(ModelMap modelo, HttpSession session, @RequestParam Double pagar, @RequestParam String Checkin, @RequestParam String Checkout, @RequestParam Integer Habitacion2Personas,
            @RequestParam Integer Habitacion4Personas, @RequestParam Integer Habitacion6Personas, @RequestParam Integer CantidadPersonas) throws ParseException, ErrorServicio {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/login";
        } else {
            List<Object> fechas = reservaServicio.convertir2StringADates(Checkin, Checkout);
            List<List<Habitacion>> habitacionesDisponibles = reservaServicio.listarHabitacionesDisponibles((String) fechas.get(2), (String) fechas.get(3));
            List<Habitacion> habitacionesAReservar = habitacionServicio.crearListaHabitaciones(habitacionesDisponibles, Habitacion2Personas, Habitacion4Personas, Habitacion6Personas);
            reservaServicio.crearReserva(pagar, (Date) fechas.get(0), (Date) fechas.get(1), login, habitacionesAReservar, CantidadPersonas);

            modelo.addAttribute("exito", "La reserva ha sido realizada.");
            modelo.put("nombreUsuario", login.getNombre());
            return "reservaHotel";
        }
    }

    @GetMapping("/reservas")
    public String reservas(HttpSession session, ModelMap model) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        System.out.println(login.getNombre());
        if (login == null) {
            return "redirect:/login";// si pasa tiempo y no hace nada para vuelva a inicio
        }
        model.put("nombreUsuario", login.getNombre());
        return "reservaHotel";
    }

    @PostMapping("/fechasLogueado")
    public String fechasLogueado(HttpSession session, ModelMap modelo, @RequestParam String Checkin, @RequestParam String Checkout) throws ParseException {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        modelo.put("nombreUsuario", login.getNombre());
        if (login == null) {
            return "redirect:/login";// si pasa tiempo y no hace nada para vuelva a inicio
        }
        List<List<Habitacion>> habitacionesDisponibles;
        List<Object> fechas;
        try {
            fechas = reservaServicio.convertir2StringADates(Checkin, Checkout);
            reservaServicio.validarFechas((Date) fechas.get(0), (Date) fechas.get(1));
            habitacionesDisponibles = reservaServicio.listarHabitacionesDisponibles((String) fechas.get(2), (String) fechas.get(3));
        } catch (ErrorServicio ex) {
            modelo.addAttribute("error", ex.getMessage());
            modelo.addAttribute("nombreUsuario", login.getNombre());
            return "reservaHotel";
        } catch (ParseException ex) {
            modelo.addAttribute("error", "Colocar ambas fechas");
            modelo.addAttribute("nombreUsuario", login.getNombre());
            return "reservaHotel";
        }

        return reservas2Logueado(session, modelo, fechas, habitacionesDisponibles);
    }

    @GetMapping("/reservasLogueado")
    public String reservas2Logueado(HttpSession session, ModelMap modelo, List<Object> fechas, List<List<Habitacion>> habitacionesDisponibles) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/login";// si pasa tiempo y no hace nada para vuelva a inicio
        }
        modelo.put("nombreUsuario", login.getNombre());
        modelo.addAttribute("disponibles2", habitacionesDisponibles.get(0).size());
        modelo.addAttribute("disponibles4", habitacionesDisponibles.get(1).size());
        modelo.addAttribute("disponibles6", habitacionesDisponibles.get(2).size());
        modelo.addAttribute("CheckIn", (String) fechas.get(2));
        modelo.addAttribute("CheckOut", (String) fechas.get(3));
        modelo.addAttribute("cantidadDePersonas", habitacionServicio.calcularCantidadMaximaDePersonas(habitacionesDisponibles));

        return "reservaHotel2";
    }

    @PostMapping("/personasLogueado")
    public String personasLogueado(HttpSession session, ModelMap modelo, @RequestParam String Checkin, @RequestParam String Checkout,
            @RequestParam Integer CantidadPersonas, @RequestParam Integer Habitacion2Personas,
            @RequestParam Integer Habitacion4Personas, @RequestParam Integer Habitacion6Personas) throws ParseException, ErrorServicio {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/login";// si pasa tiempo y no hace nada para vuelva a inicio
        }
        modelo.put("nombreUsuario", login.getNombre());
        List<Object> fechas = reservaServicio.convertir2StringADates(Checkin, Checkout);
        try {

            List<List<Habitacion>> habitacionesDisponibles = reservaServicio.listarHabitacionesDisponibles((String) fechas.get(2), (String) fechas.get(3));
            List<Habitacion> habitacionesAReservar = habitacionServicio.crearListaHabitaciones(habitacionesDisponibles, Habitacion2Personas, Habitacion4Personas, Habitacion6Personas);
            reservaServicio.validarCapacidad(CantidadPersonas, habitacionesAReservar);
            return reservasFinalLogueado(session, modelo, fechas, CantidadPersonas, habitacionesAReservar, Habitacion2Personas, Habitacion4Personas, Habitacion6Personas);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            List<List<Habitacion>> habitacionesDisponibles = reservaServicio.listarHabitacionesDisponibles((String) fechas.get(2), (String) fechas.get(3));
            return reservas2Logueado(session, modelo, fechas, habitacionesDisponibles);
        }
    }

    @GetMapping("/reservasFinalLogueado")
    public String reservasFinalLogueado(HttpSession session, ModelMap model, List<Object> fechas, Integer CantidadPersonas, List<Habitacion> habitacionesAReservar, Integer Habitacion2Personas,
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

    @GetMapping("/habitaciones")
    public String habitaciones(HttpSession session, ModelMap model) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/login";// si pasa tiempo y no hace nada para vuelva a inicio
        }
        model.put("nombreUsuario", login.getNombre());
        return "Habitaciones";
    }

    @GetMapping("/ubicacion")
    public String ubicacion(HttpSession session, ModelMap model) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/login";// si pasa tiempo y no hace nada para vuelva a inicio
        }
        model.put("nombreUsuario", login.getNombre());
        return "ubicacion";
    }
    @GetMapping("/reservasHechas")
    public String reservasHechas(HttpSession session, ModelMap model) throws ErrorServicio {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/login";// si pasa tiempo y no hace nada para vuelva a inicio
        }
        model.put("nombreUsuario", login.getNombre());
        model.addAttribute("lista", reservaServicio.consultarReservasPorIdUsuario(login));
        return "usuarioReserva";
    }
 
}
