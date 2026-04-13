package org.valeneisa.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.valeneisa.Servicios.AdminServicio;
import org.valeneisa.tokens.ITransaccionRepositorio;
import org.valeneisa.usuario.entidad.Usuario;
import org.valeneisa.usuario.repositorio.IUsuarioRepositorio;

@RestController
@RequestMapping("/api/admin")
public class AdminControlador {

    @Autowired
    private AdminServicio adminServicio;

    @Autowired
    private IUsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ITransaccionRepositorio transaccionRepositorio;
    
    // Consultar listado de usuarios con saldo y estado (PAGINADO)
    @GetMapping("/usuarios")
    public ResponseEntity<Page<Usuario>> listarUsuarios(Pageable pageable) {
        return ResponseEntity.ok(usuarioRepositorio.findAll(pageable));
    }

    // Activar o desactivar usuario [cite: 17]
    @PatchMapping("/usuarios/{id}/estado")
    public ResponseEntity<String> cambiarEstado(@PathVariable Long id, @RequestParam Boolean activo) {
        adminServicio.cambiarEstadoUsuario(id, activo);
        return ResponseEntity.ok("Estado actualizado exitosamente");
    }

    // Recargar tokens manualmente [cite: 18]
    @PostMapping("/usuarios/{id}/recargar")
    public ResponseEntity<String> recargar(@PathVariable Long id, @RequestBody Integer cantidad) {
        adminServicio.recargarTokens(id, cantidad);
        return ResponseEntity.ok("Tokens recargados correctamente");
    }

    // Actualizar tasa de cambio [cite: 21]
    @PutMapping("/tasa-cambio")
    public ResponseEntity<String> actualizarTasa(@RequestBody Double valor) {
        adminServicio.actualizarTasaCambio(valor);
        return ResponseEntity.ok("Tasa actualizada correctamente");
    }

    // Activar o desactivar una operación del catálogo (OP-01, OP-02, etc.)
    @PatchMapping("/operaciones/{codigo}/estado")
    public ResponseEntity<String> cambiarEstadoOperacion(@PathVariable String codigo, @RequestParam Boolean activo) {
        adminServicio.cambiarEstadoOperacion(codigo, activo);
        return ResponseEntity.ok("El estado de la operación " + codigo + " ha sido actualizado");
    }

    @GetMapping("/metricas/hoy")
    public ResponseEntity<Integer> obtenerConsumoHoy() {
        return ResponseEntity.ok(transaccionRepositorio.consumoTotalHoy());
    }
}