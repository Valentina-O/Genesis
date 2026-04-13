package org.valeneisa.tokens;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.valeneisa.usuario.entidad.Usuario;

import java.util.List;

/**
 * Repositorio para la entidad Transaccion.
 * Proporciona métodos para consultar el historial de consumo de tokens,
 * métricas de uso y operaciones relacionadas con los usuarios.
 */
public interface ITransaccionRepositorio extends JpaRepository<Transaccion, Long> {

    /**
     * Obtiene las transacciones de un usuario de forma paginada.
     *
     * @param usuario Usuario a consultar.
     * @param pageable Configuración de paginación.
     * @return Página de transacciones.
     */
    Page<Transaccion> findByUsuario(Usuario usuario, Pageable pageable);

    /**
     * Obtiene las transacciones de un usuario ordenadas por fecha descendente.
     *
     * @param usuario Usuario a consultar.
     * @return Lista de transacciones ordenadas.
     */
    List<Transaccion> findByUsuarioOrderByFechaDesc(Usuario usuario);

    /**
     * Suma el total de tokens consumidos por un usuario.
     *
     * @param id Identificador del usuario.
     * @return Total de tokens consumidos.
     */
    @Query("SELECT SUM(t.tokensConsumidos) FROM Transaccion t WHERE t.usuario.idUsuario = :id")
    Integer sumarTokensPorUsuario(Long id);

    /**
     * Obtiene la operación más utilizada en el sistema.
     *
     * @return Lista con el nombre de la operación y su cantidad de usos.
     */
    @Query("SELECT t.operacion.nombre, COUNT(t) FROM Transaccion t GROUP BY t.operacion.nombre ORDER BY COUNT(t) DESC")
    List<Object[]> operacionMasPopular();

    /**
     * Calcula el consumo total de tokens del día actual.
     *
     * @return Total de tokens consumidos hoy.
     */
    @Query("SELECT SUM(t.tokensConsumidos) FROM Transaccion t WHERE CAST(t.fecha AS date) = CURRENT_DATE")
    Integer consumoTotalHoy();
}