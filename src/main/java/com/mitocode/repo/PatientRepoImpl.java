package com.mitocode.repo;

import com.mitocode.model.Patient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.List;


@Repository
public class PatientRepoImpl {/*implements IPatientRepo{

    @Override
    public void printPatient(Patient patient){
        System.out.println(patient);
    }*/

    @PersistenceContext
    private EntityManager entityManager;

    public List<Patient> getPatients1(String name) {
        /*return entityManager.createQuery("FROM Patient p WHERE p.firstName LIKE :name")
                .setParameter("name", "%"+name+"%")
                .getResultList();*/

        //https://www.baeldung.com/jpa-and-or-criteria-predicates
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Patient> cq = cb.createQuery(Patient.class);

        Root<Patient> patient = cq.from(Patient.class);
        Predicate patientNamePredicate = cb.equal(patient.get("firstName"), name);
        //Predicate patientLastNamePredicate = cb.like(patient.get("lastName"), "%" + name + "%");
        cq.where(patientNamePredicate); //, patientLastNamePredicate);

        TypedQuery<Patient> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
