package org.valeneisa.Servicios;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.valeneisa.tokens.IPlanRepositorio;
import org.valeneisa.tokens.ISuscripcionRepositorio;
import org.valeneisa.tokens.Plan;
import org.valeneisa.tokens.Suscripcion;
import java.util.List;

/**
 * Servicio encargado de la gestión de planes del sistema.
 * Permite crear, listar, actualizar y eliminar planes,
 * incluyendo validaciones relacionadas con suscripciones activas.
 */
@Service
@RequiredArgsConstructor
public class PlanServicio {

    /**
     * Repositorio para gestionar planes.
     */
    private final IPlanRepositorio planRepositorio;

    /**
     * Repositorio para gestionar suscripciones.
     */
    private final ISuscripcionRepositorio suscripcionRepositorio;

    /**
     * Crea un nuevo plan y lo establece como activo por defecto.
     *
     * @param plan Plan a crear.
     * @return Plan guardado.
     */
    public Plan crearPlan(Plan plan) {
        plan.setEstaActivo(true);
        return planRepositorio.save(plan);
    }

    /**
     * Lista todos los planes disponibles.
     *
     * @return Lista de planes.
     */
    public List<Plan> listarPlanes() {
        return planRepositorio.findAll();
    }

    /**
     * Obtiene un plan por su identificador.
     *
     * @param id Identificador del plan.
     * @return Plan encontrado.
     */
    public Plan obtenerPlan(Long id) {
        return planRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));
    }

    /**
     * Lista los planes de forma paginada.
     *
     * @param pageable Configuración de paginación.
     * @return Página de planes.
     */
    public Page<Plan> listarPlanesPaginados(Pageable pageable) {
        return planRepositorio.findAll(pageable);
    }

    /**
     * Actualiza un plan existente con nueva información.
     *
     * @param id Identificador del plan.
     * @param nuevoPlan Datos actualizados del plan.
     * @return Plan actualizado.
     */
    public Plan actualizarPlan(Long id, Plan nuevoPlan) {
        Plan plan = obtenerPlan(id);

        plan.setNombre(nuevoPlan.getNombre());
        plan.setTokensOtorgados(nuevoPlan.getTokensOtorgados());
        plan.setEstaActivo(nuevoPlan.getEstaActivo());

        return planRepositorio.save(plan);
    }

    /**
     * Elimina un plan si no tiene suscripciones activas asociadas.
     *
     * @param id Identificador del plan.
     */
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