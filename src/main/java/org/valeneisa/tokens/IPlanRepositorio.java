package org.valeneisa.tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlanRepositorio extends JpaRepository<Plan, Long> {
}