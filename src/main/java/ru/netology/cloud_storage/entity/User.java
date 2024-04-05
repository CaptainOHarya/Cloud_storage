package ru.netology.cloud_storage.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
// @Table(name = "users", schema = "cloud_storage")
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false, length = 50)
    String name;

    @Column(name = "surname", nullable = false, length = 50)
    String surname;

    @Column(name = "age", nullable = false)
    int age;

    @Column(name = "phone_number", unique = true, nullable = false)
    String phoneNumber;

    @Column(name = "login", unique = true, nullable = false, length = 50)
    String login;

    @Column(name = "password", nullable = false, length = 255)
    String password;

    @Column(name = "email", nullable = false, length = 255)
    String email;


}