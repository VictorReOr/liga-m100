package com.antigravity.ligam100.modules.event.repository;

import com.antigravity.ligam100.modules.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByActivoTrue();
}
