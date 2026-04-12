package org.valeneisa.Operaciones;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IOperacionRepositorio extends JpaRepository<Operacion, Long> {

    List<Operacion> findByEstaActivaTrue();

    Optional<Operacion> findByCodigo(String codigo);
}