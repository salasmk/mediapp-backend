package com.mitocode.repo;

import com.mitocode.model.Medic;
import com.mitocode.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMedicRepo extends IGenericRepo<Medic, Integer> {

}
