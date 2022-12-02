package ru.inside.task.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.inside.task.dto.NoteDtoRequest;
import ru.inside.task.service.NoteService;

/**
 * Note's controller
 */
@RestController
@RequestMapping("/api/v1/note")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNote (@RequestBody NoteDtoRequest noteDtoRequest) {
        return ResponseEntity.ok(noteService.checkRequestHistory(noteDtoRequest) ?
                noteService.getNotesHistory(noteDtoRequest) : noteService.createNote(noteDtoRequest));
    }
}
