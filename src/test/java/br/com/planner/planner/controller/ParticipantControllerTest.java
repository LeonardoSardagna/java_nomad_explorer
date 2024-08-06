package br.com.planner.planner.controller;

import br.com.planner.planner.domain.trip.Trip;
import br.com.planner.planner.domain.trip.TripRequestDTO;
import br.com.planner.planner.repository.ParticipantRepository;
import br.com.planner.planner.repository.TripRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static br.com.planner.planner.utils.MockCategories.*;
import static br.com.planner.planner.utils.MockCategories.mockConfirmParticipantToTrip;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ParticipantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Autowired
    private JacksonTester<TripRequestDTO> tripRequestDTOJacksonTester;

    @Autowired
    private JacksonTester<Trip> tripJacksonTester;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void confirmParticipant() throws Exception {
        when(tripRepository.save(any(Trip.class))).thenReturn(mockTripEntity());
        String tripDTOJson = objectMapper.writeValueAsString(mockTripDTO());

        var responseCreateTrip = mockMvc.perform(post("/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tripDTOJson)).andReturn().getResponse();

        assertThat(responseCreateTrip.getStatus()).isEqualTo(HttpStatus.OK.value());

        JsonNode jsonNode = objectMapper.readTree(responseCreateTrip.getContentAsString());
        String tripId = jsonNode.get("id").asText();

        String InviteParticipantDTO = objectMapper.writeValueAsString(mockInviteParticipantToTrip());

        var ResponseInviteParticipant = mockMvc.perform(post("/trips/" + tripId + "/invite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(InviteParticipantDTO)).andReturn().getResponse();

        assertThat(ResponseInviteParticipant.getStatus()).isEqualTo(HttpStatus.OK.value());

        JsonNode jsonNode1 = objectMapper.readTree(ResponseInviteParticipant.getContentAsString());
        String participantId = jsonNode1.get("id").asText();

        String ConfirmParticipant = objectMapper.writeValueAsString(mockConfirmParticipantToTrip());

        var responseConfirmParticipant = mockMvc.perform(post("/participants/" + participantId + "/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ConfirmParticipant))
                .andReturn().getResponse();

        assertThat(responseConfirmParticipant.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseConfirmParticipant.getContentAsString()).contains("Leonardo Sardagna");
    }
}