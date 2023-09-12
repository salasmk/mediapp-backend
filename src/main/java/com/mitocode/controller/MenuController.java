package com.mitocode.controller;

import com.mitocode.dto.MenuDTO;
import com.mitocode.model.Menu;
import com.mitocode.service.IMenuService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {

    //@Autowired
    private final IMenuService service;
    @Qualifier("defaultMapper")
    private final ModelMapper modelMapper;

    @PostMapping("/user")
    public ResponseEntity<List<MenuDTO>> getMenusByUser(@RequestBody String username) throws Exception{
        List<Menu> menus = service.getMenusByUsername(username);
        List<MenuDTO> menusDTO = menus.stream().map(m -> {
            m.setRoles(new ArrayList<>()); //necesario porque fue un nativeQuery y devuelve PersistenBag
            return modelMapper.map(m, MenuDTO.class);
        }).collect(Collectors.toList());
        return new ResponseEntity<>(menusDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MenuDTO>> findAll(){
        List<MenuDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuDTO> findById(@PathVariable("id") Integer id){ //@Positive @Min(1) @Pattern(regexp = "[0-9]+")
        MenuDTO dto = this.convertToDto(service.findById(id));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MenuDTO> save(@Valid @RequestBody MenuDTO dto){
        Menu obj = service.save(this.convertToEntity(dto));
        //localhost:8080/menus/3
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdMenu()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody MenuDTO dto){
        dto.setIdMenu(id);
        Menu obj = service.update(this.convertToEntity(dto), id);
        return new ResponseEntity<>(this.convertToDto(obj), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Min(1) @PathVariable("id") Integer id){
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/hateoas/{id}")
    public EntityModel<MenuDTO> findByIdHateoas(@PathVariable("id") Integer id){
        EntityModel<MenuDTO> resource = EntityModel.of(this.convertToDto(service.findById(id)));
        //localhost:8080/menus/4
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
        WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).findById(id));
        resource.add(link1.withRel("menu-info1"));
        resource.add(link2.withRel("menu-info2"));

        return resource;
    }

    private MenuDTO convertToDto(Menu obj){
        return modelMapper.map(obj, MenuDTO.class);
    }

    private Menu convertToEntity(MenuDTO dto){
        return modelMapper.map(dto, Menu.class);
    }

}
