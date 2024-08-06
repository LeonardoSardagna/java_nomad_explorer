package br.com.planner.planner.utils;

import br.com.planner.planner.domain.activity.ActivityRequestDTO;
import br.com.planner.planner.domain.link.LinkRequestDTO;
import br.com.planner.planner.domain.participant.ParticipantRequestDTO;
import br.com.planner.planner.domain.trip.Trip;
import br.com.planner.planner.domain.trip.TripRequestDTO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MockCategories {

    //Dados da viagem
    static String OWNERNAME = "Leonardo Sardagna";
    static String OWNEREMAIL = "leonardotest@gmail.com";
    static String DESTINATION = "Balneário Camburiú, SC";
    static List<String> EMAILS = List.of("teste@gmail.com", "newemail@example.com");
    static LocalDateTime STARTS_AT = LocalDateTime.parse("2024-08-01T06:00:54.743Z", DateTimeFormatter.ISO_DATE_TIME);
    static LocalDateTime END_AT = LocalDateTime.parse("2024-08-10T12:00:54.743Z", DateTimeFormatter.ISO_DATE_TIME);

    //Dados links
    static String LINK_TITLE = "Casa AirBnB: Paraíso Residencial com piscina em Blumenau/SC";
    static String URL = "https://www.airbnb.com.br/rooms/963863438668882193_set_bev_on_new_domain=1718040006_MWUxMDk2NTc2YWVm&source_impression_id=p3_1718040006_P32WZ94NxT3USpE2";

    //Dados atividade
    static String ACTIVITY_TITLE = "Praia";
    static String OCCURS_AT = "2024-08-02T06:00:54.743Z";

    //Mocando dados request para a viagem
    public static TripRequestDTO mockTripDTO() {
        return new TripRequestDTO(
                OWNERNAME,
                OWNEREMAIL,
                DESTINATION,
                EMAILS,
                String.valueOf(STARTS_AT),
                String.valueOf(END_AT));
    }

    //Mocando dados request para a viagem
    public static TripRequestDTO mockUpdateTripDTO() {
        return new TripRequestDTO(
                "Leonardo Sardagna Updated",
                "leonardotestupdated@gmail.com",
                null,
                null,
                null,
                null);
    }

    public static Trip mockTripEntity() {
        return new Trip(mockTripDTO());
    }

    public static ParticipantRequestDTO mockInviteParticipantToTrip() {
        return new ParticipantRequestDTO(null, "leonardosardagna00@gmail.com");
    }

    public static ParticipantRequestDTO mockConfirmParticipantToTrip() {
        return new ParticipantRequestDTO("Leonardo Sardagna", "");
    }

    public static ActivityRequestDTO mockActivityRequestDTO() {
        return new ActivityRequestDTO(ACTIVITY_TITLE, OCCURS_AT);
    }

    public static LinkRequestDTO mockLinkRequestDTO() {
        return new LinkRequestDTO(LINK_TITLE, URL);
    }
}
