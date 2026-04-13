package org.valeneisa.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Servicios.AdminServicio;
import org.valeneisa.tokens.ITransaccionRepositorio;
import org.valeneisa.usuario.entidad.Usuario;
import org.valeneisa.usuario.repositorio.IUsuarioRepositorio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
 /*// Controlador REST para la gestión administrativa de la plataforma.
 /* Expone endpoints bajo el prefijo {@code /api/admin} para operaciones
 * exclusivas del rol administrador: gestión de usuarios, recarga de tokens,
        * configuración de tasas de cambio, administración del catálogo de operaciones
 * y consulta de métricas y estadísticas de consumo.

        */
@RestController
@RequestMapping("/api/admin")
public class AdminControlador {

    @Autowired
    private AdminServicio adminServicio;

    @Autowired
    private IUsuarioRepositorio usuarioRepositorio;
     /**
      * Retorna el listado paginado de todos los usuarios registrados en la plataforma,
      * incluyendo su saldo de tokens y estado de cuenta.
      *
      * @param pageable parámetros de paginación y ordenamiento.
      * @return {@link ResponseEntity} con una {@link Page} de {@link Usuario}.
      */
    @Autowired
    private ITransaccionRepositorio transaccionRepositorio;

     /**
      * Retorna el listado paginado de todos los usuarios registrados en la plataforma,
      * incluyendo su saldo de tokens y estado de cuenta.
      *
      * @param pageable parámetros de paginación y ordenamiento.
      * @return {@link ResponseEntity} con una {@link Page} de {@link Usuario}.
      */
    @GetMapping("/usuarios")
    public ResponseEntity<Page<Usuario>> listarUsuarios(Pageable pageable) {
        return ResponseEntity.ok(usuarioRepositorio.findAll(pageable));
    }

     /**
      * Activa o desactiva la cuenta de un usuario específico.
      *
      * @param id     identificador único del usuario.
      * @param activo {@code true} para activar la cuenta, {@code false} para desactivarla.
      * @return {@link ResponseEntity} con un mensaje de confirmación.
      */
    @PatchMapping("/usuarios/{id}/estado")
    public ResponseEntity<String> cambiarEstado(@PathVariable Long id, @RequestParam Boolean activo) {
        adminServicio.cambiarEstadoUsuario(id, activo);
        return ResponseEntity.ok("Estado actualizado exitosamente");
    }
     /**
      * Recarga manualmente una cantidad determinada de tokens en la cuenta de un usuario.
      *
      * @param id       identificador único del usuario al que se le recargarán tokens.
      * @param cantidad número de tokens a acreditar.
      * @return {@link ResponseEntity} con un mensaje de confirmación.
      */
    @PostMapping("/usuarios/{id}/recargar")
    public ResponseEntity<String> recargar(@PathVariable Long id, @RequestBody Integer cantidad) {
        adminServicio.recargarTokens(id, cantidad);
        return ResponseEntity.ok("Tokens recargados correctamente");
    }
     /**
      * Actualiza la tasa de cambio global utilizada en las operaciones de la plataforma.
      *
      * @param valor nuevo valor de la tasa de cambio.
      * @return {@link ResponseEntity} con un mensaje de confirmación.
      */

    @PutMapping("/tasa-cambio")
    public ResponseEntity<String> actualizarTasa(@RequestBody Double valor) {
        adminServicio.actualizarTasaCambio(valor);
        return ResponseEntity.ok("Tasa actualizada correctamente");
    }

     /**
      * Activa o desactiva una operación del catálogo mediante su código identificador
      * (por ejemplo, {@code OP-01}, {@code OP-02}).
      *
      * @param codigo código de la operación a modificar.
      * @param activo {@code true} para habilitar la operación, {@code false} para deshabilitarla.
      * @return {@link ResponseEntity} con un mensaje de confirmación que incluye el código de la operación.
      */
    @PatchMapping("/operaciones/{codigo}/estado")
    public ResponseEntity<String> cambiarEstadoOperacion(@PathVariable String codigo, @RequestParam Boolean activo) {
        adminServicio.cambiarEstadoOperacion(codigo, activo);
        return ResponseEntity.ok("El estado de la operación " + codigo + " ha sido actualizado");
    }

     /**
      * Obtiene el total de tokens consumidos en la plataforma durante el día actual.
      *
      * @return {@link ResponseEntity} con el consumo total del día como valor entero.
      */
    @GetMapping("/metricas/hoy")
    public ResponseEntity<Integer> obtenerConsumoHoy() {
        return ResponseEntity.ok(transaccionRepositorio.consumoTotalHoy());
    }
     /**
      * Retorna un resumen consolidado de métricas clave para el panel de administración.
      * <p>
      * Incluye el consumo total del día ({@code consumoHoy}) y la operación más
      * popular de la plataforma ({@code operacionMasPopular}).
      * </p>
      *
      * @return {@link ResponseEntity} con un {@link Map} de métricas del dashboard.
      */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> obtenerDashboard() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("consumoHoy", transaccionRepositorio.consumoTotalHoy());
        metrics.put("operacionMasPopular", transaccionRepositorio.operacionMasPopular());
        // Esto resume todo el trabajo de métricas en un solo clic
        return ResponseEntity.ok(metrics);
    }

     /**
      * Obtiene el consumo global de tokens de la plataforma correspondiente al día de hoy.
      * <p>
      * Endpoint restringido al rol {@code ADMIN}.
      * </p>
      *
      * @return {@link ResponseEntity} con el total de tokens consumidos hoy.
      */
    @GetMapping("/estadisticas/consumo-total")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> obtenerConsumoGlobal() {
        return ResponseEntity.ok(transaccionRepositorio.consumoTotalHoy());
    }

     /**
      * Retorna la operación más utilizada en la plataforma junto con su frecuencia de uso.
      * <p>
      * Endpoint restringido al rol {@code ADMIN}.
      * </p>
      *
      * @return {@link ResponseEntity} con una lista de arreglos de objetos que representan
      *         la operación popular y su conteo.
      */

    @GetMapping("/estadisticas/operacion-popular")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Object[]>> obtenerOperacionPopular() {
        return ResponseEntity.ok(transaccionRepositorio.operacionMasPopular());
    }
}