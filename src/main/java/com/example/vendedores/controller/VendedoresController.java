package com.example.vendedores.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.vendedores.DTO.VendedorDTO;
import com.example.vendedores.services.VendedorService;

import java.util.List;

@RestController
@RequestMapping("/api/vendedores")
public class VendedoresController {

    @Autowired
    private VendedorService service;

    @PostMapping
    public ResponseEntity<VendedorDTO> crear(@RequestBody VendedorDTO dto) {
        return ResponseEntity.ok(service.guardar(dto));
    }

    @GetMapping
    public ResponseEntity<List<VendedorDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendedorDTO> obtener(@PathVariable Integer id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendedorDTO> actualizar(@PathVariable Integer id, @RequestBody VendedorDTO dto) {
        return service.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

        //METODO HATEOAS para buscar por ID
    @GetMapping("/hateoas/{id}")
    public ResponseEntity<VendedorDTO> obtenerHATEOAS(@PathVariable Integer id) {
        return service.obtenerPorId(id)
            .map(dto -> {
                // Agregar los links HATEOAS
                dto.add(linkTo(methodOn(VendedoresController.class).obtenerHATEOAS(id)).withSelfRel());
                dto.add(linkTo(methodOn(VendedoresController.class).obtenerTodosHATEOAS()).withRel("todos"));
                dto.add(linkTo(methodOn(VendedoresController.class).eliminar(id)).withRel("eliminar"));

                dto.add(Link.of("http://localhost:8888/api/proxy/productos/" + dto.getIdVendedor()).withSelfRel());
                dto.add(Link.of("http://localhost:8888/api/proxy/productos/" + dto.getIdVendedor()).withRel("Modificar HATEOAS").withType("PUT"));
                dto.add(Link.of("http://localhost:8888/api/proxy/productos/" + dto.getIdVendedor()).withRel("Eliminar HATEOAS").withType("DELETE"));

                return ResponseEntity.ok(dto);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
}

    //METODO HATEOAS para listar todos los productos utilizando HATEOAS
    @GetMapping("/hateoas")
    public List<VendedorDTO> obtenerTodosHATEOAS() {
        List<VendedorDTO> lista = service.listar();

        for (VendedorDTO dto : lista) {
            //link url de la misma API
            dto.add(linkTo(methodOn(VendedoresController.class).obtenerHATEOAS(dto.getIdVendedor())).withSelfRel());

            //link HATEOAS para API Gateway "A mano"
            dto.add(Link.of("http://localhost:8888/api/proxy/productos").withRel("Get todos HATEOAS"));
            dto.add(Link.of("http://localhost:8888/api/proxy/productos/" + dto.getIdVendedor()).withRel("Crear HATEOAS").withType("POST"));
        }

        return lista;
    }

}
