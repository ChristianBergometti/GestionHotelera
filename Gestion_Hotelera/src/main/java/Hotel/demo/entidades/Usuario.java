package Hotel.demo.entidades;

import Hotel.demo.enums.Rol;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(generator = "uuid" )
    @GenericGenerator(name= "uuid", strategy = "uuid2")
    private String id;
    private String nombre;
    private String DNI;
    private String email;
    private String clave;
    private boolean alta;
    private Rol rol;
    
    public Usuario() {
    }

    public Usuario(String nombre, String DNI, String email, String clave, boolean alta, Rol rol) {
        this.nombre = nombre;
        this.DNI = DNI;
        this.email = email;
        this.clave = clave;
        this.alta = alta;
        this.rol = rol;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean isAlta() {
        return alta;
    }

    public void setAlta(boolean alta) {
        this.alta = alta;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
    
    

    @Override
    public String toString() {
        return "Nombre: " + nombre
                + "\nDNI: " + DNI 
                + "\nE-mail=" + email;
    }
}
