package br.pucpr.checkinexpress.repository;

import br.pucpr.checkinexpress.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}
