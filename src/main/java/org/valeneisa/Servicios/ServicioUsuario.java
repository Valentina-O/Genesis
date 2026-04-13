package org.valeneisa.Servicios;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.valeneisa.Dtos.RespuestaPerfilUsuario;
import org.valeneisa.Operaciones.IOperacionRepositorio;
import org.valeneisa.Operaciones.Operacion;
import org.valeneisa.tokens.*;
import org.valeneisa.usuario.entidad.Usuario;
import org.valeneisa.usuario.repositorio.IUsuarioRepositorio;
import org.valeneisa.Core.IOperacion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;

/**
 * Servicio encargado de la gestión de funcionalidades del usuario.
 * Incluye perfil, historial de transacciones, catálogo de operaciones,
 * suscripciones y ejecución de operaciones con control de tokens.
 */
@Service
@RequiredArgsConstructor
public class ServicioUsuario {

    /**
     * Repositorio de usuarios.
     */
    private final IUsuarioRepositorio usuarioRepo;

    /**
     * Repositorio de planes.
     */
    private final IPlanRepositorio planRepo;

    /**
     * Repositorio de suscripciones.
     */
    private final ISuscripcionRepositorio suscripcionRepo;

    /**
     * Repositorio de transacciones.
     */
    private final ITransaccionRepositorio transaccionRepo;

    /**
     * Repositorio de operaciones.
     */
    private final IOperacionRepositorio operacionRepo;

    /**
     * Servicio encargado de la lógica de cálculo de tokens.
     */
    private final TokenServicio tokenServicio;

    // --- PERFIL ---

    /**
     * Obtiene la información del perfil de un usuario.
     *
     * @param usuario Nombre del usuario.
     * @return Información del perfil.
     */
    public RespuestaPerfilUsuario getProfile(String usuario) {
        Usuario u = usuarioRepo.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        RespuestaPerfilUsuario res = new RespuestaPerfilUsuario();
        res.setIdUsuario(u.getIdUsuario());
        res.setUsuario(u.getUsuario());
        res.setCorreoElectronico(u.getCorreoElectronico());
        res.setRolUsuario(u.getRolUsuario().name());
        res.setTokensDisponibles(u.getTokensDisponibles());
        res.setEstaActivo(u.getEstaActivo());

        suscripcionRepo.findByUsuarioAndEstaActivaTrue(u)
                .ifPresentOrElse(
                        s -> res.setPlanActivo(s.getPlan().getNombre()),
                        () -> res.setPlanActivo("SIN PLAN")
                );

        return res;
    }

    // --- HISTORIAL ---

    /**
     * Obtiene el historial de transacciones de un usuario de forma paginada.
     *
     * @param usuario Nombre del usuario.
     * @param page Número de página.
     * @param size Tamaño de la página.
     * @return Lista de transacciones.
     */
    public List<Transaccion> getTransactions(String usuario, int page, int size) {
        page = Math.max(page, 0);
        size = Math.min(Math.max(size, 1), 50);

        Usuario u = usuarioRepo.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return transaccionRepo.findByUsuario(
                u,
                PageRequest.of(page, size)
        ).getContent();
    }

    // --- CATÁLOGO ---

    /**
     * Obtiene el catálogo de operaciones activas.
     *
     * @return Lista de operaciones activas.
     */
    public List<Operacion> getCatalogo() {
        List<Operacion> ops = operacionRepo.findByEstaActivaTrue();
        return ops != null ? ops : new ArrayList<>();
    }

    // --- SUSCRIPCIÓN ---

    /**
     * Permite a un usuario suscribirse a un plan.
     * Si ya tiene una suscripción activa, la desactiva.
     *
     * @param usuario Nombre del usuario.
     * @param planId Identificador del plan.
     * @return Mensaje de resultado.
     */
    public String subscribe(String usuario, Long planId) {
        Usuario u = usuarioRepo.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Plan plan = planRepo.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan no existe"));

        if (!plan.getEstaActivo()) {
            throw new RuntimeException("Plan inactivo");
        }

        suscripcionRepo.findByUsuarioAndEstaActivaTrue(u)
                .ifPresent(s -> {
                    s.setEstaActiva(false);
                    suscripcionRepo.save(s);
                });

        Suscripcion nueva = new Suscripcion();
        nueva.setUsuario(u);
        nueva.setPlan(plan);
        nueva.setFechaInicio(LocalDateTime.now());
        nueva.setEstaActiva(true);

        suscripcionRepo.save(nueva);

        int actuales = u.getTokensDisponibles() == null ? 0 : u.getTokensDisponibles();
        u.setTokensDisponibles(actuales + plan.getTokensOtorgados());

        usuarioRepo.save(u);
        return "Suscripción exitosa";
    }

    // --- OPERACIÓN GENÉRICA ---

    /**
     * Ejecuta una operación genérica, valida tokens, registra la transacción
     * y agrega el costo consumido a la respuesta si es posible.
     *
     * @param usuario Nombre del usuario.
     * @param request Solicitud de la operación.
     * @param calculadora Implementación de la operación.
     * @return Resultado de la operación.
     */
    public <T_REQ, T_RES> T_RES ejecutarOperacion(
            String usuario,
            T_REQ request,
            IOperacion<T_REQ, T_RES> calculadora
    ) {
        Usuario u = usuarioRepo.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        T_RES respuesta = calculadora.ejecutar(request);

        // Corregido: Llamada usando tokenServicio
        int costo = tokenServicio.calcularCostoTotal(
                calculadora.obtenerCostoBase(),
                request,
                respuesta
        );

        if (u.getTokensDisponibles() < costo) {
            throw new RuntimeException("No tienes tokens suficientes. Costo: " + costo);
        }

        u.setTokensDisponibles(u.getTokensDisponibles() - costo);
        usuarioRepo.save(u);

        Operacion opEntidad = operacionRepo
                .findByCodigo(calculadora.obtenerCodigoOp())
                .orElseThrow(() -> new RuntimeException("Operación no configurada en BD"));

        Transaccion t = new Transaccion();
        t.setUsuario(u);
        t.setOperacion(opEntidad);
        t.setTokensConsumidos(costo);
        t.setFecha(LocalDateTime.now());
        transaccionRepo.save(t);

        try {
            Method method = respuesta.getClass().getMethod("setTokensConsumidos", int.class);
            method.invoke(respuesta, costo);
        } catch (Exception ignored) {
        }

        return respuesta;
    }
}