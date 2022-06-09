package Hotel.demo.controladores;

import Hotel.demo.entidades.Usuario;
import Hotel.demo.errores.ErrorServicio;
import Hotel.demo.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
    private UsuarioServicio usuarioServicio;

    @GetMapping("/inicioOk")
    public String inicioOk(HttpSession session) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/login";// si pasa tiempo y no hace nada para vuelva a inicio
        }
        return "inicioOk";
    }

    @GetMapping("/index_logueado")
    public String index_logueado(HttpSession session) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/login";// si pasa tiempo y no hace nada para vuelva a inicio
        }
        return "index_logueado";
    }

    @GetMapping("/editarPerfil")
    public String editarPerfil(HttpSession session, ModelMap model) {
        model.put("edicion", "perfil");
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        model.put("NombreCompleto", login.getNombre());
        model.put("CorreoElectronico", login.getEmail());
        model.put("DocumentoDeIdentidad", login.getDNI());
        return "editarPerfil";
    }

    @PostMapping("/editaPersonal")
    public String editaPersonal(HttpSession session, ModelMap modelo, @RequestParam String NombreCompleto, @RequestParam String DocumentoDeIdentidad) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
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
        try {
            usuarioServicio.editarClave(login.getId(), Clave1,Clave2);
        } catch (ErrorServicio ex) {
            modelo.put("edicion", "clave");
            modelo.put("error", ex.getMessage());
            return "editarPerfil";
        }
        modelo.put("edicion", "clave");
        modelo.put("mensaje", "Datos modificados con éxito");
        return "editarPerfil";
    }
}
