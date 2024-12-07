package com.evaluacion.citasMedicas.Repository;

import com.evaluacion.citasMedicas.Entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    boolean existsByConsultorioIdAndHorario(Long consultorioId, LocalDateTime horario);

    boolean existsByDoctorIdAndHorario(Long doctorId, LocalDateTime horario);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cita c WHERE c.nombrePaciente = :nombrePaciente AND c.horario = :horario")
    boolean existsPacienteConConflicto(@Param("nombrePaciente") String nombrePaciente, @Param("horario") LocalDateTime horario);

    @Query("SELECT COUNT(c) FROM Cita c WHERE c.doctor.id = :doctorId AND DATE(c.horario) = :fecha")
    long countByDoctorIdAndFecha(@Param("doctorId") Long doctorId, @Param("fecha") LocalDate fecha);

    // Eliminar CitaDTO y devolver directamente Cita
    @Query("SELECT c FROM Cita c WHERE " +
            "(:doctorId IS NULL OR c.doctor.id = :doctorId) AND " +
            "(:fecha IS NULL OR DATE(c.horario) = :fecha) AND " +
            "(:consultorioId IS NULL OR c.consultorio.id = :consultorioId)")
    List<Cita> findCitasByFiltros(@Param("doctorId") Long doctorId,
                                  @Param("fecha") LocalDate fecha,
                                  @Param("consultorioId") Long consultorioId);
}


