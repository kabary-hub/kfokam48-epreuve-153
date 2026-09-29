package com.presencekf.backend.service;

import com.presencekf.backend.entity.Exercice;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.exception.ExerciceDejaDeposeException;
import com.presencekf.backend.exception.LienInvalideException;
import com.presencekf.backend.exception.SessionClotureeException;
import com.presencekf.backend.exception.SessionInconnueException;
import com.presencekf.backend.repository.ExerciceRepository;
import com.presencekf.backend.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExerciceServiceTest {

    @Mock
    private ExerciceRepository exerciceRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private AssignationRelecteurService assignationRelecteurService;

    @InjectMocks
    private ExerciceService exerciceService;

    private Session sessionOuverte(Long id) {
        Session session = new Session();
        session.setId(id);
        session.setCode("ABC123");
        session.setOuvertureAt(LocalDateTime.now());
        session.setExpirationAt(LocalDateTime.now().plusMinutes(15));
        return session;
    }

    @Test
    void sessionInconnue_doitLancerSessionInconnueException() {
        when(sessionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                exerciceService.deposer(99L, 1L, "https://exemple.com/exo"))
                .isInstanceOf(SessionInconnueException.class);
    }

    @Test
    void sessionCloturee_doitLancerSessionClotureeException() {
        Session session = sessionOuverte(1L);
        session.setClotureAt(LocalDateTime.now());
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertThatThrownBy(() ->
                exerciceService.deposer(1L, 1L, "https://exemple.com/exo"))
                .isInstanceOf(SessionClotureeException.class);
    }

    @Test
    void lienInvalide_sansScheme_doitLancerLienInvalideException() {
        when(sessionRepository.findById(1L))
                .thenReturn(Optional.of(sessionOuverte(1L)));

        assertThatThrownBy(() -> exerciceService.deposer(1L, 1L, "exemple.com/exo"))
                .isInstanceOf(LienInvalideException.class);
    }

    @Test
    void lienInvalide_nonHttp_doitLancerLienInvalideException() {
        when(sessionRepository.findById(1L))
                .thenReturn(Optional.of(sessionOuverte(1L)));

        assertThatThrownBy(() ->
                exerciceService.deposer(1L, 1L, "ftp://exemple.com/exo"))
                .isInstanceOf(LienInvalideException.class);
    }

    @Test
    void dejaDepose_doitLancerExerciceDejaDeposeException() {
        when(sessionRepository.findById(1L))
                .thenReturn(Optional.of(sessionOuverte(1L)));
        when(exerciceRepository.findBySessionIdAndEtudiantId(1L, 1L))
                .thenReturn(Optional.of(new Exercice()));

        assertThatThrownBy(() ->
                exerciceService.deposer(1L, 1L, "https://exemple.com/exo"))
                .isInstanceOf(ExerciceDejaDeposeException.class);
    }

    @Test
    void casNominal_doitCreerExerciceAvecDeuxRelecteurs() {
        when(sessionRepository.findById(1L))
                .thenReturn(Optional.of(sessionOuverte(1L)));
        when(exerciceRepository.findBySessionIdAndEtudiantId(1L, 1L))
                .thenReturn(Optional.empty());
        when(assignationRelecteurService.choisirRelecteurs(1L, 1L))
                .thenReturn(new AssignationRelecteurService.ChoixRelecteurs(2L, 3L));
        when(exerciceRepository.save(any(Exercice.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Exercice exercice = exerciceService.deposer(
                1L, 1L, "https://exemple.com/exo");

        assertThat(exercice.getSessionId()).isEqualTo(1L);
        assertThat(exercice.getEtudiantId()).isEqualTo(1L);
        assertThat(exercice.getLien()).isEqualTo("https://exemple.com/exo");
        assertThat(exercice.getStatut()).isEqualTo("EN_ATTENTE");
        assertThat(exercice.getDeposeAt()).isNotNull();
        assertThat(exercice.getRelecteurId()).isEqualTo(2L);
        assertThat(exercice.getRelecteur2Id()).isEqualTo(3L);
    }

    @Test
    void unSeulCandidat_doitCreerExerciceAvecUnSeulRelecteur() {
        when(sessionRepository.findById(1L))
                .thenReturn(Optional.of(sessionOuverte(1L)));
        when(exerciceRepository.findBySessionIdAndEtudiantId(1L, 1L))
                .thenReturn(Optional.empty());
        when(assignationRelecteurService.choisirRelecteurs(1L, 1L))
                .thenReturn(new AssignationRelecteurService.ChoixRelecteurs(2L, null));
        when(exerciceRepository.save(any(Exercice.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Exercice exercice = exerciceService.deposer(
                1L, 1L, "https://exemple.com/exo");

        assertThat(exercice.getStatut()).isEqualTo("EN_ATTENTE");
        assertThat(exercice.getRelecteurId()).isEqualTo(2L);
        assertThat(exercice.getRelecteur2Id()).isNull();
    }

    @Test
    void aucunCandidat_doitCreerExerciceSansRelecteur() {
        when(sessionRepository.findById(1L))
                .thenReturn(Optional.of(sessionOuverte(1L)));
        when(exerciceRepository.findBySessionIdAndEtudiantId(1L, 1L))
                .thenReturn(Optional.empty());
        when(assignationRelecteurService.choisirRelecteurs(1L, 1L))
                .thenReturn(new AssignationRelecteurService.ChoixRelecteurs(null, null));
        when(exerciceRepository.save(any(Exercice.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Exercice exercice = exerciceService.deposer(
                1L, 1L, "https://exemple.com/exo");

        assertThat(exercice.getStatut()).isEqualTo("EN_ATTENTE_SANS_RELECTEUR");
        assertThat(exercice.getRelecteurId()).isNull();
        assertThat(exercice.getRelecteur2Id()).isNull();
    }
}
