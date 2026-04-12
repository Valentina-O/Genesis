package org.valeneisa.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.valeneisa.usuario.entidad.Usuario;
import org.valeneisa.usuario.repositorio.IUsuarioRepositorio;
import org.valeneisa.Core.TasaCambio;
import org.valeneisa.Core.ITasaCambioRepositorio; // Debes crear este repo en Core
import org.valeneisa.Operaciones.Operacion;
import org.valeneisa.Operaciones.IOperacionRepositorio; // Debes crear este repo en Operaciones

import java.time.LocalDateTime;

@Service
public class AdminServicio{

    @Autowired
    private IUsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ITasaCambioRepositorio tasaCambioRepositorio;

    @Autowired
    private IOperacionRepositorio operacionRepositorio;

    // Activar o desactivar un usuario [cite: 17]
    @Transactional
    public void cambiarEstadoUsuario(Long id, Boolean estado) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEstaActivo(estado);
        usuarioRepositorio.save(usuario);
    }

    // Recargar tokens manualmente [cite: 18, 33]
    @Transactional
    public void recargarTokens(Long idUsuario, Integer cantidad) {
        Usuario usuario = usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar si es null antes de sumar
        Integer saldoActual = usuario.getTokensDisponibles() != null ? usuario.getTokensDisponibles() : 0;
        usuario.setTokensDisponibles(saldoActual + cantidad);

        usuarioRepositorio.save(usuario);
    }
    // Actualizar la tasa de cambio COP/USD [cite: 21, 73]
    @Transactional
    public void actualizarTasaCambio(Double nuevoValor) {
        TasaCambio tasa = tasaCambioRepositorio.findById(1L)
                .orElse(new TasaCambio());
        tasa.setValor(nuevoValor);
        tasa.setFechaActualizacion(LocalDateTime.now());
        tasaCambioRepositorio.save(tasa);
    }

    // Activar o desactivar una operación del catálogo [cite: 20]
    @Transactional
    public void cambiarEstadoOperacion(String codigo, Boolean activo) {
        Operacion op = operacionRepositorio.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Operación no encontrada"));
        op.setEstaActiva(activo);
        operacionRepositorio.save(op);
    }
}