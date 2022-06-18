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

}
