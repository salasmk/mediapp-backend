package com.mitocode.controller;

import com.mitocode.dto.SignDTO;
import com.mitocode.model.Sign;
import com.mitocode.service.ISignService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/signs")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class SignController {

    //@Autowired
    private final ISignService service;// = new SignService();
    @Qualifier("defaultMapper")
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<SignDTO>> findAll(){
        //ModelMapper modelMapper = new ModelMapper();
        List<SignDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SignDTO> findById(@PathVariable("id") Integer id){ //@Positive @Min(1) @Pattern(regexp = "[0-9]+")
        SignDTO dto = this.convertToDto(service.findById(id));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SignDTO> save(@Valid @RequestBody SignDTO dto){
        Sign obj = service.save(this.convertToEntity(dto));
        //localhost:8080/signs/3
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdSign()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SignDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody SignDTO dto){
        dto.setIdSign(id);
        Sign obj = service.update(this.convertToEntity(dto), id);
        return new ResponseEntity<>(this.convertToDto(obj), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Min(1) @PathVariable("id") Integer id){
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/hateoas/{id}")
    public EntityModel<SignDTO> findByIdHateoas(@PathVariable("id") Integer id){
        EntityModel<SignDTO> resource = EntityModel.of(this.convertToDto(service.findById(id)));
        //localhost:8080/signs/4
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
        WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).findById(id));
        resource.add(link1.withRel("sign-info1"));
        resource.add(link2.withRel("sign-info2"));

        return resource;
    }

    @GetMapping("/pageable")
    public ResponseEntity<Page<SignDTO>> listPage(Pageable pageable){
        Page<SignDTO> page = service.listPage(pageable).map(p -> modelMapper.map(p, SignDTO.class));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    ////////////////////

    private SignDTO convertToDto(Sign obj){
        return modelMapper.map(obj, SignDTO.class);
    }

    private Sign convertToEntity(SignDTO dto){
        return modelMapper.map(dto, Sign.class);
    }

}
