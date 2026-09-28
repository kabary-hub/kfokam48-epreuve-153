package com.presencekf.backend.service;

import com.presencekf.backend.dto.TableauDto;
import com.presencekf.backend.entity.Etudiant;
import com.presencekf.backend.entity.Exercice;
import com.presencekf.backend.entity.Promotion;
import com.presencekf.backend.entity.Relecture;
import com.presencekf.backend.exception.PromotionInconnueException;
import com.presencekf.backend.repository.EtudiantRepository;
import com.presencekf.backend.repository.ExerciceRepository;
import com.presencekf.backend.repository.PresenceRepository;
import com.presencekf.backend.repository.PromotionRepository;
import com.presencekf.backend.repository.RelectureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableauServiceTest {

    @Mock private PromotionRepository promotionRepository;
    @Mock private EtudiantRepository etudiantRepository;
    @Mock private PresenceRepository presenceRepository;
    @Mock private ExerciceRepository exerciceRepository;
    @Mock private RelectureRepository relectureRepository;

    @InjectMocks
    private TableauService tableauService;

    @Test
    void promotionInconnue_doitLancerPromotionInconnueException() {
        when(promotionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableauService.construire(99L))
                .isInstanceOf(PromotionInconnueException.class);
    }

    @Test
    void promotionSansEtudiants_doitRetournerListeVide() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(new Promotion()));
        when(etudiantRepository.findByPromotionId(1L)).thenReturn(List.of());

        assertThat(tableauService.construire(1L)).isEmpty();
    }

    @Test
    void etudiantSansExerciceRelu_doitAvoirMoyenneNulle() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(new Promotion()));
        Etudiant etudiant = new Etudiant();
        etudiant.setId(1L);
        etudiant.setNom("Alice");
        when(etudiantRepository.findByPromotionId(1L)).thenReturn(List.of(etudiant));
        when(presenceRepository.countByEtudiantId(1L)).thenReturn(3L);
        when(exerciceRepository.findByEtudiantId(1L)).thenReturn(List.of());
        when(exerciceRepository.countByRelecteurIdAndStatutNot(1L, "RELUE")).thenReturn(0L);

        List<TableauDto> resultat = tableauService.construire(1L);

        assertThat(resultat).hasSize(1);
        assertThat(resultat.get(0).getNom()).isEqualTo("Alice");
        assertThat(resultat.get(0).getPresences()).isEqualTo(3L);
        assertThat(resultat.get(0).getExercicesDeposes()).isZero();
        assertThat(resultat.get(0).getMoyenne()).isNull();
    }

    @Test
    void etudiantAvecExercicesRelus_doitAgregerMoyenneEtRelecturesEnAttente() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(new Promotion()));
        Etudiant etudiant = new Etudiant();
        etudiant.setId(1L);
        etudiant.setNom("Bob");
        when(etudiantRepository.findByPromotionId(1L)).thenReturn(List.of(etudiant));

        Exercice exercice1 = new Exercice();
        exercice1.setId(10L);
        Exercice exercice2 = new Exercice();
        exercice2.setId(11L);
        when(exerciceRepository.findByEtudiantId(1L)).thenReturn(List.of(exercice1, exercice2));
        when(presenceRepository.countByEtudiantId(1L)).thenReturn(5L);
        when(exerciceRepository.countByRelecteurIdAndStatutNot(1L, "RELUE")).thenReturn(1L);

        Relecture relecture1 = new Relecture();
        relecture1.setExerciceId(10L);
        relecture1.setNote(10);
        relecture1.setStatut("RELUE");
        Relecture relecture2 = new Relecture();
        relecture2.setExerciceId(11L);
        relecture2.setNote(20);
        relecture2.setStatut("RELUE");
        when(relectureRepository.findByExerciceIdIn(List.of(10L, 11L)))
                .thenReturn(List.of(relecture1, relecture2));

        List<TableauDto> resultat = tableauService.construire(1L);

        assertThat(resultat).hasSize(1);
        assertThat(resultat.get(0).getPresences()).isEqualTo(5L);
        assertThat(resultat.get(0).getExercicesDeposes()).isEqualTo(2L);
        assertThat(resultat.get(0).getMoyenne()).isEqualTo(15.0);
        assertThat(resultat.get(0).getRelecturesEnAttente()).isEqualTo(1L);
    }
}
