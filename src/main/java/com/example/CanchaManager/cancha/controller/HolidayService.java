package com.example.CanchaManager.cancha.controller;

import com.example.CanchaManager.cancha.service.HolidayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Service
public class HolidayService {

    private final RestTemplate restTemplate;

    @Autowired
    public HolidayService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final String API_URL = "https://date.nager.at/Api/v2/PublicHolidays";

    public boolean isHoliday(LocalDate date) {
        // Define el formato de la fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(formatter);

        // Construye la URL para la solicitud
        String url = String.format("%s/%d/AR", API_URL, date.getYear());

        // Realiza la solicitud a la API
        HolidayResponse[] response = restTemplate.getForObject(url, HolidayResponse[].class);

        if (response != null) {
            Set<String> holidays = new HashSet<>();
            for (HolidayResponse holiday : response) {
                holidays.add(holiday.getDate());
            }
            return holidays.contains(formattedDate);
        }
        return false;
    }
}
