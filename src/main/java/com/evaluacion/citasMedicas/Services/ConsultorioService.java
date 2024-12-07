package com.evaluacion.citasMedicas.Services;

import com.evaluacion.citasMedicas.Entity.Consultorio;
import com.evaluacion.citasMedicas.Repository.ConsultorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConsultorioService {

    @Autowired
    private ConsultorioRepository consultorioRepository;

    /**
     * Lista todos los consultorios disponibles.
     */
    public List<Consultorio> listarConsultorios() {
        return consultorioRepository.findAll();
    }

    /**
     * Busca un consultorio por su ID.
     */
    public Consultorio obtenerConsultorioPorId(Long consultorioId) {
        return consultorioRepository.findById(consultorioId)
                .orElseThrow(() -> new IllegalArgumentException("Consultorio no encontrado."));
    }

    /**
     * Registra o actualiza un consultorio.
     */
    @Transactional
    public Consultorio guardarConsultorio(Consultorio consultorio) {
        return consultorioRepository.save(consultorio);
    }

    /**
     * Elimina un consultorio por su ID.
     */
    @Transactional
    public void eliminarConsultorio(Long consultorioId) {
        consultorioRepository.deleteById(consultorioId);
    }
}
