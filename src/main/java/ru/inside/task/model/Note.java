package ru.inside.task.model;


import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Note object.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "o_note")
public class Note {

    /**
     * Id task.
     */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Note's message.
     */
    @Column(nullable = false)
    private String message;

    /**
     * Note's author
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
