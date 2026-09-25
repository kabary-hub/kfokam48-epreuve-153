package com.presencekf.backend.service;

import com.presencekf.backend.dto.RègleDto;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RèglesService {

    public List<RègleDto> getAllRègles() {
        return Arrays.asList(
            new RègleDto(1L, "Présence obligatoire pendant la session"),
            new RègleDto(2L, "Code de présence valide 15 minutes"),
            new RègleDto(3L, "Un relecteur par exercice"),
            new RègleDto(4L, "Modifications de note avant clôture uniquement"),
            new RègleDto(5L, "Clôture automatique à la fin de la session")
        ).stream()
        .collect(Collectors.toList());
    }

    public RègleDto getRègle(Long id) {
        return getAllRègles().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Règle non trouvée"));
    }
}
