package Hotel.demo.servicios;

import Hotel.demo.entidades.Usuario;
import Hotel.demo.enums.Rol;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import Hotel.demo.errores.ErrorServicio;
import Hotel.demo.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void crearUsuario(String nombre, String DNI, String mail, String clave, String claveVerificacion) throws ErrorServicio {
        validar(nombre, DNI, mail, clave, claveVerificacion);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setDNI(DNI);
        usuario.setEmail(mail);
        String encriptada = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encriptada);
        usuario.setAlta(true);
        usuario.setRol(Rol.ROLE_USER);

        usuarioRepositorio.save(usuario);
    }

    @Transactional
    public void editarDatosPersonales(String id, String nombre, String DNI) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        validar(nombre, respuesta.get().getEmail(), DNI, respuesta.get().getClave(), respuesta.get().getClave());

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setDNI(DNI);
            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado.");
        }
    }
    
    @Transactional
    public void editarClave(String id, String clave1, String clave2) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        validar(respuesta.get().getNombre(),respuesta.get().getEmail(),respuesta.get().getDNI(), clave1, clave2);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            String encriptada = new BCryptPasswordEncoder().encode(clave1);
            usuario.setClave(encriptada);

            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado.");
        }
    }

    @Transactional
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

    @Transactional
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

    public void validar(String nombre, String mail, String DNI, String clave, String claveVerificacion) throws ErrorServicio {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre del usuario no puede ser nulo.");
        }

        if (mail == null || mail.trim().isEmpty()) {
            throw new ErrorServicio("El email del usuario no puede ser nulo.");
        }

        if (DNI == null || DNI.trim().isEmpty()) {
            throw new ErrorServicio("El DNI del usuario no puede ser nulo.");
        }

        if (clave == null || clave.trim().isEmpty()) {
            throw new ErrorServicio("La clave no puede ser nula.");
        }

        if (clave.length() <= 5) {
            throw new ErrorServicio("La clave debe tener al menos 6 caracteres.");
        }

        if (!claveVerificacion.equals(clave)) {
            throw new ErrorServicio("La claves deben ser iguales.");
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();
//
            GrantedAuthority p1 = new SimpleGrantedAuthority(usuario.getRol().toString());
            permisos.add(p1);
//            GrantedAuthority p2 = new SimpleGrantedAuthority("USUARIO_COMUN" /*+ usuario.getRol()*/);
//            permisos.add(p2);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            User user = new User(usuario.getEmail(), usuario.getClave(), permisos);
            System.out.println(user.toString());
            return user;
        } else {
            return null;
        }
    }

}
