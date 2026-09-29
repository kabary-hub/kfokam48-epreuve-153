package com.presencekf.backend.service;

import com.presencekf.backend.entity.Presence;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.exception.CodeExpireException;
import com.presencekf.backend.exception.CodeInconnuException;
import com.presencekf.backend.exception.DejaPresentException;
import com.presencekf.backend.exception.TropTentativesException;
import com.presencekf.backend.repository.PresenceRepository;
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
class PresenceServiceTest {

    @Mock
    private PresenceRepository presenceRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private PresenceService presenceService;

    private Session sessionValide(Long id) {
        Session session = new Session();
        session.setId(id);
        session.setCode("ABC123");
        session.setOuvertureAt(LocalDateTime.now().minusMinutes(5));
        session.setExpirationAt(LocalDateTime.now().plusMinutes(10));
        return session;
    }

    @Test
    void codeInconnu_doitLancerCodeInconnuException() {
        when(sessionRepository.findByCode("XXXXXX")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> presenceService.enregistrerPresence("XXXXXX", 1L))
                .isInstanceOf(CodeInconnuException.class);
    }

    @Test
    void codeExpire_doitLancerCodeExpireException() {
        Session session = sessionValide(1L);
        session.setExpirationAt(LocalDateTime.now().minusMinutes(1));
        when(sessionRepository.findByCode("ABC123")).thenReturn(Optional.of(session));

        assertThatThrownBy(() -> presenceService.enregistrerPresence("ABC123", 1L))
                .isInstanceOf(CodeExpireException.class);
    }

    @Test
    void dejaPresent_doitLancerDejaPresentException() {
        when(sessionRepository.findByCode("ABC123"))
                .thenReturn(Optional.of(sessionValide(1L)));
        when(presenceRepository.findBySessionIdAndEtudiantId(1L, 1L))
                .thenReturn(Optional.of(new Presence()));

        assertThatThrownBy(() -> presenceService.enregistrerPresence("ABC123", 1L))
                .isInstanceOf(DejaPresentException.class);
    }

    @Test
    void casNominal_doitCreerPresenceAvecSourceEtudiant() {
        when(sessionRepository.findByCode("ABC123"))
                .thenReturn(Optional.of(sessionValide(1L)));
        when(presenceRepository.findBySessionIdAndEtudiantId(1L, 1L))
                .thenReturn(Optional.empty());
        when(presenceRepository.saveAndFlush(any(Presence.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Presence presence = presenceService.enregistrerPresence("ABC123", 1L);

        assertThat(presence.getSessionId()).isEqualTo(1L);
        assertThat(presence.getEtudiantId()).isEqualTo(1L);
        assertThat(presence.getSource()).isEqualTo("ETUDIANT");
        assertThat(presence.getMarqueAt()).isNotNull();
    }

    @Test
    void cinqCodesInconnus_bloquentLEtudiantPourLesTentativesSuivantes() {
        when(sessionRepository.findByCode("XXXXXX")).thenReturn(Optional.empty());

        for (int attempt = 0; attempt < 5; attempt++) {
            assertThatThrownBy(() -> presenceService.enregistrerPresence("XXXXXX", 42L))
                    .isInstanceOf(CodeInconnuException.class);
        }

        assertThatThrownBy(() -> presenceService.enregistrerPresence("XXXXXX", 42L))
                .isInstanceOf(TropTentativesException.class)
                .hasMessageContaining("Réessayez dans 2 minutes");
    }
}
