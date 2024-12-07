package com.evaluacion.citasMedicas.Controller;

import com.evaluacion.citasMedicas.Entity.Doctor;
import com.evaluacion.citasMedicas.Services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/doctores")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * Lista todos los doctores.
     */
    @GetMapping
    public String listarDoctores(Model model) {
        model.addAttribute("doctores", doctorService.listarDoctores());
        return "doctores/listar-doctores";
    }

    /**
     * Muestra el formulario para agregar o editar un doctor.
     */
    @GetMapping("/editar")
    public String mostrarFormulario(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            model.addAttribute("doctor", doctorService.obtenerDoctorPorId(id));
        } else {
            model.addAttribute("doctor", new Doctor());
        }
        return "doctores/editar-doctor";
    }

    /**
     * Guarda un nuevo doctor o actualiza uno existente.
     */
    @PostMapping("/guardar")
    public String guardarDoctor(@ModelAttribute Doctor doctor, RedirectAttributes redirectAttributes) {
        doctorService.guardarDoctor(doctor);
        redirectAttributes.addFlashAttribute("success", "Doctor guardado exitosamente.");
        return "redirect:/doctores";
    }

    /**
     * Elimina un doctor.
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarDoctor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        doctorService.eliminarDoctor(id);
        redirectAttributes.addFlashAttribute("success", "Doctor eliminado exitosamente.");
        return "redirect:/doctores";
    }
}
