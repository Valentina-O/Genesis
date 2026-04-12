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

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioUsuario {

    private final IUsuarioRepositorio usuarioRepo;
    private final IPlanRepositorio planRepo;
    private final ISuscripcionRepositorio suscripcionRepo;
    private final ITransaccionRepositorio transaccionRepo;
    private final IOperacionRepositorio operacionRepo;
    private final ServicioToken servicioToken;

    // 🔹 PERFIL
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
                .ifPresent(s -> res.setPlanActivo(s.getPlan().getNombre()));

        return res;
    }

    // 🔹 HISTORIAL
    public List<Transaccion> getTransactions(String usuario, int page, int size) {

        Usuario u = usuarioRepo.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return transaccionRepo.findByUsuario(
                u,
                PageRequest.of(page, size)
        ).getContent();
    }

    // 🔹 CATÁLOGO
    public List<Operacion> getCatalogo() {
        return operacionRepo.findByEstaActivaTrue();
    }

    // 🔹 SUSCRIPCIÓN
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

        u.setTokensDisponibles(
                u.getTokensDisponibles() + plan.getTokensOtorgados()
        );

        usuarioRepo.save(u);

        return "Suscripción exitosa";
    }

    // 🔹 OPERACIÓN
    public Object ejecutarOperacion(String usuario, Object request, org.valeneisa.Core.IOperacion operacion) {

        Usuario u = usuarioRepo.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Object respuesta = operacion.ejecutar(request);

        int costo = servicioToken.calcularCostoTotal(
                operacion.obtenerCostoBase(),
                request,
                respuesta
        );

        if (u.getTokensDisponibles() < costo) {
            throw new RuntimeException("No tienes tokens suficientes");
        }

        u.setTokensDisponibles(u.getTokensDisponibles() - costo);
        usuarioRepo.save(u);

        Operacion op = operacionRepo
                .findByNombre(operacion.getClass().getSimpleName())
                .orElse(null);

        Transaccion t = new Transaccion();
        t.setUsuario(u);
        t.setOperacion(op);
        t.setTokensConsumidos(costo);
        t.setFecha(LocalDateTime.now());

        transaccionRepo.save(t);

        return respuesta;
    }
}