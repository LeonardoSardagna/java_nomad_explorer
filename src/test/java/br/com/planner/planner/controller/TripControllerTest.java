package br.com.planner.planner.controller;

import br.com.planner.planner.domain.trip.Trip;
import br.com.planner.planner.domain.trip.TripRequestDTO;
import br.com.planner.planner.domain.trip.TripResponseDTO;
import br.com.planner.planner.repository.TripRepository;
import br.com.planner.planner.service.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private TripService tripService;

    @Autowired
    private JacksonTester<TripRequestDTO> tripRequestDTOJacksonTester;

    @Autowired
    private JacksonTester<TripResponseDTO> tripResponseDTOJacksonTester;

    @Autowired
    private JacksonTester<Trip> tripJacksonTester;

    //método que é executado antes de todos os teste. Serve para preparação da API
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Retorna 200 quando cria o evento")
    void createTripTest() throws Exception {
        when(tripRepository.save(any())).thenReturn(new Trip(getCreateTripBody()));
        List<String> emails = List.of("valmirsardagna00@gmail.com");

        var response = mockMvc.perform(post("/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tripRequestDTOJacksonTester.write(new TripRequestDTO(
                        "Leonardo Sardagna",
                        "leonardotest@gmail.com",
                        "Balneário Camburiú, SC",
                        emails,
                        "2024-08-01T06:00:54.743Z",
                        "2024-08-10T12:00:54.743Z"
                )).getJson())).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void tripDetails() throws Exception {
        UUID id = UUID.randomUUID();
        Trip trip = new Trip(getCreateTripBody());
        trip.setId(id);

        when(tripRepository.save(any())).thenReturn(trip);

        var response = mockMvc.perform(get("/trips/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(tripJacksonTester.write(new Trip(
                        getCreateTripBody()
                )).getJson())).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response).isEqualTo(getCreateTripBody());
    }

    @Test
    void updateTrip() throws Exception {

    }

    @Test
    void confirmTrip() {
    }

    @Test
    void inviteParticipant() {
    }

    @Test
    void getAllParticipants() {
    }

    @Test
    void createActivity() {
    }

    @Test
    void getAllActivities() {
    }

    @Test
    void createLink() {
    }

    @Test
    void getAllLinks() {
    }

    //Criando dados request para a viagem
    private TripRequestDTO getCreateTripBody() {

        List<String> emails = List.of("valmirsardagna00@gmail.com");

        return new TripRequestDTO(
                "Leonardo Sardagna",
                "leonardotest@gmail.com",
                "Balneário Camburiú, SC",
                emails,
                "2024-08-01T06:00:54.743Z",
                "2024-08-10T12:00:54.743Z");
    }

    //Criando dados request para os links
    String getCreateLinkBody() {
        return "{\n" +
                "\t\"title\": \"Casa AirBnB: Paraíso Residencial com piscina em Blumenau/SC\",\n" +
                "\"url\":\"https://www.airbnb.com.br/rooms/963863438668882193_set_bev_on_new_domain=1718040006_MWUxMDk2NTc2YWVm&source_impression_id=p3_1718040006_P32WZ94NxT3USpE2\"\n" +
                "}";
    }

    //Criando dados request para a atividade
    String getCreateActivityBody() {
        return "{\n" +
                "\t\"title\": \"Praia\",\n" +
                "\t\"occurs_at\":\"2024-08-02T06:00:54.743Z\"\n" +
                "}";
    }

    //Request para Confirmar o convidado
    String getConfirmParticipant() {
        return "{\n" +
                "\"name\":\"Valmir Sardagna\",\n" +
                "\"email\": \"\"\n" +
                "}";
    }

    //Request para enviar o convite
    String getEnvite() {
        return "{\n" +
                "\t\"email\":\"marisardagna00@gmail.com\"\n" +
                "}";
    }
}