package org.valeneisa.Operaciones;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Operacion.
 * Proporciona métodos para acceder y consultar operaciones en la base de datos.
 */
public interface IOperacionRepositorio extends JpaRepository<Operacion, Long> {

    /**
     * Obtiene todas las operaciones que están activas.
     *
     * @return Lista de operaciones activas.
     */
    List<Operacion> findByEstaActivaTrue();

    /**
     * Busca una operación por su código único.
     *
     * @param codigo Código de la operación.
     * @return Operación encontrada (si existe).
     */
    Optional<Operacion> findByCodigo(String codigo);
}