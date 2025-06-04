package moises.impresoraservidor.controller;

import moises.impresoraservidor.service.FileValidationService;
import moises.impresoraservidor.service.ValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/impresora")
public class ImpresoraController {

    @Autowired
    FileValidationService fileValidationService;

    @Autowired
    ValidatorService validatorService;

    @GetMapping("/imprimir")
    public String imprimir(Model model) {
        return "imprimir";
    }

    @PostMapping("/archivos")
    public String subirArchivos(@RequestParam("Files")MultipartFile[] files){

        if (files == null) {
            return "redirect:/impresora/imprimir";
        }

        
        //Comparar archivos (si es PDF o imagen)
        //Si validacion pasa, lo metemos a nuestro servicio para finalmente imprimirlo





    }


}
