package org.valeneisa.Servicios;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.valeneisa.Core.IOperacion;
import org.valeneisa.Dtos.UserProfileResponse;
import org.valeneisa.tokens.*;
import org.valeneisa.usuario.entidad.Usuario;
import org.valeneisa.usuario.repositorio.IUsuarioRepositorio;
import org.valeneisa.Operaciones.Operacion;
import org.valeneisa.Operaciones.IOperacionRepositorio;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUsuarioRepositorio usuarioRepo;
    private final IPlanRepositorio planRepo;
    private final ISuscripcionRepositorio suscripcionRepo;
    private final ITransaccionRepositorio transaccionRepo;
    private final IOperacionRepositorio operacionRepo;
    private final ServicioToken servicioToken;

    // 🔹 PERFIL (DTO)
    public UserProfileResponse getProfile(String username) {

        Usuario u = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserProfileResponse res = new UserProfileResponse();
        res.setIdUsuario(u.getIdUsuario());
        res.setUsername(u.getUsername());
        res.setCorreoElectronico(u.getCorreoElectronico());
        res.setRolUsuario(u.getRolUsuario().name());
        res.setTokensDisponibles(u.getTokensDisponibles());
        res.setEstaActivo(u.getEstaActivo());

        // 🔥 PLAN ACTIVO (si existe)
        suscripcionRepo.findByUsuarioAndEstaActivaTrue(u)
                .ifPresent(s -> res.setPlanActivo(s.getPlan().getNombre()));

        return res;
    }

    // 🔹 HISTORIAL (PAGINADO)
    public List<Transaccion> getTransactions(String username, int page, int size) {

        Usuario usuario = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return transaccionRepo.findByUsuario(
                usuario,
                PageRequest.of(page, size)
        ).getContent();
    }

    // 🔹 CATÁLOGO DE OPERACIONES ACTIVAS
    public List<Operacion> getCatalogo() {
        return operacionRepo.findByEstaActivaTrue();
    }

    // 🔹 SUSCRIPCIÓN A PLAN
    public String subscribe(String username, Long planId) {

        Usuario usuario = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Plan plan = planRepo.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan no existe"));

        if (!plan.getEstaActivo()) {
            throw new RuntimeException("Plan inactivo");
        }

        // 🔥 Desactivar suscripción anterior
        suscripcionRepo.findByUsuarioAndEstaActivaTrue(usuario)
                .ifPresent(s -> {
                    s.setEstaActiva(false);
                    suscripcionRepo.save(s);
                });

        // 🔥 Crear nueva suscripción
        Suscripcion nueva = new Suscripcion();
        nueva.setUsuario(usuario);
        nueva.setPlan(plan);
        nueva.setFechaInicio(LocalDateTime.now());
        nueva.setEstaActiva(true);

        suscripcionRepo.save(nueva);

        // 🔥 Sumar tokens del plan
        usuario.setTokensDisponibles(
                usuario.getTokensDisponibles() + plan.getTokensOtorgados()
        );

        usuarioRepo.save(usuario);

        return "Suscripción exitosa";
    }

    // 🔹 EJECUTAR OPERACIÓN (TOKENS + TRANSACCIÓN)
    public Object ejecutarOperacion(String username, Object request, IOperacion operacion) {

        Usuario usuario = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 🔥 Ejecutar operación
        Object respuesta = operacion.ejecutar(request);

        // 🔥 Calcular costo
        int costo = servicioToken.calcularCostoTotal(
                operacion.obtenerCostoBase(),
                request,
                respuesta
        );

        if (usuario.getTokensDisponibles() < costo) {
            throw new RuntimeException("No tienes tokens suficientes");
        }

        // 🔥 Descontar tokens
        usuario.setTokensDisponibles(usuario.getTokensDisponibles() - costo);
        usuarioRepo.save(usuario);

        // 🔥 Buscar operación en BD (IMPORTANTE)
        Operacion op = operacionRepo
                .findByNombre(operacion.getClass().getSimpleName())
                .orElse(null); // si no existe, no rompe

        // 🔥 Guardar transacción
        Transaccion t = new Transaccion();
        t.setUsuario(usuario);
        t.setOperacion(op);
        t.setTokensConsumidos(costo);
        t.setFecha(LocalDateTime.now());

        transaccionRepo.save(t);

        return respuesta;
    }
}