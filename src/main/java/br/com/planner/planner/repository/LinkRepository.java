package br.com.planner.planner.repository;

import br.com.planner.planner.domain.link.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface LinkRepository extends JpaRepository<Link, UUID> {
    List<Link> findByTripId(UUID id);
}
