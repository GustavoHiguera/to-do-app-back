package todoapp.business;

import todoapp.persistence.exception.ToDoNotFoundException;
import todoapp.persistence.model.ToDo;

import java.awt.print.Pageable;
import java.util.List;

//@Repository
public interface ToDoRepository {
    List<ToDo> findAll();
    ToDo save(ToDo toDo) throws IllegalArgumentException;
    ToDo getById(String id) throws IllegalArgumentException, ToDoNotFoundException;
    void deleteById(String id) throws IllegalArgumentException, ToDoNotFoundException;
    void deleteAll();
}
