package Hotel.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Habitacion {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    @Column (unique = true)
    private String numero;
    
    private Integer capacidad;
    private Double precio;
    private Boolean alta;

    public Habitacion() {
    }

    public Habitacion(String numero, Integer capacidad, Double precio, Boolean alta) {
        this.numero = numero;
        this.capacidad = capacidad;
        this.precio = precio;
        this.alta = alta;
    }
    
    
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return the capacidad
     */
    public Integer getCapacidad() {
        return capacidad;
    }

    /**
     * @param capacidad the capacidad to set
     */
    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    /**
     * @return the precio
     */
    public Double getPrecio() {
        return precio;
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    /**
     * @return the alta
     */
    public Boolean getAlta() {
        return alta;
    }

    /**
     * @param alta the alta to set
     */
    public void setAlta(Boolean alta) {
        this.alta = alta;
    }

    @Override
    public String toString() {
        return "\nHabitación " + numero + ", capacidad para " + capacidad + " personas, precio por noche $" + precio + ".";
    }
    
    
    
}
