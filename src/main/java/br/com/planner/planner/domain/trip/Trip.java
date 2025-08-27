package br.com.planner.planner.domain.trip;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Table(name = "trips")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "owner_name", nullable = false)
    private String ownerName;
    @Column(name = "owner_email", nullable = false)
    private String ownerEmail;
    @Column(nullable = false)
    private String destination;
    @Column(name = "starts_at", nullable = false)
    private LocalDate startsAt;
    @Column(name = "ends_at", nullable = false)
    private LocalDate endsAt;
    @Column(name = "is_confirmed", nullable = false, columnDefinition = "BOOLEAN")
    private Boolean isConfirmed;

    public Trip(TripRequestDTO data) {
        this.ownerName = data.ownerName();
        this.ownerEmail = data.ownerEmail();
        this.destination = data.destination();
        this.startsAt = LocalDate.parse(data.starts_at().substring(0, 10));
        this.endsAt = LocalDate.parse(data.ends_at().substring(0, 10));
        this.isConfirmed = false;
    }
}
