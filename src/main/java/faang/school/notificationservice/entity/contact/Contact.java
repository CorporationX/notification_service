package faang.school.notificationservice.entity.contact;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import faang.school.notificationservice.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "contact", length = 128, nullable = false, unique = true)
    private String contact;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private ContactType type;
}