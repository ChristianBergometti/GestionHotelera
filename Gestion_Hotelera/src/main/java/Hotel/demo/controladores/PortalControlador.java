package Hotel.demo.controladores;

import Hotel.demo.entidades.Habitacion;
import Hotel.demo.entidades.Usuario;
import Hotel.demo.errores.ErrorServicio;
import Hotel.demo.servicios.HabitacionServicio;
import Hotel.demo.servicios.ReservaServicio;
import Hotel.demo.servicios.UsuarioServicio;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/reservas")
    public String reservas(ModelMap model) {
        return "reservaHotel";
    }

    @PostMapping("/fechas")
    public String fechas(HttpSession session, ModelMap modelo, @RequestParam String Checkin, @RequestParam String Checkout, RedirectAttributes redirectAttrs) throws ParseException {
        Date ingreso = new Date();
        Date egreso = new Date();

        try {
            ingreso = reservaServicio.convertirStringADate(Checkin);
            egreso = reservaServicio.convertirStringADate(Checkout);
            reservaServicio.validarFechas(ingreso, egreso);
        } catch (ErrorServicio ex) {
            modelo.put("error", "Verificar que las fechas sean correctas");
            return "reservaHotel";
        } catch (ParseException ex) {
            modelo.put("error", "Colocar ambas fechas");
            return "reservaHotel";
        }

        return reservas2(modelo, Checkin, Checkout);
    }

    @GetMapping("/reservas2")
    public String reservas2(ModelMap model, String ingreso, String egreso) {

        model.addAttribute("CheckIn", ingreso);
        model.addAttribute("CheckOut", egreso);

        return "reservaHotel2";
    }

    @PostMapping("/personas")
    public String personas(ModelMap modelo, @RequestParam String Checkin, @RequestParam String Checkout,
            @RequestParam Integer CantidadPersonas, @RequestParam Integer Habitacion2Personas, 
            @RequestParam Integer Habitacion4Personas, @RequestParam Integer Habitacion6Personas){
        try {
                        
            reservaServicio.validarCapacidad(CantidadPersonas, habitacionesAReservar);
        } catch (ErrorServicio ex) {
            modelo.put("error", "Verificar que las fechas sean correctas");
            return "reservaHotel2";
        }

        return reservas2(modelo, Checkin, Checkout);
    }

}
