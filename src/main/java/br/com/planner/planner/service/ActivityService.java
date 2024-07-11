package br.com.planner.planner.service;

import br.com.planner.planner.domain.activity.Activity;
import br.com.planner.planner.domain.activity.ActivityDetails;
import br.com.planner.planner.domain.activity.ActivityRequestDTO;
import br.com.planner.planner.domain.activity.ActivityResponseDTO;
import br.com.planner.planner.domain.trip.Trip;
import br.com.planner.planner.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public ActivityResponseDTO registerActivity(Trip trip, ActivityRequestDTO data) {

            Activity newActivity = new Activity(data.title(), data.occurs_at(), trip);
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
}
