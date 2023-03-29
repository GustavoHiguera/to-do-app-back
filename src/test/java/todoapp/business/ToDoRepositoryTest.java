package todoapp.business;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import todoapp.persistence.exception.ToDoNotFoundException;
import todoapp.persistence.model.ToDo;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ToDoRepositoryTest {

    @Autowired
    ToDoRepository toDoRepository = Mockito.mock(ToDoRepository.class);

    @AfterEach
    void clearMemory() {
        toDoRepository.deleteAll();
    }

    // findAll()
    @Test
    void whenObjectsAdded_thenFindAll_ObjectReturned_andCheckSizeAndContent() {
        ToDo toDoOne = toDoRepository.save(new ToDo("Create tests", "High"));
        ToDo toDoTwo = toDoRepository.save(new ToDo("Start Front End", "Low", LocalDate.parse("2023-12-03")));
        ToDo toDoThree = toDoRepository.save(new ToDo("Start Back End", "Medium"));

        assertThat(toDoRepository.findAll())
                .hasSize(3)
                .contains(toDoOne)
                .contains(toDoTwo)
                .contains(toDoThree);
    }

    @Test
    void withNoObjectsAdded_returnEmptyArray() {
        assertThat(toDoRepository.findAll())
                .isEmpty();
    }

    //save()
    @Test
    void whenObjectGiven_andDoesNotExistsOnList_thenAddObject() {
        ToDo toDoCreated = new ToDo("Create tests", "High");

        assertThat(toDoRepository.findAll()).isEmpty();

        ToDo toDoSaved = toDoRepository.save(toDoCreated);

        assertThat(toDoRepository.findAll())
                .hasSize(1)
                .contains(toDoSaved);
    }

    @Test
    void whenObjectGiven_andExistsOnList_thenUpdateObject() {
        ToDo toDoSaved = toDoRepository.save(new ToDo("Create tests", "High"));

        assertThat(toDoRepository.findAll()).hasSize(1).contains(toDoSaved);
        assertEquals("Create tests", toDoSaved.getName());
        assertEquals("High", toDoSaved.getPriority());

        String savedId = toDoSaved.getId();

        ToDo toDoUpdated = new ToDo("Create Back End", "Low", LocalDate.parse("2023-06-29"));
        toDoUpdated.setId(savedId);

        toDoRepository.save(toDoUpdated);

        assertThat(toDoRepository.findAll()).hasSize(1).contains(toDoUpdated);
        assertEquals("Create Back End", toDoUpdated.getName());
        assertEquals("Low", toDoUpdated.getPriority());
        assertEquals(LocalDate.parse("2023-06-29"), toDoUpdated.getDueDate());

        assertEquals(toDoSaved.getId(), toDoUpdated.getId());
    }

    @Test
    void whenSaveObject_andObjectEqualsNull_thenThrowIllegalArgumentException() throws IllegalArgumentException {
        Throwable exception = assertThrows(
                IllegalArgumentException.class, () -> toDoRepository.save(null)
        );

        assertEquals("The object can't be null", exception.getMessage());
    }

    //getById
    @Test
    void whenGetById_ObjectFound_ObjectReturned() throws ToDoNotFoundException {
        ToDo toDoSaved = toDoRepository.save(new ToDo("Create tests", "High"));
        String idCreated = toDoSaved.getId();

        ToDo toDoObtained = toDoRepository.getById(idCreated);

        assertEquals(toDoSaved, toDoObtained);
        assertEquals(toDoSaved.getId(), toDoObtained.getId());

    }

    @Test
    void whenGetById_andIdEqualsNull_throwIllegalArgumentException() throws IllegalArgumentException {
        Throwable exception = assertThrows(
                IllegalArgumentException.class, () -> toDoRepository.getById(null)
        );

        assertEquals("The id parameter can't be null", exception.getMessage());
    }

    @Test
    void whenGetById_andObjectIsNotFound_throwToDoNotFoundException() {
        String invalidId = "a67-hsb6";
        Throwable exception = assertThrows(
                ToDoNotFoundException.class, () -> toDoRepository.getById(invalidId)
        );

        assertEquals("The to do with the id" + invalidId + " was not found", exception.getMessage());
    }

    // delete()
    @Test
    void whenObjectsAdded_thenDeleteById_checkSizeAndContent() throws ToDoNotFoundException {
        toDoRepository.save(new ToDo("Create tests", "High"));
        ToDo toDoToDelete = toDoRepository.save(new ToDo("Start Front End", "Low", LocalDate.parse("2023-12-03")));
        toDoRepository.save(new ToDo("Start Back End", "Medium"));

        toDoRepository.deleteById(toDoToDelete.getId());

        assertThat(toDoRepository.findAll())
                .hasSize(2)
                .doesNotContainNull()
                .doesNotContain(toDoToDelete);
    }

    @Test
    void whenDeleteById_IdEqualsNull_thenThrowIllegalArgumentException() throws IllegalArgumentException {
        Throwable exception = assertThrows(
                IllegalArgumentException.class, () -> toDoRepository.deleteById(null)
        );

        assertEquals("The id parameter can't be null", exception.getMessage());
    }

    @Test
    void whenIdInvalid_deleteById_thenThrowsToDoNotFoundException() {
        String invalidId = "a67-hsb6";
        Throwable exception = assertThrows(
                ToDoNotFoundException.class, () -> toDoRepository.deleteById(invalidId)
        );

        assertEquals("The to do with the id" + invalidId + " was not found", exception.getMessage());
    }
}