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
import org.valeneisa.Core.IOperacion; // Importamos la interfaz del core

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;

@Service
@RequiredArgsConstructor
public class ServicioUsuario {

    private final IUsuarioRepositorio usuarioRepo;
    private final IPlanRepositorio planRepo;
    private final ISuscripcionRepositorio suscripcionRepo;
    private final ITransaccionRepositorio transaccionRepo;
    private final IOperacionRepositorio operacionRepo;
    private final ServicioToken servicioToken;

    // --- PERFIL ---
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
    public List<Operacion> getCatalogo() {
        List<Operacion> ops = operacionRepo.findByEstaActivaTrue();
        return ops != null ? ops : new ArrayList<>();
    }

    // --- SUSCRIPCIÓN ---
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

    // --- OPERACIÓN GENÉRICA (CORREGIDA) ---
    public <T_REQ, T_RES> T_RES ejecutarOperacion(
            String usuario,
            T_REQ request,
            IOperacion<T_REQ, T_RES> calculadora // Usamos el nombre 'calculadora' para no confundir con la entidad
    ) {
        // 1. Validar Usuario
        Usuario u = usuarioRepo.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Ejecutar el cálculo
        T_RES respuesta = calculadora.ejecutar(request);

        // 3. Calcular costo
        int costo = servicioToken.calcularCostoTotal(
                calculadora.obtenerCostoBase(),
                request,
                respuesta
        );

        // 4. Validar saldo
        if (u.getTokensDisponibles() < costo) {
            throw new RuntimeException("No tienes tokens suficientes. Costo: " + costo);
        }

        // 5. Descontar y Guardar
        u.setTokensDisponibles(u.getTokensDisponibles() - costo);
        usuarioRepo.save(u);

        // 6. Registrar en Historial
        Operacion opEntidad = operacionRepo
                .findByCodigo(calculadora.obtenerCodigoOp())
                .orElseThrow(() -> new RuntimeException("Operación no configurada en BD: " + calculadora.obtenerCodigoOp()));

        Transaccion t = new Transaccion();
        t.setUsuario(u);
        t.setOperacion(opEntidad);
        t.setTokensConsumidos(costo);
        t.setFecha(LocalDateTime.now());
        transaccionRepo.save(t);

        // 7. (Opcional) Intentar inyectar el costo en la respuesta si tiene el método setTokensConsumidos
        try {
            Method method = respuesta.getClass().getMethod("setTokensConsumidos", int.class);
            method.invoke(respuesta, costo);
        } catch (Exception ignored) {
            // Si la respuesta no tiene el campo de tokens, simplemente ignoramos
        }

        return respuesta;
    }
}