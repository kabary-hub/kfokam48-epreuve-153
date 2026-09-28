package com.presencekf.backend.service;

import com.presencekf.backend.repository.ExerciceRepository;
import com.presencekf.backend.repository.PresenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssignationRelecteurServiceTest {

    @Mock
    private PresenceRepository presenceRepository;

    @Mock
    private ExerciceRepository exerciceRepository;

    @InjectMocks
    private AssignationRelecteurService assignationRelecteurService;

    @Test
    void choisirRelecteur_exclutAuteurEtRelecteurDejaOccupe() {
        when(presenceRepository.findEtudiantIdsBySessionId(10L))
                .thenReturn(List.of(1L, 2L, 3L));
        when(exerciceRepository.existsByRelecteurIdAndSessionId(2L, 10L))
                .thenReturn(true);
        when(exerciceRepository.existsByRelecteurIdAndSessionId(3L, 10L))
                .thenReturn(false);

        Optional<Long> relecteur = assignationRelecteurService.choisirRelecteur(10L, 1L);

        assertThat(relecteur).contains(3L);
        verify(exerciceRepository, never()).existsByRelecteurIdAndSessionId(1L, 10L);
    }

    @Test
    void choisirRelecteur_sansCandidatRetourneVide() {
        when(presenceRepository.findEtudiantIdsBySessionId(10L))
                .thenReturn(List.of(1L));

        Optional<Long> relecteur = assignationRelecteurService.choisirRelecteur(10L, 1L);

        assertThat(relecteur).isEmpty();
        verify(exerciceRepository, never()).existsByRelecteurIdAndSessionId(1L, 10L);
    }
}
