package org.valeneisa.Core;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repositorio JPA para la gestión de la entidad {@link TasaCambio}.
 * <p>
 * Extiende {@link JpaRepository} para heredar las operaciones CRUD estándar
 * y de paginación sobre la tabla de tasas de cambio, sin necesidad de
 * implementación adicional.
 * </p>
 *
 * @see TasaCambio
 */
public interface ITasaCambioRepositorio extends JpaRepository<TasaCambio, Long> {
}