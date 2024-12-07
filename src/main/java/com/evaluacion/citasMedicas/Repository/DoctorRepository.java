package com.evaluacion.citasMedicas.Repository;

import com.evaluacion.citasMedicas.Entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {
}
