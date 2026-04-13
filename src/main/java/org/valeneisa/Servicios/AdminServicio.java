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

/**
 * Servicio encargado de gestionar funcionalidades administrativas del sistema.
 * Permite administrar usuarios, tokens, tasas de cambio y operaciones.
 */
@Service
public class AdminServicio{

    /**
     * Repositorio para gestionar usuarios.
     */
    @Autowired
    private IUsuarioRepositorio usuarioRepositorio;

    /**
     * Repositorio para gestionar la tasa de cambio.
     */
    @Autowired
    private ITasaCambioRepositorio tasaCambioRepositorio;

    /**
     * Repositorio para gestionar las operaciones del sistema.
     */
    @Autowired
    private IOperacionRepositorio operacionRepositorio;

    /**
     * Activa o desactiva un usuario.
     *
     * @param id Identificador del usuario.
     * @param estado Estado a establecer (true = activo, false = inactivo).
     */
    @Transactional
    public void cambiarEstadoUsuario(Long id, Boolean estado) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEstaActivo(estado);
        usuarioRepositorio.save(usuario);
    }

    /**
     * Recarga manualmente tokens a un usuario.
     *
     * @param idUsuario Identificador del usuario.
     * @param cantidad Cantidad de tokens a agregar.
     */
    @Transactional
    public void recargarTokens(Long idUsuario, Integer cantidad) {
        Usuario usuario = usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar si es null antes de sumar
        Integer saldoActual = usuario.getTokensDisponibles() != null ? usuario.getTokensDisponibles() : 0;
        usuario.setTokensDisponibles(saldoActual + cantidad);

        usuarioRepositorio.save(usuario);
    }

    /**
     * Actualiza la tasa de cambio COP/USD en el sistema.
     *
     * @param nuevoValor Nuevo valor de la tasa de cambio.
     */
    @Transactional
    public void actualizarTasaCambio(Double nuevoValor) {
        TasaCambio tasa = tasaCambioRepositorio.findById(1L)
                .orElse(new TasaCambio());
        tasa.setValor(nuevoValor);
        tasa.setFechaActualizacion(LocalDateTime.now());
        tasaCambioRepositorio.save(tasa);
    }

    /**
     * Activa o desactiva una operación del catálogo.
     *
     * @param codigo Código de la operación.
     * @param activo Estado a establecer (true = activa, false = inactiva).
     */
    @Transactional
    public void cambiarEstadoOperacion(String codigo, Boolean activo) {
        Operacion op = operacionRepositorio.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Operación no encontrada"));
        op.setEstaActiva(activo);
        operacionRepositorio.save(op);
    }
}