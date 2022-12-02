package ru.inside.task.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.inside.task.dto.NoteDtoRequest;
import ru.inside.task.dto.NoteDtoResponse;
import ru.inside.task.exception.BadRequestException;
import ru.inside.task.exception.NotFoundException;
import ru.inside.task.model.Note;
import ru.inside.task.model.User;
import ru.inside.task.repository.NoteRepository;
import ru.inside.task.repository.UserRepository;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private static final int MAX_LENGTH_MESSAGE = 255;

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteServiceImpl(NoteRepository noteRepository
            , UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean checkRequestHistory(NoteDtoRequest noteDtoRequest) {
        String[] words = noteDtoRequest.getMessage().split(" ");
        return (words[0].equals("history") && words[1].chars().allMatch( Character::isDigit) && words.length == 2);
    }

    @Override
    public NoteDtoResponse createNote(NoteDtoRequest noteDtoRequest) {
        if (noteDtoRequest == null) {
            throw new BadRequestException("Invalid note");
        }
        if (!StringUtils.isNotBlank(noteDtoRequest.getName())) {
            throw new BadRequestException("Invalid user's name");
        }
        User userFromDB = userRepository.findByUsername(noteDtoRequest.getName());
        System.out.println(noteDtoRequest.getName());
        System.out.println(noteDtoRequest);
        if (userFromDB == null) {
            throw new BadRequestException("User's name not found");
        }
        if (!StringUtils.isNotBlank(noteDtoRequest.getMessage())) {
            throw new BadRequestException("Invalid note's message");
        }
        if (noteDtoRequest.getMessage().length() > MAX_LENGTH_MESSAGE) {
            throw new BadRequestException("Too long note's message");
        }
        return mapToNoteDto(noteRepository.save(mapToNote(noteDtoRequest)));
    }

    @Override
    public NoteDtoResponse getNoteById(Long id) {
        if (id == null) {
            throw new BadRequestException("Invalid ID");
        }
        return mapToNoteDto(noteRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Note not found!")));
    }

    @Override
    public List<NoteDtoResponse> getNotesHistory(NoteDtoRequest noteDtoRequest) {
        String[] words = noteDtoRequest.getMessage().split(" ");
        String name = noteDtoRequest.getName();
        int count = Integer.parseInt(words[1]);
        return noteRepository.findAll().stream()
                .filter(note -> note.getUser().getUsername().equals(name))
                .sorted((o1, o2)->o2.getId().compareTo(o1.getId()))
                .limit(count)
                .map(this::mapToNoteDto)
                .toList();
    }

    @Override
    public NoteDtoResponse mapToNoteDto(Note note){
        NoteDtoResponse noteDtoResponse = new NoteDtoResponse();
        //3 column
        noteDtoResponse.setId(note.getId());
        noteDtoResponse.setMessage(note.getMessage());
        noteDtoResponse.setName(note.getUser().getUsername());
        return noteDtoResponse;
    }
    
    @Override
    public Note mapToNote(NoteDtoRequest noteDtoRequest){
        Note note = new Note();
        //2 column
        note.setMessage(noteDtoRequest.getMessage());
        note.setUser(userRepository.findByUsername(noteDtoRequest.getName()));
        return note;
    }
}
