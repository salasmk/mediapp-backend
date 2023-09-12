package com.mitocode.repo;

import com.mitocode.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface IPatientRepo extends IGenericRepo<Patient, Integer> {

    //void printPatient(Patient patient);
}
