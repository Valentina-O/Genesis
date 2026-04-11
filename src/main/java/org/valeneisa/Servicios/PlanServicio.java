package org.valeneisa.Servicios;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.valeneisa.tokens.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServicio {

    private final IPlanRepositorio planRepositorio;
    private final ISuscripcionRepositorio suscripcionRepositorio;

    // 🔹 Crear plan
    public Plan crearPlan(Plan plan) {
        plan.setEstaActivo(true);
        return planRepositorio.save(plan);
    }

    // 🔹 Listar planes
    public List<Plan> listarPlanes() {
        return planRepositorio.findAll();
    }

    // 🔹 Buscar por id
    public Plan obtenerPlan(Long id) {
        return planRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));
    }

    // 🔹 Actualizar plan
    public Plan actualizarPlan(Long id, Plan nuevoPlan) {
        Plan plan = obtenerPlan(id);

        plan.setNombre(nuevoPlan.getNombre());
        plan.setTokensOtorgados(nuevoPlan.getTokensOtorgados());
        plan.setEstaActivo(nuevoPlan.getEstaActivo());

        return planRepositorio.save(plan);
    }

    // 🔥 Eliminar plan (con validación)
    public void eliminarPlan(Long id) {
        Plan plan = obtenerPlan(id);

        boolean tieneSuscripcionesActivas = plan.getSuscripciones()
                .stream()
                .anyMatch(Suscripcion::getEstaActiva);

        if (tieneSuscripcionesActivas) {
            throw new RuntimeException("No se puede eliminar el plan porque tiene suscripciones activas");
        }

        planRepositorio.delete(plan);
    }
}