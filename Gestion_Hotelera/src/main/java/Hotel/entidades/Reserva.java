package Hotel.entidades;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Entity
public class Reserva {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid" , strategy = "uuid2")
    private String id;

    private Boolean alta;

    private Double precio;

    private Integer personas;

    @Temporal(TemporalType.DATE)
    private Date ingreso;

    @Temporal(TemporalType.DATE)
    private Date egreso;

    @OneToMany
    private List<Habitacion> habitaciones;

    @ManyToOne
    private Usuario usuario;



    public Reserva() {
    }

    public Reserva(Boolean alta, Double precio, Integer personas, Date ingreso, Date egreso,
                   List<Habitacion> habitaciones, Usuario usuario) {
        this.alta = alta;
        this.precio = precio;
        this.personas = personas;
        this.ingreso = ingreso;
        this.egreso = egreso;
        this.habitaciones = habitaciones;
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(List<Habitacion> habitaciones) {
        this.habitaciones = habitaciones;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Datos de su Reserva" +
                "\nCosto de estadía: $" + precio +
                "\nPara " + personas + " personas." +
                "\nCheck in: " + ingreso +
                "\nCheck out: " + egreso +
                "\n\nDatos de la/s habitacion/es: " + habitaciones;
    }
}
