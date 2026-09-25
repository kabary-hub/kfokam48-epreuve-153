package com.presencekf.backend.controller;

import com.presencekf.backend.dto.NoteDto;
import com.presencekf.backend.service.NoteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions/{sessionId}/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/{etudiant}")
    public NoteDto getNote(@PathVariable Long sessionId,
                          @PathVariable String etudiant) {
        return noteService.getNote(sessionId, etudiant);
    }
}
