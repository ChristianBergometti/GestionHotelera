package Hotel.demo.servicios;

import Hotel.demo.entidades.Usuario;

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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void crearUsuario(String nombre, String DNI, String mail, String clave, boolean alta) throws ErrorServicio {
        validar(nombre, DNI, mail, clave);
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setDNI(DNI);
        usuario.setEmail(mail);
        String encriptada = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encriptada);
        usuario.setAlta(alta);

        usuarioRepositorio.save(usuario);

    }

    @Transactional
    public void editarUsuario(String id, String nombre, String DNI, String mail, String clave) throws ErrorServicio {
        validar(nombre, DNI, mail, clave);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setDNI(DNI);
            usuario.setEmail(mail);
            String encriptada = new BCryptPasswordEncoder().encode(clave);
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p1 = new SimpleGrantedAuthority("USUARIO_REGISTRADO" /*+ usuario.getRol()*/);
            permisos.add(p1);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            User user = new User(usuario.getEmail(), usuario.getClave(), permisos);
            return user;
        } else {
            return null;
        }
    }

}
