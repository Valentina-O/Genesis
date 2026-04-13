package org.valeneisa.Controladores;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Dtos.MetricasResponse;
import org.valeneisa.Servicios.AdminServicio;
import org.valeneisa.tokens.ITransaccionRepositorio;
import org.valeneisa.usuario.entidad.Usuario;
import org.valeneisa.usuario.repositorio.IUsuarioRepositorio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión administrativa de la plataforma.
 * <p>
 * Expone endpoints bajo el prefijo {@code /api/v1/admin} para operaciones
 * exclusivas del rol administrador: gestión de usuarios, tokens y métricas.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminControlador {

    private final AdminServicio adminServicio;
    private final IUsuarioRepositorio usuarioRepositorio;
    private final ITransaccionRepositorio transaccionRepositorio;

    // =========================================================================
    // GESTIÓN DE USUARIOS
    // =========================================================================

    /**
     * Retorna el listado paginado de todos los usuarios registrados.
     */
    @GetMapping("/usuarios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Usuario>> listarUsuarios(Pageable pageable) {
        return ResponseEntity.ok(usuarioRepositorio.findAll(pageable));
    }

    /**
     * Activa o desactiva la cuenta de un usuario específico.
     */
    @PatchMapping("/usuarios/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> cambiarEstado(@PathVariable Long id, @RequestParam Boolean activo) {
        adminServicio.cambiarEstadoUsuario(id, activo);
        return ResponseEntity.ok("Estado actualizado exitosamente");
    }

    /**
     * Recarga manualmente tokens en la cuenta de un usuario.
     */
    @PostMapping("/usuarios/{id}/recargar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> recargar(@PathVariable Long id, @RequestBody Integer cantidad) {
        adminServicio.recargarTokens(id, cantidad);
        return ResponseEntity.ok("Tokens recargados correctamente");
    }

    // =========================================================================
    // CONFIGURACIÓN Y CATÁLOGO
    // =========================================================================

    /**
     * Actualiza la tasa de cambio global de la plataforma.
     */
    @PutMapping("/tasa-cambio")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> actualizarTasa(@RequestBody Double valor) {
        adminServicio.actualizarTasaCambio(valor);
        return ResponseEntity.ok("Tasa actualizada correctamente");
    }

    /**
     * Activa o desactiva una operación del catálogo mediante su código.
     */
    @PatchMapping("/operaciones/{codigo}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> cambiarEstadoOperacion(@PathVariable String codigo, @RequestParam Boolean activo) {
        adminServicio.cambiarEstadoOperacion(codigo, activo);
        return ResponseEntity.ok("El estado de la operación " + codigo + " ha sido actualizado");
    }

    // =========================================================================
    // MÉTRICAS Y ESTADÍSTICAS (Unificado con YAML)
    // =========================================================================

    /**
     * Retorna un resumen consolidado de métricas según el contrato OpenAPI.
     * <p>
     * Utiliza los métodos del repositorio para obtener datos reales de consumo y popularidad.
     * </p>
     */
    @GetMapping("/metricas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MetricasResponse> obtenerMetricasResumen() {
        // Obtenemos datos reales de tus métodos existentes
        Integer consumoHoy = transaccionRepositorio.consumoTotalHoy();
        List<Object[]> popular = transaccionRepositorio.operacionMasPopular();

        // Formateamos la operación más popular para el DTO
        String nombreOpMasPopular = (popular != null && !popular.isEmpty())
                ? String.valueOf(popular.get(0)[0])
                : "N/A";

        // Usuarios totales registrados
        long totalUsuarios = usuarioRepositorio.count();

        return ResponseEntity.ok(new MetricasResponse(
                (int) totalUsuarios,
                nombreOpMasPopular,
                (consumoHoy != null) ? consumoHoy : 0
        ));
    }

    /**
     * Resumen consolidado para el dashboard (Map genérico).
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> obtenerDashboard() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("consumoHoy", transaccionRepositorio.consumoTotalHoy());
        metrics.put("operacionMasPopular", transaccionRepositorio.operacionMasPopular());
        return ResponseEntity.ok(metrics);
    }
}