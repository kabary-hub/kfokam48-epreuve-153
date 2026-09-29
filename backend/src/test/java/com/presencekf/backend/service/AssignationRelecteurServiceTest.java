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
    void choisirRelecteur_sansPresenceRetourneVide() {
        when(presenceRepository.findEtudiantIdsBySessionId(10L))
                .thenReturn(List.of());

        Optional<Long> relecteur = assignationRelecteurService.choisirRelecteur(10L, 1L);

        assertThat(relecteur).isEmpty();
        verify(exerciceRepository, never()).existsByRelecteurIdAndSessionId(2L, 10L);
    }

    @Test
    void choisirRelecteur_tousLesCandidatsSontOccupesRetourneVide() {
        when(presenceRepository.findEtudiantIdsBySessionId(10L))
                .thenReturn(List.of(1L, 2L));
        when(exerciceRepository.existsByRelecteurIdAndSessionId(2L, 10L))
                .thenReturn(true);

        Optional<Long> relecteur = assignationRelecteurService.choisirRelecteur(10L, 1L);

        assertThat(relecteur).isEmpty();
    }

    @Test
    void choisirRelecteur_unSeulCandidatDisponibleLeRetourne() {
        when(presenceRepository.findEtudiantIdsBySessionId(10L))
                .thenReturn(List.of(1L, 2L));
        when(exerciceRepository.existsByRelecteurIdAndSessionId(2L, 10L))
                .thenReturn(false);

        Optional<Long> relecteur = assignationRelecteurService.choisirRelecteur(10L, 1L);

        assertThat(relecteur).contains(2L);
    }

    @Test
    void choisirRelecteur_avecPlusieursCandidatsRetourneUnCandidatDisponible() {
        when(presenceRepository.findEtudiantIdsBySessionId(10L))
                .thenReturn(List.of(1L, 2L, 3L, 4L));
        when(exerciceRepository.existsByRelecteurIdAndSessionId(2L, 10L))
                .thenReturn(false);
        when(exerciceRepository.existsByRelecteurIdAndSessionId(3L, 10L))
                .thenReturn(false);
        when(exerciceRepository.existsByRelecteurIdAndSessionId(4L, 10L))
                .thenReturn(false);

        Optional<Long> relecteur = assignationRelecteurService.choisirRelecteur(10L, 1L);

        assertThat(relecteur).isPresent();
        assertThat(relecteur.orElseThrow()).isIn(2L, 3L, 4L);
    }

    // Tests sur choisirRelecteurs (issue #53 : 2 relecteurs par exercice)

    @Test
    void choisirRelecteurs_deuxCandidatsDisponiblesRetourneDeuxRelecteurs() {
        when(presenceRepository.findEtudiantIdsBySessionId(10L))
                .thenReturn(List.of(1L, 2L, 3L));
        when(exerciceRepository.existsByRelecteurIdAndSessionId(2L, 10L))
                .thenReturn(false);
        when(exerciceRepository.existsByRelecteurIdAndSessionId(3L, 10L))
                .thenReturn(false);
        when(exerciceRepository.existsByRelecteur2IdAndSessionId(2L, 10L))
                .thenReturn(false);
        when(exerciceRepository.existsByRelecteur2IdAndSessionId(3L, 10L))
                .thenReturn(false);

        AssignationRelecteurService.ChoixRelecteurs choix =
                assignationRelecteurService.choisirRelecteurs(10L, 1L);

        assertThat(choix.getRelecteur1Id()).isNotNull();
        assertThat(choix.getRelecteur2Id()).isNotNull();
        Long r1 = choix.getRelecteur1Id();
        Long r2 = choix.getRelecteur2Id();
        assertThat(r1).isNotEqualTo(r2);
        assertThat(r1).isIn(2L, 3L);
        assertThat(r2).isIn(2L, 3L);
    }

    @Test
    void choisirRelecteurs_unSeulCandidatDisponibleRetourneUnSeulRelecteur() {
        when(presenceRepository.findEtudiantIdsBySessionId(10L))
                .thenReturn(List.of(1L, 2L));
        when(exerciceRepository.existsByRelecteurIdAndSessionId(2L, 10L))
                .thenReturn(false);
        when(exerciceRepository.existsByRelecteur2IdAndSessionId(2L, 10L))
                .thenReturn(false);

        AssignationRelecteurService.ChoixRelecteurs choix =
                assignationRelecteurService.choisirRelecteurs(10L, 1L);

        assertThat(choix.getRelecteur1Id()).isEqualTo(2L);
        assertThat(choix.getRelecteur2Id()).isNull();
    }

    @Test
    void choisirRelecteurs_aucunCandidatDisponibleRetourneDesNull() {
        when(presenceRepository.findEtudiantIdsBySessionId(10L))
                .thenReturn(List.of());

        AssignationRelecteurService.ChoixRelecteurs choix =
                assignationRelecteurService.choisirRelecteurs(10L, 1L);

        assertThat(choix.getRelecteur1Id()).isNull();
        assertThat(choix.getRelecteur2Id()).isNull();
    }

    @Test
    void choisirRelecteurs_deuxCandidatsMaisUnEstDeja2eRelecteurRetourneUnSeul() {
        when(presenceRepository.findEtudiantIdsBySessionId(10L))
                .thenReturn(List.of(1L, 2L, 3L));
        when(exerciceRepository.existsByRelecteurIdAndSessionId(2L, 10L))
                .thenReturn(false);
        when(exerciceRepository.existsByRelecteurIdAndSessionId(3L, 10L))
                .thenReturn(false);
        when(exerciceRepository.existsByRelecteur2IdAndSessionId(2L, 10L))
                .thenReturn(false);
        when(exerciceRepository.existsByRelecteur2IdAndSessionId(3L, 10L))
                .thenReturn(true); // 3 est déjà 2e relecteur

        AssignationRelecteurService.ChoixRelecteurs choix =
                assignationRelecteurService.choisirRelecteurs(10L, 1L);

        assertThat(choix.getRelecteur1Id()).isEqualTo(2L);
        assertThat(choix.getRelecteur2Id()).isNull();
    }
}
