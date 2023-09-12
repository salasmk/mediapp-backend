package com.mitocode.controller;

import com.mitocode.dto.MedicDTO;
import com.mitocode.model.Medic;
import com.mitocode.service.IMedicService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/medics")
@RequiredArgsConstructor
public class MedicController {

    //@Autowired
    private final IMedicService service;
    @Qualifier("medicMapper")
    private final ModelMapper modelMapper;

    //@PreAuthorize("@authorizeLogic.hasAccess('findAll')")
    //@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @GetMapping
    public ResponseEntity<List<MedicDTO>> findAll(){
        List<MedicDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicDTO> findById(@PathVariable("id") Integer id){ //@Positive @Min(1) @Pattern(regexp = "[0-9]+")
        MedicDTO dto = this.convertToDto(service.findById(id));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MedicDTO> save(@Valid @RequestBody MedicDTO dto){
        Medic obj = service.save(this.convertToEntity(dto));
        //localhost:8080/medics/3
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdMedic()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody MedicDTO dto){
        dto.setIdMedic(id);
        Medic obj = service.update(this.convertToEntity(dto), id);
        return new ResponseEntity<>(this.convertToDto(obj), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Min(1) @PathVariable("id") Integer id){
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/hateoas/{id}")
    public EntityModel<MedicDTO> findByIdHateoas(@PathVariable("id") Integer id){
        EntityModel<MedicDTO> resource = EntityModel.of(this.convertToDto(service.findById(id)));
        //localhost:8080/medics/4
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
        WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).findById(id));
        resource.add(link1.withRel("medic-info1"));
        resource.add(link2.withRel("medic-info2"));

        return resource;
    }

    private MedicDTO convertToDto(Medic obj){
        return modelMapper.map(obj, MedicDTO.class);
    }

    private Medic convertToEntity(MedicDTO dto){
        return modelMapper.map(dto, Medic.class);
    }

}
