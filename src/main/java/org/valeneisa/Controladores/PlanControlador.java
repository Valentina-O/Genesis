package org.valeneisa.Controladores;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Servicios.PlanServicio;
import org.valeneisa.tokens.Plan;

import java.util.List;

@RestController
@RequestMapping("/planes")
@RequiredArgsConstructor
public class PlanControlador {

    private final PlanServicio planServicio;

    @PostMapping
    public Plan crear(@RequestBody Plan plan) {
        return planServicio.crearPlan(plan);
    }

    @GetMapping
    public List<Plan> listar() {
        return planServicio.listarPlanes();
    }

    @GetMapping("/{id}")
    public Plan obtener(@PathVariable Long id) {
        return planServicio.obtenerPlan(id);
    }

    @PutMapping("/{id}")
    public Plan actualizar(@PathVariable Long id, @RequestBody Plan plan) {
        return planServicio.actualizarPlan(id, plan);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        planServicio.eliminarPlan(id);
    }
}