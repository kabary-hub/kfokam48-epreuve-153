package com.presencekf.backend.service;

import com.presencekf.backend.entity.Exercice;
import com.presencekf.backend.entity.Relecture;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.exception.AutoRelectureException;
import com.presencekf.backend.exception.NoteInvalideException;
import com.presencekf.backend.exception.RelectureDejaRendueException;
import com.presencekf.backend.exception.SessionClotureeException;
import com.presencekf.backend.exception.SessionInconnueException;
import com.presencekf.backend.repository.ExerciceRepository;
import com.presencekf.backend.exception.RelecteurNonAssigneException;
import com.presencekf.backend.repository.RelectureRepository;
import com.presencekf.backend.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RelectureServiceTest {

    @Mock
    private RelectureRepository relectureRepository;

    @Mock
    private ExerciceRepository exerciceRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private RelectureService relectureService;

    private Exercice exerciceAvecRelecteur(Long id, Long auteur, Long relecteur1, Long relecteur2) {
        Exercice exercice = new Exercice();
        exercice.setId(id);
        exercice.setEtudiantId(auteur);
        exercice.setSessionId(1L);
        exercice.setRelecteurId(relecteur1);
        exercice.setRelecteur2Id(relecteur2);
        exercice.setStatut("EN_ATTENTE");
        return exercice;
    }

    @Test
    void exerciceInconnu_doitLancerSessionInconnueException() {
        when(exerciceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> relectureService.rendre(99L, null, 15, "Bien"))
                .isInstanceOf(SessionInconnueException.class);
    }

    @Test
    void relecteurNonAssigne_doitLancerRelecteurNonAssigneException() {
        Exercice exercice = exerciceAvecRelecteur(1L, 1L, 2L, 3L);
        when(exerciceRepository.findById(1L)).thenReturn(Optional.of(exercice));
                when(sessionRepository.findById(1L)).thenReturn(Optional.of(new Session()));

        Long relecteurIdNonAssigne = 4L;

        assertThatThrownBy(() -> relectureService.rendre(1L, relecteurIdNonAssigne, 15, "Bien"))
                .isInstanceOf(RelecteurNonAssigneException.class);
    }

    @Test
    void auteurEtRelecteurIdentiques_doitLancerAutoRelectureException() {
        Exercice exercice = exerciceAvecRelecteur(1L, 1L, 1L, 2L);
        when(exerciceRepository.findById(1L)).thenReturn(Optional.of(exercice));
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(new Session()));

        assertThatThrownBy(() -> relectureService.rendre(1L, 1L, 15, "Bien"))
                .isInstanceOf(AutoRelectureException.class);
    }

    @Test
    void noteHorsLimites_doitLancerNoteInvalideException() {
        Exercice exercice = exerciceAvecRelecteur(1L, 1L, 2L, 3L);
        when(exerciceRepository.findById(1L)).thenReturn(Optional.of(exercice));
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(new Session()));
        Long relecteurIdNoteInvalide = 2L;
        when(relectureRepository.findByExerciceIdAndRelecteurId(eq(1L), eq(relecteurIdNoteInvalide)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> relectureService.rendre(1L, relecteurIdNoteInvalide, 25, "Bien"))
                .isInstanceOf(NoteInvalideException.class);
    }

    @Test
    void dejaRendue_doitLancerRelectureDejaRendueException() {
        Exercice exercice = exerciceAvecRelecteur(1L, 1L, 2L, 3L);
        when(exerciceRepository.findById(1L)).thenReturn(Optional.of(exercice));
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(new Session()));
        Relecture existing = new Relecture();
        existing.setExerciceId(1L);
        existing.setRelecteurId(2L);
        existing.setStatut("RELUE");
        when(relectureRepository.findByExerciceIdAndRelecteurId(1L, 2L))
                .thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> relectureService.rendre(1L, 2L, 15, "Bien"))
                .isInstanceOf(RelectureDejaRendueException.class);
    }

    @Test
    void sessionCloturee_doitLancerSessionClotureeException() {
        Exercice exercice = exerciceAvecRelecteur(1L, 1L, 2L, 3L);
        when(exerciceRepository.findById(1L)).thenReturn(Optional.of(exercice));
        Session session = new Session();
        session.setClotureAt(java.time.LocalDateTime.now());
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertThatThrownBy(() -> relectureService.rendre(1L, 2L, 15, "Bien"))
                .isInstanceOf(SessionClotureeException.class);
    }

    @Test
    void casNominal_creeRelectureEtMarqueExerciceProvisoire() {
        Exercice exercice = exerciceAvecRelecteur(1L, 1L, 2L, 3L);
        when(exerciceRepository.findById(1L)).thenReturn(Optional.of(exercice));
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(new Session()));
        when(relectureRepository.findByExerciceIdAndRelecteurId(1L, 2L))
                .thenReturn(Optional.empty());
        when(relectureRepository.countByExerciceIdAndStatut(1L, "RELUE"))
                .thenReturn(1L);
        when(relectureRepository.save(any(Relecture.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(exerciceRepository.save(any(Exercice.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Relecture relecture = relectureService.rendre(1L, 2L, 15, "Bien");

        assertThat(relecture.getExerciceId()).isEqualTo(1L);
        assertThat(relecture.getRelecteurId()).isEqualTo(2L);
        assertThat(relecture.getNote()).isEqualTo(15);
        assertThat(relecture.getCommentaire()).isEqualTo("Bien");
        assertThat(relecture.getStatut()).isEqualTo("RELUE");
        assertThat(relecture.getRendueAt()).isNotNull();
        assertThat(exercice.getStatut()).isEqualTo("PROVISOIRE");
        verify(exerciceRepository).save(exercice);
    }

    @Test
    void secondRelecteur_creeRelectureEtMarqueExerciceRelu() {
        Exercice exercice = exerciceAvecRelecteur(1L, 1L, 2L, 3L);
        when(exerciceRepository.findById(1L)).thenReturn(Optional.of(exercice));
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(new Session()));
        when(relectureRepository.findByExerciceIdAndRelecteurId(1L, 3L))
                .thenReturn(Optional.empty());
        when(relectureRepository.countByExerciceIdAndStatut(1L, "RELUE"))
                .thenReturn(2L);
        when(relectureRepository.save(any(Relecture.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(exerciceRepository.save(any(Exercice.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Relecture relecture = relectureService.rendre(1L, 3L, 20, "Excellent");

        assertThat(relecture.getExerciceId()).isEqualTo(1L);
        assertThat(relecture.getRelecteurId()).isEqualTo(3L);
        assertThat(relecture.getNote()).isEqualTo(20);
        assertThat(relecture.getStatut()).isEqualTo("RELUE");
        assertThat(exercice.getStatut()).isEqualTo("RELUE");
        verify(exerciceRepository).save(exercice);
    }
}
