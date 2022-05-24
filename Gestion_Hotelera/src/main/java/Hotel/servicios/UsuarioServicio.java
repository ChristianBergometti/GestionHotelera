package Hotel.servicios;

import Hotel.entidades.Usuario;
import Hotel.errores.ErrorServicio;
import Hotel.repositorios.UsuarioRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public void creaUsuario(String nombre, String DNI, String mail, String clave, boolean alta) throws ErrorServicio {
        validar(nombre, DNI, mail, clave);
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setDNI(DNI);
        usuario.setEmail(mail);
        usuario.setClave(clave);
        usuario.setAlta(alta);
        usuarioRepositorio.save(usuario);

    }

    public void editarUsuario(String id, String nombre, String DNI, String mail, String clave) throws ErrorServicio {
        validar(nombre, DNI, mail, clave);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setDNI(DNI);
            usuario.setEmail(mail);
            usuario.setClave(clave);

            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado.");
        }
    }

    public void bajaUsuario(String id) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setAlta(false);
            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado.");
        }
    }

    public void altaUsuario(String id) throws ErrorServicio {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setAlta(true);
            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado.");
        }
    }

    public void validar(String nombre, String DNI, String mail, String clave) throws ErrorServicio {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre del usuario no puede ser nulo.");
        }

        if (DNI == null || DNI.trim().isEmpty()) {
            throw new ErrorServicio("El DNI del usuario no puede ser nulo.");
        }

        if (mail == null || mail.trim().isEmpty()) {
            throw new ErrorServicio("El E-mail del usuario no puede ser nulo.");
        }

        if (clave == null || clave.trim().isEmpty() || clave.length() <= 5) {
            throw new ErrorServicio("La clave del usuario no puede ser nula y tiene que tener al menos 6 dígitos.");
        }

    }

    public List<Usuario> listarUsuarios() {

        return usuarioRepositorio.findAll();

    }

    public Usuario usuarioPorId(String id) throws ErrorServicio {

        Optional<Usuario> usuario = usuarioRepositorio.findById(id);

        if (usuario.isPresent()) {
            return usuario.get();
        } else {
            throw new ErrorServicio("No se ha encontrado un Usuario con el ID ingresado.");
        }
    }

}
