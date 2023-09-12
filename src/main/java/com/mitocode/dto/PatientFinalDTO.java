package com.mitocode.dto;

import java.util.Objects;


public class PatientFinalDTO {


    private final Integer idPatient;
    private final String firstName;
    private final String lastName;
    private final String dni;
    private final String address;
    private final String phone;
    private final String email;

    public PatientFinalDTO(Integer idPatient, String firstName, String lastName, String dni, String address, String phone, String email) {
        this.idPatient = idPatient;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dni = dni;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public Integer getIdPatient() {
        return idPatient;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDni() {
        return dni;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientFinalDTO that = (PatientFinalDTO) o;
        return Objects.equals(idPatient, that.idPatient) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(dni, that.dni) && Objects.equals(address, that.address) && Objects.equals(phone, that.phone) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPatient, firstName, lastName, dni, address, phone, email);
    }

    @Override
    public String toString() {
        return "PatientDTO{" +
                "idPatient=" + idPatient +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dni='" + dni + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
