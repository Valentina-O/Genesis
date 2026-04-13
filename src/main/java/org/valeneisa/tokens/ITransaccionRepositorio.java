package org.valeneisa.tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ITransaccionRepositorio extends JpaRepository<Transaccion, Long> {
    @Query("SELECT SUM(t.tokensConsumidos) FROM Transaccion t WHERE t.usuario.idUsuario = :id")
    Integer sumarTokensPorUsuario(Long id);

    // 2. Operación más utilizada (Métrica de Admin)
    @Query("SELECT t.operacion.nombre, COUNT(t) FROM Transaccion t GROUP BY t.operacion.nombre ORDER BY COUNT(t) DESC")
    List<Object[]> operacionMasPopular();

    // 3. Consumo total del día
    @Query("SELECT SUM(t.tokensConsumidos) FROM Transaccion t WHERE CAST(t.fecha AS date) = CURRENT_DATE")
    Integer consumoTotalHoy();

}