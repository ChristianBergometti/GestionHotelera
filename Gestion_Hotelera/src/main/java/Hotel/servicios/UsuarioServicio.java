/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hotel.servicios;

import Hotel.entidades.Usuario;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {
    
    @Autowired
    //private UsuarioRepositorio usuarioRepositorio;
       
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
        
        Optional<Usuario> respuesta=usuarioRepositorio.findById(id);
        
        if(respuesta.isPresent()){
            Usuario usuario= respuesta.get();
            usuario.setNombre(nombre);
            usuario.setDNI(DNI);
            usuario.setEmail(mail);
            usuario.setClave(clave);
            
            usuarioRepositorio.save(usuario);
        }else {
            throw new ErrorServicio("No se encontro el Usuario Solicitado");
        }
    }
    
    public void bajaUsuario(String id,boolean alta) throws ErrorServicio {
        Optional<Usuario> respuesta=usuarioRepositorio.findById(id);
        
        if(respuesta.isPresent()){
            Usuario usuario= respuesta.get();
            usuario.setAlta(alta);
            usuarioRepositorio.save(usuario);
        }else {
            throw new ErrorServicio("No se encontro el Usuario Solicitado");
        }
    }
    
    public void altaUsuario(String id,boolean alta) throws ErrorServicio {
        
        Optional<Usuario> respuesta=usuarioRepositorio.findById(id);
        
        if(respuesta.isPresent()){
            Usuario usuario= respuesta.get();
            usuario.setAlta(alta);
            usuarioRepositorio.save(usuario);
        }else {
            throw new ErrorServicio("No se encontro el Usuario Solicitado");
        }
    }
    
    public void validar(String nombre, String DNI, String mail, String clave) throws ErrorServicio {
        
        if(nombre == null || nombre.isEmpty()){
            throw new ErrorServicio("El nombre del Usuario no puede ser nulo. ");
        }
        
        if(DNI == null || DNI.isEmpty()){
            throw new ErrorServicio("El DBI del Usuario no puede ser nulo. ");
        }
        
        if(mail == null || mail.isEmpty()){
            throw new ErrorServicio("El E-mail del Usuario no puede ser nulo. ");
        }
        
        if(clave == null || mail.isEmpty() || clave.length() <= 8 ){
            throw new ErrorServicio("El E-mail del Usuario no puede ser nulo y tiene que tener mas de 8 digitos. ");
        }
        
        
    }
    
    public List<Usuario> listarUsuarios() {
        
    return usuarioRepositorio.findAll();
    
    }
    public Usuario usuarioPorId(String id) throws ErrorServicio {
        
        Optional<Usuario> usuario = usuarioRepositorio.findById(id);
        
        if(usuario.isPresent()) {
            return usuario.get();
        } else {
            throw new ErrorServicio("No se ha encontrado un Usuario con el ID ingresado.");
        }
    }
    
}
