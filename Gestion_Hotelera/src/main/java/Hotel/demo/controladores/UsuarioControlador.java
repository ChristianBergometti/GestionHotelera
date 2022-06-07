package Hotel.demo.controladores;

import Hotel.demo.entidades.Usuario;
import javax.servlet.http.HttpSession;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasAnyRole('ROLE_USER')")
@RequestMapping("/usuario")
public class UsuarioControlador {

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
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        model.put("NombreCompleto", login.getNombre());
        model.put("CorreoElectronico", login.getEmail());
        model.put("DocumentoDeIdentidad", login.getDNI());
        return "editarPerfil";
    }
}
