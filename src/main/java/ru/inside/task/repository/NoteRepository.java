package ru.inside.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.inside.task.model.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
}
