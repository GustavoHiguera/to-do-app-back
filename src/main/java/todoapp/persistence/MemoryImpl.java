package todoapp.persistence;

import org.springframework.stereotype.Component;
import todoapp.business.ToDoRepository;
import todoapp.persistence.exception.ToDoNotFoundException;
import todoapp.persistence.model.ToDo;

import java.util.*;

@Component
public class MemoryImpl implements ToDoRepository {

    private final Map<String, ToDo> storedToDos = new HashMap<>();

    @Override
    public List<ToDo> findAll() {
        return new ArrayList<>(storedToDos.values());
    }

    @Override
    public ToDo save(ToDo toDo) throws IllegalArgumentException {
        if (toDo == null) {
            throw new IllegalArgumentException("The object can't be null");
        }

        if (storedToDos.containsKey(toDo.getId())) {
            storedToDos.replace(toDo.getId(), toDo);
            return toDo;
        } else if (toDo.getDueDate() == null) {
            ToDo newToDo = new ToDo(toDo.getName(), toDo.getPriority());
            newToDo.setId(UUID.randomUUID().toString());
            storedToDos.put(newToDo.getId(), newToDo);
            return newToDo;
        } else {
            ToDo newToDo = new ToDo(toDo.getName(), toDo.getPriority(), toDo.getDueDate());
            newToDo.setId(UUID.randomUUID().toString());
            storedToDos.put(newToDo.getId(), newToDo);
            return newToDo;
        }
    }

    @Override
    public ToDo getById(String id) throws IllegalArgumentException, ToDoNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("The id parameter can't be null");
        }

        return storedToDos.entrySet().stream().filter(toDo -> toDo.getValue().getId().equals(id)).findAny().orElseThrow(
                () -> new ToDoNotFoundException("The to do with the id" + id + " was not found")
        ).getValue();
    }

    @Override
    public void deleteById(String id) throws IllegalArgumentException, ToDoNotFoundException{
        if (id == null) {
            throw new IllegalArgumentException("The id parameter can't be null");
        }

        ToDo toDoFound = storedToDos.entrySet().stream().filter(toDo -> toDo.getValue().getId().equals(id)).findAny().orElseThrow(
                () -> new ToDoNotFoundException("The to do with the id" + id + " was not found")
        ).getValue();

        storedToDos.remove(id, toDoFound);
    }

    @Override
    public void deleteAll() {
        storedToDos.clear();
    }


}
