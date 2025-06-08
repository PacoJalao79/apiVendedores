package com.example.vendedores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.vendedores.model.Vendedor;

public interface VendedorRepository  extends JpaRepository <Vendedor, Integer> {
}