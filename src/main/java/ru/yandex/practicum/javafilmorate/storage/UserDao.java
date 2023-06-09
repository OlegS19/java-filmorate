package ru.yandex.practicum.javafilmorate.storage;

import ru.yandex.practicum.javafilmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    User createUser(User user);

    Optional<User> getUserById(int id);

    List<User> getAllUsers();

    User updateUser(User user);

}
