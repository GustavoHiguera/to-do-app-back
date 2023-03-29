package todoapp.presentation;

import todoapp.business.ToDoService;
import todoapp.persistence.model.ToDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/toDo")
public class ToDoController {

    private final ToDoService toDoService;

    @Autowired
    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @CrossOrigin
    @GetMapping("/getAll")
    public ResponseEntity<Collection<ToDo>> getAllToDos() {
        Collection<ToDo> toDos = toDoService.findAllToDos();
        return new ResponseEntity(toDos, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/addToDo")
    public ResponseEntity<ToDo> addToDo(@RequestBody ToDo toDo){
        ToDo newToDo = toDoService.addToDo(toDo);
        return new ResponseEntity(newToDo, HttpStatus.CREATED);
    }

    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ToDo> deleteToDo(@PathVariable("id") String id) {
        toDoService.deleteToDo(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/{id}/done")
    public ResponseEntity<ToDo> markToDoAsDone(@PathVariable("id") String id) {
        ToDo toDo = toDoService.markToDoAsDone(id);
        return new ResponseEntity(toDo, HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/{id}/undone")
    public ResponseEntity<ToDo> markAsUndone(@PathVariable("id") String id) {
        ToDo toDo = toDoService.markToDoAsUndone(id);
        return new ResponseEntity(toDo, HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/{id}/update")
    public ResponseEntity<ToDo> updateToDo(@PathVariable("id") String id, @RequestBody ToDo toDo){
        ToDo toDoUpdated = toDoService.updateToDo(id, toDo);
        return new ResponseEntity(toDoUpdated, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/sortToDos")
    public ResponseEntity<List<ToDo>> sortToDos(@RequestBody List<ToDo> prevList, String priorityOrder,  String dateOrder) {
        List<ToDo> toDosSorted = toDoService.stableSort(prevList, priorityOrder, dateOrder);
        return new ResponseEntity(toDosSorted, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/filterByName")
    public ResponseEntity<List<ToDo>> filterToDoByName(@RequestBody List<ToDo> prevList, String name) {
        List<ToDo> toDosFiltered = toDoService.filterToDosByName(prevList, name);
        return new ResponseEntity(toDosFiltered, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/filterByPriority")
    public ResponseEntity<List<ToDo>> filterToDoByPriority(@RequestBody List<ToDo> prevList, String priority) {
        List<ToDo> toDosFiltered = toDoService.filterToDosByPriority(prevList, priority);
        return new ResponseEntity(toDosFiltered, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/filterByFlag")
    public ResponseEntity<List<ToDo>> filterToDoByFlag(@RequestBody List<ToDo> prevList, Boolean flag) {
        List<ToDo> toDosFiltered = toDoService.filterToDosByFlag(prevList, flag);
        return new ResponseEntity(toDosFiltered, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/getStats")
    public ResponseEntity<List<Long>> getToDoStats() {
        List<Long> stats = toDoService.getStatistics();
        return new ResponseEntity(stats, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/getToDosFilteredAndSorted")
    public ResponseEntity<List<ToDo>> getToDosSortedAndFilteredWithPagination(String name, String priority, Boolean doneUnDoneFlag, String priorityOrder, String dateOrder, int pageNumber) {
        List<ToDo> toDosFilteredAndSorted = toDoService.getToDosSortedAndFilteredWithPagination(name, priority, doneUnDoneFlag, priorityOrder, dateOrder, pageNumber);
        return new ResponseEntity(toDosFilteredAndSorted, HttpStatus.OK);
    }
    }
