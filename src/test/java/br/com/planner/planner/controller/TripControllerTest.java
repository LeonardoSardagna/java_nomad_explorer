package br.com.planner.planner.controller;

import br.com.planner.planner.domain.trip.Trip;
import br.com.planner.planner.domain.trip.TripRequestDTO;
import br.com.planner.planner.repository.TripRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.planner.planner.utils.MockCategories.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TripRepository tripRepository;

    @Autowired
    private JacksonTester<TripRequestDTO> tripRequestDTOJacksonTester;

    private ObjectMapper objectMapper = new ObjectMapper();

    //método que é executado antes de todos os teste. Serve para preparação da API
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Retorna 200 quando cria o evento")
    void createTripTestCase1() throws Exception {
        when(tripRepository.save(any(Trip.class))).thenReturn(mockTripEntity());
        String tripJson = objectMapper.writeValueAsString(mockTripDTO());

        var response = mockMvc.perform(post("/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tripJson)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Retorna 404 quando é não é informado algum dado")
    void createTripTestCase2() throws Exception {
        when(tripRepository.save(any(Trip.class))).thenReturn(mockTripEntity());
        String tripJson = objectMapper.writeValueAsString(new TripRequestDTO(null,
                null,
                "Balneário Camburiú, SC",
                List.of("teste@gmail.com", "newemail@example.com"),
                String.valueOf(LocalDateTime.parse("2024-08-01T06:00:54.743Z", DateTimeFormatter.ISO_DATE_TIME)),
                String.valueOf(LocalDateTime.parse("2024-08-10T12:00:54.743Z", DateTimeFormatter.ISO_DATE_TIME)))
        );

        var response = mockMvc.perform(post("/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tripJson)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Retorna 200 quando faz a atualização da viagem")
    void updateTripCase1() throws Exception {
        Trip newTrip = mockTripEntity();
        when(tripRepository.save(any(Trip.class))).thenReturn(newTrip);
        when(tripRepository.findById(newTrip.getId())).thenReturn(Optional.of(newTrip));

        String tripJson = objectMapper.writeValueAsString(mockTripDTO());

        var responseCreateTrip = mockMvc.perform(post("/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tripJson)).andReturn().getResponse();

        assertThat(responseCreateTrip.getStatus()).isEqualTo(HttpStatus.OK.value());

        TripRequestDTO updateRequest = mockUpdateTripDTO();

        JsonNode jsonNode = objectMapper.readTree(responseCreateTrip.getContentAsString());
        String tripId = jsonNode.get("id").asText();

        var responseUploadTrip = mockMvc.perform(put("/trips/" + tripId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tripRequestDTOJacksonTester.write(updateRequest).getJson()))
                .andReturn().getResponse();

        assertThat(responseUploadTrip.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseUploadTrip.getContentAsString()).contains("Leonardo Sardagna Updated");
        assertThat(responseUploadTrip.getContentAsString()).contains("leonardotestupdated@gmail.com");
    }

    @Test
    @DisplayName("Retorna 404 quando não encontra a viagem")
    void updateTripCase2() throws Exception {
        UUID uuid = UUID.randomUUID();
        TripRequestDTO updateRequest = mockUpdateTripDTO();

        var responseUploadTrip = mockMvc.perform(put("/trips/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tripRequestDTOJacksonTester.write(updateRequest).getJson()))
                .andReturn().getResponse();

        assertThat(responseUploadTrip.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Retorna 200 quando a viagem é excluída")
    void deleteTripTest() throws Exception {
        when(tripRepository.save(any(Trip.class))).thenReturn(mockTripEntity());
        String tripJson = objectMapper.writeValueAsString(mockTripDTO());

        var responseCreateTrip = mockMvc.perform(post("/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tripJson)).andReturn().getResponse();

        assertThat(responseCreateTrip.getStatus()).isEqualTo(HttpStatus.OK.value());

        JsonNode jsonNode = objectMapper.readTree(responseCreateTrip.getContentAsString());
        String tripId = jsonNode.get("id").asText();

        var responseDeleteTrip = mockMvc.perform(delete("/trips/" + tripId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(tripJson)).andReturn().getResponse();

        assertThat(responseDeleteTrip.getStatus()).isEqualTo(HttpStatus.OK.value());

        var checkRepository = mockMvc.perform(get("/trips/" + tripId)
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertThat(checkRepository.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Retorna 200 quando exibe os detalhes de uma determinada viagem")
    void tripDetailsTest() throws Exception {
        when(tripRepository.save(any(Trip.class))).thenReturn(mockTripEntity());
        String tripJson = objectMapper.writeValueAsString(mockTripDTO());

        var responseCreateTrip = mockMvc.perform(post("/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tripJson)).andReturn().getResponse();

        assertThat(responseCreateTrip.getStatus()).isEqualTo(HttpStatus.OK.value());

        JsonNode jsonNode = objectMapper.readTree(responseCreateTrip.getContentAsString());
        String tripId = jsonNode.get("id").asText();

        var responseDetailsTrip = mockMvc.perform(get("/trips/" + tripId)
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertThat(responseDetailsTrip.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseDetailsTrip.getContentAsString()).contains("Leonardo Sardagna");

    }

    @Test
    @DisplayName("Retorna 400 quando exibe há um conflito na formatação das datas")
    void conflictBetweenDatesTest() throws Exception {
        when(tripRepository.save(any(Trip.class))).thenReturn(mockTripEntity());
        String tripJson = objectMapper.writeValueAsString(mockTripDTO());

        var responseCreateTrip = mockMvc.perform(post("/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(new TripRequestDTO("Leonardo Sardagna Updated",
                        "leonardotestupdated@gmail.com",
                        "teste",
                        List.of("teste@gmail.com", "newemail@example.com"),
                        "2024-08-10T06:00:54.743Z",
                        "2024-07-02T06:00:54.743Z")))).andReturn().getResponse();

        assertThat(responseCreateTrip.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Retorna 200 quando confirma a viagem e envia os emails para os convidados")
    void confirmTripCase1() throws Exception {
        when(tripRepository.save(any(Trip.class))).thenReturn(mockTripEntity());
        String tripJson = objectMapper.writeValueAsString(mockTripDTO());

        var responseCreateTrip = mockMvc.perform(post("/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tripJson)).andReturn().getResponse();

        assertThat(responseCreateTrip.getStatus()).isEqualTo(HttpStatus.OK.value());

        JsonNode jsonNode = objectMapper.readTree(responseCreateTrip.getContentAsString());
        String tripId = jsonNode.get("id").asText();

        var ResponseConfirmTrip = mockMvc.perform(get("/trips/" + tripId + "/confirm")).andReturn().getResponse();
        assertThat(ResponseConfirmTrip.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Retorna 404 quando não encontra a viagem")
    void confirmTripCase2() throws Exception {
        var ResponseConfirmTrip = mockMvc.perform(get("/trips/" + UUID.randomUUID() + "/confirm")).andReturn().getResponse();
        assertThat(ResponseConfirmTrip.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Retorna 200 quando faz o envio do convite por email ao participante")
    void inviteParticipant() throws Exception {
        when(tripRepository.save(any(Trip.class))).thenReturn(mockTripEntity());
        String tripJson = objectMapper.writeValueAsString(mockTripDTO());

        var responseCreateTrip = mockMvc.perform(post("/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tripJson)).andReturn().getResponse();

        assertThat(responseCreateTrip.getStatus()).isEqualTo(HttpStatus.OK.value());

        JsonNode jsonNode = objectMapper.readTree(responseCreateTrip.getContentAsString());
        String tripId = jsonNode.get("id").asText();

        var ResponseConfirmTrip = mockMvc.perform(get("/trips/" + tripId + "/confirm")).andReturn().getResponse();

        assertThat(ResponseConfirmTrip.getStatus()).isEqualTo(HttpStatus.OK.value());

        String InviteParticipantDTO = objectMapper.writeValueAsString(mockInviteParticipantToTrip());

        var ResponseInviteParticipant = mockMvc.perform(post("/trips/" + tripId + "/invite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(InviteParticipantDTO)).andReturn().getResponse();

        assertThat(ResponseInviteParticipant.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Retorna 200 quando realiza a criação de uma atividade")
    void createActivity() throws Exception {
        when(tripRepository.save(any(Trip.class))).thenReturn(mockTripEntity());
        String tripJson = objectMapper.writeValueAsString(mockTripDTO());

        var responseCreateTrip = mockMvc.perform(post("/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tripJson)).andReturn().getResponse();

        assertThat(responseCreateTrip.getStatus()).isEqualTo(HttpStatus.OK.value());

        JsonNode jsonNode = objectMapper.readTree(responseCreateTrip.getContentAsString());
        String tripId = jsonNode.get("id").asText();

        var ResponseConfirmTrip = mockMvc.perform(get("/trips/" + tripId + "/confirm")).andReturn().getResponse();

        assertThat(ResponseConfirmTrip.getStatus()).isEqualTo(HttpStatus.OK.value());

        String activityTrip = objectMapper.writeValueAsString(mockActivityRequestDTO());

        var responseCreateActivity = mockMvc.perform(post("/trips/" + tripId + "/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(activityTrip)).andReturn().getResponse();

        JsonNode jsonNode2 = objectMapper.readTree(responseCreateActivity.getContentAsString());
        String activityId = jsonNode2.get("id").asText();

        assertThat(responseCreateActivity.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseCreateActivity.getContentAsString()).contains(activityId);
    }

    @Test
    @DisplayName("Retorna 200 quando realiza a criação de uma Link importante para o evento")
    void createLink() throws Exception {
        when(tripRepository.save(any(Trip.class))).thenReturn(mockTripEntity());
        String tripJson = objectMapper.writeValueAsString(mockTripDTO());

        var responseCreateTrip = mockMvc.perform(post("/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tripJson)).andReturn().getResponse();

        assertThat(responseCreateTrip.getStatus()).isEqualTo(HttpStatus.OK.value());

        JsonNode jsonNode = objectMapper.readTree(responseCreateTrip.getContentAsString());
        String tripId = jsonNode.get("id").asText();

        var ResponseConfirmTrip = mockMvc.perform(get("/trips/" + tripId + "/confirm")).andReturn().getResponse();

        assertThat(ResponseConfirmTrip.getStatus()).isEqualTo(HttpStatus.OK.value());

        String linkTrip = objectMapper.writeValueAsString(mockLinkRequestDTO());

        var responseCreateActivity = mockMvc.perform(post("/trips/" + tripId + "/links")
                .contentType(MediaType.APPLICATION_JSON)
                .content(linkTrip)).andReturn().getResponse();

        JsonNode jsonNode2 = objectMapper.readTree(responseCreateActivity.getContentAsString());
        String activityId = jsonNode2.get("id").asText();

        assertThat(responseCreateActivity.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseCreateActivity.getContentAsString()).contains(activityId);
    }
}