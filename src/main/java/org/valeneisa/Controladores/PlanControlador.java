package org.valeneisa.Controladores;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Servicios.PlanServicio;
import org.valeneisa.tokens.Plan;

/**
 * Controlador REST para la gestión del catálogo de planes de la plataforma.
 * <p>
 * Expone endpoints bajo el prefijo {@code /api/planes} que permiten crear,
 * consultar, actualizar y eliminar planes. El listado soporta paginación
 * mediante {@link Pageable}.
 * </p>
 *
 * @see PlanServicio
 */
@RestController
@RequestMapping("/api/planes")
@RequiredArgsConstructor
public class PlanControlador {

    private final PlanServicio planServicio;

    /**
     * Crea un nuevo plan en el sistema.
     *
     * @param plan objeto {@link Plan} con los datos del plan a registrar.
     * @return {@link ResponseEntity} con estado {@code 200 OK} y el {@link Plan} creado.
     */
    @PostMapping
    public ResponseEntity<Plan> crear(@RequestBody Plan plan) {
        return ResponseEntity.ok(planServicio.crearPlan(plan));
    }

    /**
     * Retorna el listado paginado de todos los planes disponibles en la plataforma.
     *
     * @param pageable parámetros de paginación y ordenamiento.
     * @return {@link ResponseEntity} con estado {@code 200 OK} y una {@link Page} de {@link Plan}.
     */
    @GetMapping
    public ResponseEntity<Page<Plan>> listar(Pageable pageable) {
        return ResponseEntity.ok(planServicio.listarPlanesPaginados(pageable));
    }

    /**
     * Retorna un plan específico por su identificador único.
     *
     * @param id identificador único del plan a consultar.
     * @return {@link ResponseEntity} con estado {@code 200 OK} y el {@link Plan} encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Plan> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(planServicio.obtenerPlan(id));
    }

    /**
     * Actualiza los datos de un plan existente.
     *
     * @param id   identificador único del plan a actualizar.
     * @param plan objeto {@link Plan} con los nuevos datos a aplicar.
     * @return {@link ResponseEntity} con estado {@code 200 OK} y el {@link Plan} actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Plan> actualizar(@PathVariable Long id, @RequestBody Plan plan) {
        return ResponseEntity.ok(planServicio.actualizarPlan(id, plan));
    }

    /**
     * Elimina un plan del sistema por su identificador único.
     * <p>
     * Incluye validación de suscripciones activas antes de proceder con la eliminación.
     * </p>
     *
     * @param id identificador único del plan a eliminar.
     * @return {@link ResponseEntity} con estado {@code 204 No Content} si la eliminación fue exitosa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        planServicio.eliminarPlan(id);
        return ResponseEntity.noContent().build();
    }
}