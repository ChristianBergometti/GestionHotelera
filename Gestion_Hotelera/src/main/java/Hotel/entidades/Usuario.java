package Hotel.entidades;

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

    public Usuario() {
    }

    public Usuario(String nombre, String DNI, String email, String clave, boolean alta) {
        this.nombre = nombre;
        this.DNI = DNI;
        this.email = email;
        this.clave = clave;
        this.alta = alta;
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

    @Override
    public String toString() {
        return "Usuario{" + ", Usuario=" + nombre + ", DNI=" + DNI + ", e-mail=" + email  + '}';
    }
    
    
    
    
    
}
