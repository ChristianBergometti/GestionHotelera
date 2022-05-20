package Hotel.entidades;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Reserva {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid" , strategy = "uuid2")
    private String id;

    private String clave;
    private Boolean alta;

    private Double precio;

    private Integer personas;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ingreso;

    @Temporal(TemporalType.TIMESTAMP)
    private Date egreso;

    @OneToMany
    private List<Habitacion> habitaciones;

    @ManyToOne
    private Usuario usuario;



    public Reserva() {
    }

    public Reserva(String clave, Boolean alta, Double precio, Integer personas, Date ingreso, Date egreso,
                   Habitacion habitacion, Usuario usuario) {
        this.clave = clave;
        this.alta = alta;
        this.precio = precio;
        this.personas = personas;
        this.ingreso = ingreso;
        this.egreso = egreso;
        this.habitacion = habitacion;
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getPersonas() {
        return personas;
    }

    public void setPersonas(Integer personas) {
        this.personas = personas;
    }

    public Date getIngreso() {
        return ingreso;
    }

    public void setIngreso(Date ingreso) {
        this.ingreso = ingreso;
    }

    public Date getEgreso() {
        return egreso;
    }

    public void setEgreso(Date egreso) {
        this.egreso = egreso;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id='" + id + '\'' +
                ", clave='" + clave + '\'' +
                ", alta=" + alta +
                ", precio=" + precio +
                ", personas=" + personas +
                ", ingreso=" + ingreso +
                ", egreso=" + egreso +
                ", habitacion=" + habitacion +
                ", usuario=" + usuario +
                '}';
    }
}
