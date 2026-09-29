package com.presencekf.backend.service;

import com.presencekf.backend.entity.Presence;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.exception.DejaPresentException;
import com.presencekf.backend.exception.SessionInconnueException;
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
class PresenceServiceFormateurTest {

    @Mock
    private PresenceRepository presenceRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private PresenceService presenceService;

    @Test
    void sessionInconnue_doitLancerSessionInconnueException() {
        when(sessionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                presenceService.enregistrerPresenceFormateur(99L, 1L))
                .isInstanceOf(SessionInconnueException.class);
    }

    @Test
    void dejaPresent_doitLancerDejaPresentException() {
        Session session = new Session();
        session.setId(1L);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(presenceRepository.findBySessionIdAndEtudiantId(1L, 1L))
                .thenReturn(Optional.of(new Presence()));

        assertThatThrownBy(() ->
                presenceService.enregistrerPresenceFormateur(1L, 1L))
                .isInstanceOf(DejaPresentException.class);
    }

    @Test
    void casNominal_doitCreerPresenceAvecSourceFormateur() {
        Session session = new Session();
        session.setId(1L);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(presenceRepository.findBySessionIdAndEtudiantId(1L, 2L))
                .thenReturn(Optional.empty());
        when(presenceRepository.saveAndFlush(any(Presence.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Presence presence = presenceService.enregistrerPresenceFormateur(1L, 2L);

        assertThat(presence.getSessionId()).isEqualTo(1L);
        assertThat(presence.getEtudiantId()).isEqualTo(2L);
        assertThat(presence.getSource()).isEqualTo("FORMATEUR");
        assertThat(presence.getMarqueAt()).isNotNull();
    }
}
