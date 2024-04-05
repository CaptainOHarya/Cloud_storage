package ru.netology.cloud_storage.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // имя файла
    @Column(name = "name", nullable = false)
    String fileName;

    // тип (расширение файла)
    @Column(name = "content_type")// расширения файла может и не быть
    String fileType;


    // файл это массив байт
    @Column(name = "content", nullable = false)
    // @Lob// аннотация для хранения больших объёмов данных, используется для обозначения того, что поле в классе сущности должно быть сохранено как большой объект (LOB).
    byte[] content;

    // дата создания файла
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_date")
    Date createDate;

    // размер файла
    @Column(name = "size",  nullable = false)
    Long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    public File(Long id, String fileName, byte[] content, User user) {
        this.id = id;
        this.fileName = fileName;
        this.content = content;
        this.user = user;
    }

    public File(Long id, String fileName, User user) {
        this.id = id;
        this.fileName = fileName;
        this.user = user;
    }
}
