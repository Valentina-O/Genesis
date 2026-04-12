package org.valeneisa.Controladores;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // IMPORTANTE: Debe ser este
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Servicios.PlanServicio;
import org.valeneisa.tokens.Plan;

@RestController
@RequestMapping("/api/planes") // Agregamos /api por buena práctica
@RequiredArgsConstructor
public class PlanControlador {

    private final PlanServicio planServicio;

    // 🔹 Crear un nuevo plan
    @PostMapping
    public ResponseEntity<Plan> crear(@RequestBody Plan plan) {
        return ResponseEntity.ok(planServicio.crearPlan(plan));
    }

    // 🔹 Listar planes con PAGINACIÓN (Requisito del profesor)
    @GetMapping
    public ResponseEntity<Page<Plan>> listar(Pageable pageable) {
        return ResponseEntity.ok(planServicio.listarPlanesPaginados(pageable));
    }

    // 🔹 Obtener un plan por ID
    @GetMapping("/{id}")
    public ResponseEntity<Plan> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(planServicio.obtenerPlan(id));
    }

    // 🔹 Actualizar un plan existente
    @PutMapping("/{id}")
    public ResponseEntity<Plan> actualizar(@PathVariable Long id, @RequestBody Plan plan) {
        return ResponseEntity.ok(planServicio.actualizarPlan(id, plan));
    }

    // 🔹 Eliminar un plan (incluye la validación de suscripciones)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        planServicio.eliminarPlan(id);
        return ResponseEntity.noContent().build();
    }
}