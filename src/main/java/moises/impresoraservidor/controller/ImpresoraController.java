package moises.impresoraservidor.controller;

import moises.impresoraservidor.service.FileValidationService;
import moises.impresoraservidor.service.ImpresoraService;
import moises.impresoraservidor.service.ValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/impresora")
public class ImpresoraController {

    @Autowired
    ImpresoraService impresoraService;

    @GetMapping("/imprimir")
    public String imprimir(Model model) {
        return "imprimir";
    }

    @PostMapping("/archivos")
    public String subirArchivos(@RequestParam("Files")MultipartFile[] files, RedirectAttributes redirectAttributes) {


        if (files == null || files.length == 0 || (files.length == 1 && files[0].isEmpty())) {
            redirectAttributes.addFlashAttribute("errorMessage","Por favor selecciona al menos un archivo");
            return "redirect:/impresora/imprimir";
        }


       boolean success = impresoraService.imprimir(files);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage","Imprimido correctamente");
            return "redirect:/impresora/imprimir";
        }
        else{
            redirectAttributes.addFlashAttribute("errorMessage","Error al imprimir");
            return "redirect:/impresora/imprimir";
        }
    }


}
