package ru.inside.task.service;

import ru.inside.task.dto.NoteDtoRequest;
import ru.inside.task.dto.NoteDtoResponse;
import ru.inside.task.model.Note;

import java.util.List;

public interface NoteService {

    /**
     * Check request history
     *
     * @param noteDtoRequest - object request note
     * @return flag is history
     */
    boolean checkRequestHistory(NoteDtoRequest noteDtoRequest);

    /**
     * Create new note
     *
     * @param noteDtoRequest - object note
     * @return object NoteDtoResponse
     */
    NoteDtoResponse createNote(NoteDtoRequest noteDtoRequest);

    /**
     * Get note by id
     *
     * @param id note
     * @return object NoteDtoResponse
     */
    NoteDtoResponse getNoteById(Long id);

    /**
     * Get last notes
     *
     * @param noteDtoRequest object NoteDtoResponse
     * @return list objects NoteDtoResponse
     */
    List<NoteDtoResponse> getNotesHistory(NoteDtoRequest noteDtoRequest);


    /**
     * Map from Note to NoteDtoResponse
     *
     * @param note оbject Note
     * @return оbject DtoResponse
     */
    NoteDtoResponse mapToNoteDto(Note note);

    /**
     * Map from NoteDtoRequest to Note
     *
     * @param noteDtoRequest оbject noteDtoRequest
     * @return object Note
     */
    Note mapToNote(NoteDtoRequest noteDtoRequest);
}
