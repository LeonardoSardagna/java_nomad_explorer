package br.com.planner.planner.service;

import br.com.planner.planner.domain.activity.*;
import br.com.planner.planner.domain.trip.Trip;
import br.com.planner.planner.infra.exception.ValidationException;
import br.com.planner.planner.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TripService tripService;

    public ActivityResponseDTO registerActivity(Trip trip, ActivityRequestDTO data) {
        Activity newActivity = new Activity(data.title(), data.occurs_at(), trip);

        if (newActivity.getOccursAt().isBefore(trip.getStartsAt())) {
            throw new ValidationException("A atividade não pode ser agendada no passado da viagem.");
        }

        if (newActivity.getOccursAt().isAfter(trip.getEndsAt().plusDays(1).plusSeconds(1))) {
            throw new ValidationException("A atividade não pode ser agendada no futuro da viagem.");
        }

        List<Activity> existingActivities = this.activityRepository.findByTripAndOccursAtBetween(
                trip,
                newActivity.getOccursAt().minusHours(2),
                newActivity.getOccursAt().plusHours(2)
        );

        for (Activity activity : existingActivities) {
            if (activity.getOccursAt().equals(newActivity.getOccursAt())) {
                throw new ValidationException("Já existe uma atividade agendada para este horário.");
            }
        }

        this.activityRepository.save(newActivity);
        return new ActivityResponseDTO(newActivity.getId());
    }

    public List<ActivityDetails> getAllActivitiesFromId(UUID id) {
        return this.activityRepository.findByTripId(id)
                .stream().map(activity -> new ActivityDetails(
                        activity.getId(),
                        activity.getTitle(),
                        activity.getOccursAt())).toList();
    }

    public List<DailyActivitiesDTO> getAllActivitiesGroupedByDate(UUID tripId) {
        Trip trip = this.tripService.getTrip(tripId);
        List<ActivityDetails> activities = getAllActivitiesFromId(tripId);

        LocalDate startDate = trip.getStartsAt().toLocalDate();
        LocalDate endDate = trip.getEndsAt().toLocalDate();
        int daysBetween = (int) ChronoUnit.DAYS.between(startDate, endDate);

        List<DailyActivitiesDTO> dailyActivitiesList = new ArrayList<>();

        for (int i = 0; i <= daysBetween; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            List<ActivityDetails> activitiesForDay = activities.stream()
                    .filter(activity -> activity.occurs_at().toLocalDate().isEqual(currentDate))
                    .map(activity -> new ActivityDetails(activity.id(), activity.title(), activity.occurs_at()))
                    .collect(Collectors.toList());

            dailyActivitiesList.add(new DailyActivitiesDTO(currentDate.atStartOfDay(), activitiesForDay));
        }
        return dailyActivitiesList;
    }
}
