package com.evaluacion.citasMedicas.Controller;

import com.evaluacion.citasMedicas.Entity.Consultorio;
import com.evaluacion.citasMedicas.Services.ConsultorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/consultorios")
public class ConsultorioController {

    @Autowired
    private ConsultorioService consultorioService;

    /**
     * Lista todos los consultorios.
     */
    @GetMapping
    public String listarConsultorios(Model model) {
        model.addAttribute("consultorios", consultorioService.listarConsultorios());
        return "consultorios/listar-consultorios";
    }

    /**
     * Muestra el formulario para agregar o editar un consultorio.
     */
    @GetMapping("/editar")
    public String mostrarFormulario(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            model.addAttribute("consultorio", consultorioService.obtenerConsultorioPorId(id));
        } else {
            model.addAttribute("consultorio", new Consultorio());
        }
        return "consultorios/editar-consultorio";
    }

    /**
     * Guarda un nuevo consultorio o actualiza uno existente.
     */
    @PostMapping("/guardar")
    public String guardarConsultorio(@ModelAttribute Consultorio consultorio, RedirectAttributes redirectAttributes) {
        consultorioService.guardarConsultorio(consultorio);
        redirectAttributes.addFlashAttribute("success", "Consultorio guardado exitosamente.");
        return "redirect:/consultorios";
    }

    /**
     * Elimina un consultorio.
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarConsultorio(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        consultorioService.eliminarConsultorio(id);
        redirectAttributes.addFlashAttribute("success", "Consultorio eliminado exitosamente.");
        return "redirect:/consultorios";
    }
}
