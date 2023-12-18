package com.camilopoblete.ejerciciotecnico.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Data
@Entity
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String created;
    private String lastLogin;
    private String name;
    @Column(name = "TOKEN", length = 2000, columnDefinition = "VARCHAR(1000)")
    private String Token;
    private boolean isActive;

    @NotNull(message = "Email requerido")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "email no cumple formato : aaaaaaa@undominio.algo")
    private String email;

    @NotNull(message = "Password requerido")
    private String password;

    @ElementCollection
    @CollectionTable(name = "custom_objects", joinColumns = @JoinColumn(name = "user_id"))
    List<Phone> phones;
}