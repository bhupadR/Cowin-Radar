package cowin.appointment.finder.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "message_archive")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MessageArchive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "message",length = 5000)
    private String message;
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}
