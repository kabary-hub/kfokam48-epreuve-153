package com.presencekf.backend.service;

import com.presencekf.backend.entity.Exercice;
import com.presencekf.backend.entity.Relecture;
import com.presencekf.backend.exception.AutoRelectureException;
import com.presencekf.backend.exception.NoteInvalideException;
import com.presencekf.backend.exception.RelectureDejaRendueException;
import com.presencekf.backend.exception.SessionInconnueException;
import com.presencekf.backend.repository.ExerciceRepository;
import com.presencekf.backend.repository.RelectureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RelectureServiceTest {

    @Mock
    private RelectureRepository relectureRepository;

    @Mock
    private ExerciceRepository exerciceRepository;

    @InjectMocks
    private RelectureService relectureService;

    private Exercice exerciceAvecRelecteur(Long id, Long auteur, Long relecteur) {
        Exercice exercice = new Exercice();
        exercice.setId(id);
        exercice.setEtudiantId(auteur);
        exercice.setRelecteurId(relecteur);
        exercice.setStatut("EN_ATTENTE");
        return exercice;
    }

    @Test
    void exerciceInconnu_doitLancerSessionInconnueException() {
        when(exerciceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> relectureService.rendre(99L, 15, "Bien"))
                .isInstanceOf(SessionInconnueException.class);
    }

    @Test
    void relecteurNonAssigne_doitLancerAutoRelectureException() {
        Exercice exercice = exerciceAvecRelecteur(1L, 1L, null);
        when(exerciceRepository.findById(1L)).thenReturn(Optional.of(exercice));

        assertThatThrownBy(() -> relectureService.rendre(1L, 15, "Bien"))
                .isInstanceOf(AutoRelectureException.class);
    }

    @Test
    void auteurEtRelecteurIdentiques_doitLancerAutoRelectureException() {
        Exercice exercice = exerciceAvecRelecteur(1L, 1L, 1L);
        when(exerciceRepository.findById(1L)).thenReturn(Optional.of(exercice));

        assertThatThrownBy(() -> relectureService.rendre(1L, 15, "Bien"))
                .isInstanceOf(AutoRelectureException.class);
    }

    @Test
    void noteHorsLimites_doitLancerNoteInvalideException() {
        Exercice exercice = exerciceAvecRelecteur(1L, 1L, 2L);
        when(exerciceRepository.findById(1L)).thenReturn(Optional.of(exercice));

        assertThatThrownBy(() -> relectureService.rendre(1L, 25, "Bien"))
                .isInstanceOf(NoteInvalideException.class);
    }

    @Test
    void dejaRendue_doitLancerRelectureDejaRendueException() {
        Exercice exercice = exerciceAvecRelecteur(1L, 1L, 2L);
        when(exerciceRepository.findById(1L)).thenReturn(Optional.of(exercice));
        Relecture existing = new Relecture();
        existing.setStatut("RELUE");
        when(relectureRepository.findByExerciceId(1L)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> relectureService.rendre(1L, 15, "Bien"))
                .isInstanceOf(RelectureDejaRendueException.class);
    }

    @Test
    void casNominal_creeRelectureEtMarqueExerciceRelu() {
        Exercice exercice = exerciceAvecRelecteur(1L, 1L, 2L);
        when(exerciceRepository.findById(1L)).thenReturn(Optional.of(exercice));
        when(relectureRepository.findByExerciceId(1L)).thenReturn(Optional.empty());
        when(relectureRepository.save(any(Relecture.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(exerciceRepository.save(any(Exercice.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Relecture relecture = relectureService.rendre(1L, 15, "Bien");

        assertThat(relecture.getExerciceId()).isEqualTo(1L);
        assertThat(relecture.getRelecteurId()).isEqualTo(2L);
        assertThat(relecture.getNote()).isEqualTo(15);
        assertThat(relecture.getCommentaire()).isEqualTo("Bien");
        assertThat(relecture.getStatut()).isEqualTo("RELUE");
        assertThat(relecture.getRendueAt()).isNotNull();
        assertThat(exercice.getStatut()).isEqualTo("RELUE");
        verify(exerciceRepository).save(exercice);
    }
}
