package org.valeneisa.tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repositorio para la entidad Plan.
 * Permite realizar operaciones CRUD sobre los planes en la base de datos.
 */
@Repository
public interface IPlanRepositorio extends JpaRepository<Plan, Long> {
}