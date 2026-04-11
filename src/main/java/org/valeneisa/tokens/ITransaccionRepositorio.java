package org.valeneisa.tokens;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITransaccionRepositorio extends JpaRepository<Transaccion, Long> {
}