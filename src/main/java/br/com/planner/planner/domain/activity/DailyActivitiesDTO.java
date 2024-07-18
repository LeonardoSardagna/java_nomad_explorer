package br.com.planner.planner.domain.activity;

import java.time.LocalDateTime;
import java.util.List;

public record DailyActivitiesDTO(LocalDateTime date, List<ActivityDetails> activities) {
}
