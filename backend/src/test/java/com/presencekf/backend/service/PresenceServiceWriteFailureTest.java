package com.presencekf.backend.service;

import com.presencekf.backend.entity.Presence;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.exception.DejaPresentException;
import com.presencekf.backend.repository.PresenceRepository;
import com.presencekf.backend.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PresenceServiceWriteFailureTest {

    @Mock
    private PresenceRepository presenceRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private PresenceService presenceService;

    @Test
    void saveAvecViolationContrainte_doitRetournerDejaPresent() {
        Session session = new Session();
        session.setId(1L);
        session.setExpirationAt(LocalDateTime.now().plusMinutes(15));
        when(sessionRepository.findByCode("ABC123")).thenReturn(Optional.of(session));
        when(presenceRepository.findBySessionIdAndEtudiantId(1L, 1L))
                .thenReturn(Optional.empty());
        when(presenceRepository.saveAndFlush(any(Presence.class)))
                .thenThrow(new DataIntegrityViolationException("unique constraint"));

        assertThatThrownBy(() -> presenceService.enregistrerPresence("ABC123", 1L))
                .isInstanceOf(DejaPresentException.class)
                .hasMessageContaining("présence existe déjà");
    }
}
