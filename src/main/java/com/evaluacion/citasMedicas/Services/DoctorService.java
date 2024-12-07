package com.evaluacion.citasMedicas.Services;

import com.evaluacion.citasMedicas.Entity.Doctor;
import com.evaluacion.citasMedicas.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    /**
     * Lista todos los doctores registrados.
     */
    public List<Doctor> listarDoctores() {
        return doctorRepository.findAll();
    }

    /**
     * Busca un doctor por su ID.
     */
    public Doctor obtenerDoctorPorId(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor no encontrado."));
    }

    /**
     * Registra o actualiza un doctor.
     */
    @Transactional
    public Doctor guardarDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    /**
     * Elimina un doctor por su ID.
     */
    @Transactional
    public void eliminarDoctor(Long doctorId) {
        doctorRepository.deleteById(doctorId);
    }
}
