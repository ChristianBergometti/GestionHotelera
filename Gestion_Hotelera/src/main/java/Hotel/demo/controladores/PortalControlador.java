package Hotel.demo.controladores;

import Hotel.demo.errores.ErrorServicio;
import Hotel.demo.servicios.UsuarioServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @GetMapping("/")
    public String landing() {
        return "landingHotel.html";
    }
    
    @GetMapping("/index")
    public String index() {
        return "index.html";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registroUsuarioHotel";
    }

    @GetMapping("/login")
    public String login() {
        return "loginHotel";
    }

    @PostMapping("/registrar")
    public String registrar(ModelMap modelo, @RequestParam String NombreCompleto, @RequestParam String CorreoElectronico, @RequestParam String DocumentoDeIdentidad, @RequestParam String Clave1, @RequestParam String Clave2) {
        try {
            usuarioServicio.crearUsuario(NombreCompleto, CorreoElectronico, DocumentoDeIdentidad, Clave1, Clave2);
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
}
