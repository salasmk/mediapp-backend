package com.mitocode.controller;

import com.mitocode.dto.SpecialtyDTO;
import com.mitocode.model.Specialty;
import com.mitocode.service.ISpecialtyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/specialties")
@RequiredArgsConstructor
public class SpecialtyController {

    //@Autowired
    private final ISpecialtyService service;
    @Qualifier("defaultMapper")
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<SpecialtyDTO>> findAll(){
        List<SpecialtyDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyDTO> findById(@PathVariable("id") Integer id){
        SpecialtyDTO dto = this.convertToDto(service.findById(id));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SpecialtyDTO> save(@Valid @RequestBody SpecialtyDTO dto){
        Specialty obj = service.save(this.convertToEntity(dto));
        //localhost:8080/specialtys/3
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdSpecialty()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecialtyDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody SpecialtyDTO dto){
        dto.setIdSpecialty(id);
        Specialty obj = service.update(this.convertToEntity(dto), id);
        return new ResponseEntity<>(this.convertToDto(obj), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Min(1) @PathVariable("id") Integer id){
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/hateoas/{id}")
    public EntityModel<SpecialtyDTO> findByIdHateoas(@PathVariable("id") Integer id){
        EntityModel<SpecialtyDTO> resource = EntityModel.of(this.convertToDto(service.findById(id)));
        //localhost:8080/specialtys/4
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
        WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).findById(id));
        resource.add(link1.withRel("specialty-info1"));
        resource.add(link2.withRel("specialty-info2"));

        return resource;
    }

    private SpecialtyDTO convertToDto(Specialty obj){
        return modelMapper.map(obj, SpecialtyDTO.class);
    }

    private Specialty convertToEntity(SpecialtyDTO dto){
        return modelMapper.map(dto, Specialty.class);
    }

}
