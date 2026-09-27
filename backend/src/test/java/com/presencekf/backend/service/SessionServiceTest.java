package com.presencekf.backend.service;

import com.presencekf.backend.entity.Session;
import com.presencekf.backend.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Test unitaire pour SessionService.
 *
 * Prouve la règle RG1 : un code de présence expire 15 minutes après
 * l'ouverture (Q2). Vérifie aussi le format du code (6 caractères
 * [A-Z0-9], conforme au pattern du contrat).
 *
 * Référence : RG1 (CDC section 6), EF2 (CDC section 4), M1 (#15).
 */
@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionService;

    @Test
    void ouvrirSession_doitCalculerExpiration15MinutesApresOuverture() {
        // Given
        when(sessionRepository.save(any(Session.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Session session = sessionService.ouvrirSession("Cours de test", 1L);

        // Then — RG1 : expirationAt = ouvertureAt + 15 min
        assertThat(session.getOuvertureAt()).isNotNull();
        assertThat(session.getExpirationAt()).isNotNull();

        Duration ecart = Duration.between(
                session.getOuvertureAt(),
                session.getExpirationAt()
        );
        assertThat(ecart.toMinutes())
                .as("RG1 : le code doit expirer 15 minutes après ouverture")
                .isEqualTo(15);
    }

    @Test
    void ouvrirSession_doitGenererCode6CaracteresAlphanumeriques() {
        // Given
        when(sessionRepository.save(any(Session.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Session session = sessionService.ouvrirSession("Cours de test", 1L);

        // Then — code 6 caractères [A-Z0-9]
        assertThat(session.getCode())
                .as("Le code doit faire 6 caractères")
                .hasSize(6);
        assertThat(session.getCode())
                .as("Le code doit respecter le pattern ^[A-Z0-9]{6}$")
                .matches("^[A-Z0-9]{6}$");
    }

    @Test
    void ouvrirSession_doitConserverTitreEtPromotionId() {
        // Given
        when(sessionRepository.save(any(Session.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Session session = sessionService.ouvrirSession("Mathématiques", 42L);

        // Then
        assertThat(session.getTitre()).isEqualTo("Mathématiques");
        assertThat(session.getPromotionId()).isEqualTo(42L);
    }
}
