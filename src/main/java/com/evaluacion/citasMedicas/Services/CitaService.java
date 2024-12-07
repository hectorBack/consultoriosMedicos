package com.evaluacion.citasMedicas.Services;

import com.evaluacion.citasMedicas.Entity.Cita;
import com.evaluacion.citasMedicas.Entity.Consultorio;
import com.evaluacion.citasMedicas.Entity.Doctor;
import com.evaluacion.citasMedicas.Repository.CitaRepository;
import com.evaluacion.citasMedicas.Repository.ConsultorioRepository;
import com.evaluacion.citasMedicas.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ConsultorioRepository consultorioRepository;

    /**
     * Valida y crea una nueva cita, siguiendo las reglas de negocio.
     */
    @Transactional
    public void validarYCreaCita(Cita cita) {

        // Asegúrate de que el doctor y el consultorio existan antes de crear la cita
        Doctor doctor = doctorRepository.findById(cita.getDoctor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor no encontrado."));
        cita.setDoctor(doctor);
        Consultorio consultorio = consultorioRepository.findById(cita.getConsultorio().getId())
                .orElseThrow(() -> new IllegalArgumentException("Consultorio no encontrado."));
        cita.setConsultorio(consultorio);


        // Regla 1: No se puede agendar cita en el mismo consultorio y horario.
        if (citaRepository.existsByConsultorioIdAndHorario(cita.getConsultorio().getId(), cita.getHorario())) {
            throw new IllegalArgumentException("Ya existe una cita en este consultorio a la misma hora.");
        }

        // Regla 2: No se puede agendar cita para el mismo doctor y horario.
        if (citaRepository.existsByDoctorIdAndHorario(cita.getDoctor().getId(), cita.getHorario())) {
            throw new IllegalArgumentException("El doctor ya tiene una cita a esta hora.");
        }

        // Regla 3: Paciente no puede agendar citas con menos de 2 horas de diferencia.
        if (citaRepository.existsPacienteConConflicto(cita.getNombrePaciente(), cita.getHorario())) {
            throw new IllegalArgumentException("El paciente tiene conflicto con otra cita en el mismo día.");
        }

        // Regla 4: El doctor no puede tener más de 8 citas en el día.
        long citasDelDia = citaRepository.countByDoctorIdAndFecha(
                cita.getDoctor().getId(), cita.getHorario().toLocalDate()
        );
        if (citasDelDia >= 8) {
            throw new IllegalArgumentException("El doctor ha alcanzado el límite de citas para este día.");
        }

        citaRepository.save(cita);

    }

    /**
     * Consulta citas con filtros opcionales: doctor, consultorio y fecha.
     */
    public List<Cita> consultarCitas(Long doctorId, LocalDate fecha, Long consultorioId) {
        return citaRepository.findCitasByFiltros(doctorId, fecha, consultorioId);
    }

    /**
     * Cancela una cita pendiente.
     */
    @Transactional
    public void cancelarCita(Long citaId) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada."));
        if (cita.getHorario().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se puede cancelar una cita que ya ocurrió.");
        }
        citaRepository.delete(cita);
    }

}
