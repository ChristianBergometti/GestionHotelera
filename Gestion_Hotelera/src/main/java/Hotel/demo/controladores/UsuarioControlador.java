/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hotel.demo.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Diego
 */
@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

    @GetMapping("/registro")
    public String registro() {
        return "registroUsuarioHotel";
    }

}
