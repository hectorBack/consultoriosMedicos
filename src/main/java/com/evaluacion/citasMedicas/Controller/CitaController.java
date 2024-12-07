package com.evaluacion.citasMedicas.Controller;

import com.evaluacion.citasMedicas.Entity.Cita;
import com.evaluacion.citasMedicas.Entity.Consultorio;
import com.evaluacion.citasMedicas.Entity.Doctor;
import com.evaluacion.citasMedicas.Repository.ConsultorioRepository;
import com.evaluacion.citasMedicas.Repository.DoctorRepository;
import com.evaluacion.citasMedicas.Services.CitaService;
import com.evaluacion.citasMedicas.Services.ConsultorioService;
import com.evaluacion.citasMedicas.Services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ConsultorioService consultorioService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ConsultorioRepository consultorioRepository;

    /**
     * Muestra la lista de citas con filtros opcionales.
     */
    @GetMapping
    public String listarCitas(@RequestParam(value = "doctorId", required = false) Long doctorId,
                              @RequestParam(value = "fecha", required = false) LocalDate fecha,
                              @RequestParam(value = "consultorioId", required = false) Long consultorioId,
                              Model model) {
        // Obtener las citas filtradas directamente (sin DTO)
        List<Doctor> doctores = doctorRepository.findAll();
        List<Consultorio> consultorios = consultorioRepository.findAll();
        List<Cita> citas = citaService.consultarCitas(doctorId, fecha, consultorioId);

        // Agregar al modelo
        model.addAttribute("doctores", doctores);
        model.addAttribute("consultorios", consultorios);
        model.addAttribute("citas", citas);
        return "citas/listar-citas";  // Vista
    }

    /**
     * Muestra el formulario para agregar una nueva cita.
     */
    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("doctores", doctorService.listarDoctores());
        model.addAttribute("consultorios", consultorioService.listarConsultorios());
        model.addAttribute("cita", new Cita());
        return "citas/crear-cita";
    }

    /**
     * Procesa el formulario para crear una nueva cita.
     */
    @PostMapping("/crear")
    public String crearCita(@ModelAttribute Cita cita, Model model) {
        try {
            citaService.validarYCreaCita(cita); // Crear cita validando las reglas
            model.addAttribute("success", "Cita creada exitosamente.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/citas";
    }

    /**
     * Cancela una cita pendiente.
     */
    @PostMapping("/cancelar/{id}")
    public String cancelarCita(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            citaService.cancelarCita(id);
            redirectAttributes.addFlashAttribute("success", "Cita cancelada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/citas";
    }

}
