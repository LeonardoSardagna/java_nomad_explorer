package br.com.planner.planner.service;

import br.com.planner.planner.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateFormaterService {

    @Autowired
    private TripRepository tripRepository;

    public String dateFormater(LocalDate date) {
        LocalDate DateTime = date;
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return DateTime.format(formatterStart);
    }
}
