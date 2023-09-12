package com.mitocode.controller;

import com.mitocode.dto.PatientDTO;
import com.mitocode.dto.PatientRecord;
import com.mitocode.model.Patient;
import com.mitocode.service.IPatientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class PatientController {

    //@Autowired
    private final IPatientService service;// = new PatientService();
    @Qualifier("defaultMapper")
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<PatientDTO>> findAll(){
        //ModelMapper modelMapper = new ModelMapper();
        List<PatientDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /*@GetMapping
    public ResponseEntity<List<PatientRecord>> findAll(){
        List<PatientRecord> list = service.findAll().stream().map( e ->
                new PatientRecord(e.getIdPatient(), e.getFirstName(), e.getLastName(), e.getDni(), e.getEmail(), e.getPhone(), e.getAddress())
        ).collect(Collectors.toList());*/
        /*List<PatientDTO> list = service.findAll().stream().map( e -> {
            PatientDTO dto = new PatientDTO();
            dto.setIdPatient(e.getIdPatient());
            dto.setFirstName(e.getFirstName());
            dto.setLastName(e.getLastName());
            dto.setDni(e.getDni());
            dto.setEmail(e.getEmail());
            dto.setPhone(e.getPhone());
            return dto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> findById(@PathVariable("id") Integer id){ //@Positive @Min(1) @Pattern(regexp = "[0-9]+")
        PatientDTO dto = this.convertToDto(service.findById(id));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /*@PostMapping
    public ResponseEntity<Patient> save(@RequestBody Patient patient){
        Patient obj = service.save(patient);
        return new ResponseEntity<>(obj, HttpStatus.CREATED);
    }*/

    @PostMapping
    public ResponseEntity<PatientDTO> save(@Valid @RequestBody PatientDTO dto){
        Patient obj = service.save(this.convertToEntity(dto));
        //localhost:8080/patients/3
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdPatient()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody PatientDTO dto){
        dto.setIdPatient(id);
        Patient obj = service.update(this.convertToEntity(dto), id);
        return new ResponseEntity<>(this.convertToDto(obj), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Min(1) @PathVariable("id") Integer id){
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/hateoas/{id}")
    public EntityModel<PatientDTO> findByIdHateoas(@PathVariable("id") Integer id){
        EntityModel<PatientDTO> resource = EntityModel.of(this.convertToDto(service.findById(id)));
        //localhost:8080/patients/4
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
        WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).findById(id));
        resource.add(link1.withRel("patient-info1"));
        resource.add(link2.withRel("patient-info2"));

        return resource;
    }

    @GetMapping("/pageable")
    public ResponseEntity<Page<PatientDTO>> listPage(Pageable pageable){
        Page<PatientDTO> page = service.listPage(pageable).map(p -> modelMapper.map(p, PatientDTO.class));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    ////////////////////

    @GetMapping("/get1/{name}")
    public ResponseEntity<List<PatientDTO>> getPatients1(@PathVariable("name") String name){
        //ModelMapper modelMapper = new ModelMapper();
        List<PatientDTO> list = service.getPatients1(name).stream().map(this::convertToDto).collect(Collectors.toList());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    private PatientDTO convertToDto(Patient obj){
        return modelMapper.map(obj, PatientDTO.class);
    }

    private Patient convertToEntity(PatientDTO dto){
        return modelMapper.map(dto, Patient.class);
    }

}
