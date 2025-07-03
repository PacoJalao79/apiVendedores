package com.example.vendedores.DTO;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;

@Data
public class VendedorDTO extends RepresentationModel<VendedorDTO>{
    private Integer idVendedor;
    private Integer idUsuario;
    private String nombreCompleto;
    private String rut;
    private String areaDeVentas;
    
}
