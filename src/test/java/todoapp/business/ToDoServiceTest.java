package todoapp.business;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;
import todoapp.persistence.model.ToDo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ToDoServiceTest {

    @Autowired
    ToDoService toDoService = Mockito.mock(ToDoService.class);

    @AfterEach
    void clearMemory() {
        toDoService.deleteAllToDos();
    }

    //addToDo

    @Test
    void whenAddToDo_thenFindAll_checkSizeAndContent_ToDoReturned() {

        ToDo toDoOne = toDoService.addToDo(new ToDo("Create tests", "High"));
        ToDo toDoTwo = toDoService.addToDo(new ToDo("Get milk", "Low", LocalDate.parse("2023-12-03")));
        ToDo toDoThree = toDoService.addToDo(new ToDo("Get cookies", "Medium"));

        assertThat(toDoService.findAllToDos())
                .hasSize(3)
                .contains(toDoOne)
                .contains(toDoTwo)
                .contains(toDoThree);
    }

    @Test
    void whenNullToDoAdded_ThenThrowResponseStatusException() throws ResponseStatusException {
        Throwable exception = assertThrows(
                ResponseStatusException.class, () -> toDoService.addToDo(null)
        );

        assertEquals("400 BAD_REQUEST \"The object can't be null\"", exception.getMessage());
    }

    @Test
    void whenNameParameter_isNull_ThenThrowResponseStatusException() throws ResponseStatusException {
        ToDo testToDo = new ToDo(null, "High");
        Throwable exception = assertThrows(
                ResponseStatusException.class, () -> toDoService.addToDo(testToDo)
        );

        assertEquals("400 BAD_REQUEST \"The name or the priority parameter can't be null\"", exception.getMessage());
    }

    @Test
    void whenPriorityParameter_isNull_ThenThrowResponseStatusException() throws ResponseStatusException {
        ToDo testToDo = new ToDo("Test name", null);
        Throwable exception = assertThrows(
                ResponseStatusException.class, () -> toDoService.addToDo(testToDo)
        );

        assertEquals("400 BAD_REQUEST \"The name or the priority parameter can't be null\"", exception.getMessage());
    }

    @Test
    void whenNameParameter_isEmpty_ThenThrowResponseStatusException() throws ResponseStatusException {
        ToDo testToDo = new ToDo("", "Medium", LocalDate.parse("2023-03-27"));

        Throwable exception = assertThrows(
                ResponseStatusException.class, () -> toDoService.addToDo(testToDo)
        );

        assertEquals("400 BAD_REQUEST \"The name parameter can't be empty or have more than 120 characters length\"", exception.getMessage());
    }

    @Test
    void whenNameParameterLength_isBigger_than120Chars_ThenThrowResponseStatusException() throws ResponseStatusException {
        ToDo testToDo = new ToDo("Example Text: Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque.", "Medium", LocalDate.parse("2023-03-27"));

        Throwable exception = assertThrows(
                ResponseStatusException.class, () -> toDoService.addToDo(testToDo)
        );

        assertEquals("400 BAD_REQUEST \"The name parameter can't be empty or have more than 120 characters length\"", exception.getMessage());
    }

    //findAllToDos
    @Test
    void whenFindAllToDos_withNoToDosAdded_getEmptyArray() {
        assertThat(toDoService.findAllToDos()).isEmpty();
    }

    @Test
    void whenFindAllToDos_withToDosAdded_checkSizeAndContent() {
        ToDo toDoOne = toDoService.addToDo(new ToDo("Create tests", "High"));
        ToDo toDoTwo = toDoService.addToDo(new ToDo("get bread", "Low", LocalDate.parse("2023-12-03")));
        ToDo toDoThree = toDoService.addToDo(new ToDo("Go for a walk", "Medium"));
        ToDo toDoFour = toDoService.addToDo(new ToDo("Study for the test", "Low", LocalDate.parse("2023-12-17")));

        assertThat(toDoService.findAllToDos())
                .hasSize(4)
                .contains(toDoOne)
                .contains(toDoTwo)
                .contains(toDoThree)
                .contains(toDoFour);
    }

    //delete
    @Test
    void whenAddedToDo_ThenDeleteToDo_checkArraySizeAndContent() {
        toDoService.addToDo(new ToDo("Create tests", "High"));
        ToDo toDoTwo = toDoService.addToDo(new ToDo("Start testing", "Low", LocalDate.parse("2023-12-03")));
        toDoService.addToDo(new ToDo("Start sleeping", "Medium"));

        assertThat(toDoService.findAllToDos())
                .hasSize(3)
                .contains(toDoTwo);

        toDoService.deleteToDo(toDoTwo.getId());

        assertThat(toDoService.findAllToDos()).hasSize(2)
                .doesNotContain(toDoTwo);
    }

    @Test
    void whenDeleteToDo_andToDoDoesNotExist_thenThrowResponseStatusException() throws ResponseStatusException {
        String invalidId = "3123j-321j4k2";
        Throwable exception = assertThrows(
                ResponseStatusException.class, () -> toDoService.deleteToDo(invalidId)
        );

        assertEquals("404 NOT_FOUND \"The to do with the id"+ invalidId +" was not found\"", exception.getMessage());
    }

    @Test
    void whenDeleteToDo_andIdEqualsNull_thenThrowResponseStatusException() throws ResponseStatusException {

        Throwable exception = assertThrows(
                ResponseStatusException.class, () -> toDoService.deleteToDo(null)
        );

        assertEquals("400 BAD_REQUEST \"The id parameter can't be null\"", exception.getMessage());
    }

    //Update
    @Test
    void ToDoAdded_ThenToDoSuccessfullyUpdated_andCheckArraySize() {
        ToDo testToDo = toDoService.addToDo(new ToDo("Start testing", "Low"));

        String toDoId = testToDo.getId();

        assertEquals("Start testing", testToDo.getName());
        assertEquals("Low", testToDo.getPriority());
        assertNull(testToDo.getDueDate());

        ToDo newToDo = toDoService.updateToDo(toDoId, new ToDo("Get coffee", "Medium", LocalDate.parse("2023-12-03")));

        assertEquals("Get coffee", testToDo.getName());
        assertEquals("Medium", testToDo.getPriority());
        assertEquals(LocalDate.parse("2023-12-03"), testToDo.getDueDate());

        assertEquals(newToDo.getId(), toDoId);

        assertThat(toDoService.findAllToDos()).hasSize(1).contains(newToDo);
    }

    @Test
    void whenUpdateToDo_butToDoDoesNotExists_thenThrowResponseStatusException() throws ResponseStatusException {
        String invalidId = "281fas-33jkd";
        Throwable exception = assertThrows(
                ResponseStatusException.class, () -> toDoService.updateToDo(invalidId, new ToDo("Get milk", "Medium", LocalDate.parse("2023-12-03")))
        );

        assertEquals("404 NOT_FOUND \"The to do with the id"+ invalidId +" was not found\"", exception.getMessage());
    }

    @Test
    void whenUpdateToDo_butNameParameterIsNull_ThenThrowResponseStatusException() throws ResponseStatusException {
        ToDo toDoAdded = toDoService.addToDo(new ToDo("Take a shower", "Low"));

        ToDo newToDo = new ToDo(null, "High", LocalDate.parse("2023-12-03"));

        Throwable exception = assertThrows(
                ResponseStatusException.class, () -> toDoService.updateToDo(toDoAdded.getId(), newToDo)
        );

        assertEquals("400 BAD_REQUEST \"The name or the priority parameter can't be null\"", exception.getMessage());
    }

    @Test
    void whenUpdateToDo_butPriorityParameter_isNull_ThenThrowResponseStatusException() throws ResponseStatusException {
        ToDo toDoAdded = toDoService.addToDo(new ToDo("Get some rest", "Low"));

        ToDo newToDo = new ToDo("test", null, LocalDate.parse("2023-12-03"));

        Throwable exception = assertThrows(
                ResponseStatusException.class, () -> toDoService.updateToDo(toDoAdded.getId(), newToDo)
        );

        assertEquals("400 BAD_REQUEST \"The name or the priority parameter can't be null\"", exception.getMessage());
    }

    @Test
    void whenUpdateToDo_butNameParameter_isEmpty_ThenThrowResponseStatusException() throws ResponseStatusException {
        ToDo toDoAdded = toDoService.addToDo(new ToDo("Sleep well", "Low"));

        ToDo newToDo = new ToDo("", "Medium", LocalDate.parse("2023-12-03"));

        Throwable exception = assertThrows(
                ResponseStatusException.class, () -> toDoService.updateToDo(toDoAdded.getId(), newToDo)
        );

        assertEquals("400 BAD_REQUEST \"The name parameter can't be empty or have more than 120 characters length\"", exception.getMessage());
    }

    @Test
    void whenUpdateToDo_butNameParameterLength_isBigger_than120Chars_ThenThrowResponseStatusException() throws ResponseStatusException {
        ToDo toDoAdded = toDoService.addToDo(new ToDo("Start testing", "Low"));

        ToDo newToDo = new ToDo("Dummy Text: Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque.", "Medium", LocalDate.parse("2023-03-27"));

        Throwable exception = assertThrows(
                ResponseStatusException.class, () -> toDoService.updateToDo(toDoAdded.getId(), newToDo)
        );

        assertEquals("400 BAD_REQUEST \"The name parameter can't be empty or have more than 120 characters length\"", exception.getMessage());
    }

    // markToDoAsDone

    @Test
    void whenMarkToDoAsDone_successfullyUpdated() {
        ToDo toDoAdded = toDoService.addToDo(new ToDo("Not blank", "Low"));
        toDoService.markToDoAsDone(toDoAdded.getId());

        assertTrue(toDoAdded.isDoneUndoneFlag());
    }

    @Test
    void whenMarkAsDone_butIdEqualsNull_thenThrowResponseStatusException() throws ResponseStatusException {
        Throwable exception = assertThrows(
                ResponseStatusException.class, () -> toDoService.markToDoAsDone(null)
        );

        assertEquals("400 BAD_REQUEST \"The id parameter can't be null\"", exception.getMessage());
    }

    @Test
    void whenMarkAsDone_butIdDoesNotExist_thenThrowResponseStatusException() throws ResponseStatusException {
        String invalidId = "7j23b-j1231";

        Throwable exception = assertThrows(
                ResponseStatusException.class, () -> toDoService.markToDoAsDone(invalidId)
        );

        assertEquals("404 NOT_FOUND \"The to do with the id"+ invalidId +" was not found\"", exception.getMessage());
    }

    // markToDoAsUndone
    @Test
    void whenMarkToDoAsUndone_successfullyUpdated() {
        ToDo toDoAdded = toDoService.addToDo(new ToDo("Do the tests", "Low"));
        toDoService.markToDoAsDone(toDoAdded.getId());

        assertTrue(toDoAdded.isDoneUndoneFlag());

        toDoService.markToDoAsUndone(toDoAdded.getId());

        assertFalse(toDoAdded.isDoneUndoneFlag());
        assertNull(toDoAdded.getDoneDate());
    }

    @Test
    void whenMarkAsUndone_butIdEqualsNull_thenThrowResponseStatusException() throws ResponseStatusException {
        Throwable exception = assertThrows(
                ResponseStatusException.class, () -> toDoService.markToDoAsUndone(null)
        );

        assertEquals("400 BAD_REQUEST \"The id parameter can't be null\"", exception.getMessage());
    }

    @Test
    void whenMarkAsUndone_butIdDoesNotExist_thenThrowResponseStatusException() throws ResponseStatusException {
        String invalidId = "7j23b-j1231";

        Throwable exception = assertThrows(
                ResponseStatusException.class, () -> toDoService.markToDoAsUndone(invalidId)
        );

        assertEquals("404 NOT_FOUND \"The to do with the id"+ invalidId +" was not found\"", exception.getMessage());
    }

    @Test
    void whenToDosAdded_thenFilterToDosByPriority_checkSizeAndContent() {
        ToDo toDoOne = toDoService.addToDo(new ToDo("Create tests", "High"));
        ToDo toDoTwo = toDoService.addToDo(new ToDo("Identify which IDE is best for java", "Low", LocalDate.parse("2023-12-03")));
        ToDo toDoThree = toDoService.addToDo(new ToDo("Search for help", "Medium"));
        ToDo toDoFour = toDoService.addToDo(new ToDo("Keep searching for help", "Low", LocalDate.parse("2023-12-17")));

        List<ToDo> newList = toDoService.getToDosSortedAndFilteredWithPagination("", "High", null, "default", "default", 1);

        assertThat(newList)
                .hasSize(1)
                .contains(toDoOne);
    }

    // getToDosSortedAndFiltered
    @Test
    void whenToDosAdded_thenFilterToDosByName_checkSizeAndContent() {
        ToDo toDoOne = toDoService.addToDo(new ToDo("Create tests", "High"));
        ToDo toDoTwo = toDoService.addToDo(new ToDo("Listen to new music", "Low", LocalDate.parse("2023-12-03")));
        ToDo toDoThree = toDoService.addToDo(new ToDo("test", "Medium"));
        ToDo toDoFour = toDoService.addToDo(new ToDo("Understand how this test works", "Low", LocalDate.parse("2023-12-17")));

        List<ToDo> newList = toDoService.getToDosSortedAndFilteredWithPagination("end", "default", null, "default", "default", 1);

        assertThat(newList)
                .hasSize(2)
                .contains(toDoTwo)
                .contains(toDoThree);
    }

    @Test
    void whenToDosAdded_thenFilterToDosByDoneUndoneFlag_checkSizeAndContent() {
        ToDo toDoOne = toDoService.addToDo(new ToDo("Create tests", "High"));
        ToDo toDoTwo = toDoService.addToDo(new ToDo("Bake muffins", "Low", LocalDate.parse("2023-12-03")));
        ToDo toDoThree = toDoService.addToDo(new ToDo("Wash tzutzu", "Medium"));
        ToDo toDoFour = toDoService.addToDo(new ToDo("Go see rings", "Low", LocalDate.parse("2023-12-17")));

        toDoService.markToDoAsDone(toDoFour.getId());

        List<ToDo> newList = toDoService.getToDosSortedAndFilteredWithPagination("", "default", false, "default", "default", 1);

        assertThat(newList)
                .hasSize(3)
                .contains(toDoOne)
                .contains(toDoTwo)
                .contains(toDoThree);
    }

    @Test
    void whenToDosAdded_thenSortedByDueDateAscendant_checkContent() {
        ToDo toDoOne = toDoService.addToDo(new ToDo("Create tests", "High", LocalDate.parse("2023-12-17")));
        ToDo toDoTwo = toDoService.addToDo(new ToDo("Start saving money", "Low", LocalDate.parse("2023-12-02")));
        ToDo toDoThree = toDoService.addToDo(new ToDo("Go to tha SAT", "Medium"));
        ToDo toDoFour = toDoService.addToDo(new ToDo("Do the laundry", "Low", LocalDate.parse("2023-12-06")));

        List<ToDo> actualList = toDoService.getToDosSortedAndFilteredWithPagination("", "default", null, "default", "asc", 1);
        List<ToDo> expectedList = new ArrayList<>();

        expectedList.add(toDoTwo);expectedList.add(toDoFour);expectedList.add(toDoOne);expectedList.add(toDoThree);

        assertThat(actualList).isEqualTo(expectedList);
    }

    @Test
    void whenToDosAdded_thenSortedByDueDateDescendant_checkContent() {
        ToDo toDoOne = toDoService.addToDo(new ToDo("Create tests", "High", LocalDate.parse("2023-12-17")));
        ToDo toDoTwo = toDoService.addToDo(new ToDo("Clean the bedroom", "Low", LocalDate.parse("2023-12-02")));
        ToDo toDoThree = toDoService.addToDo(new ToDo("Clean the bathroom", "Medium"));
        ToDo toDoFour = toDoService.addToDo(new ToDo("Take a shower", "Low", LocalDate.parse("2023-12-06")));

        List<ToDo> actualList = toDoService.getToDosSortedAndFilteredWithPagination("", "default", null, "default", "desc", 1);
        List<ToDo> expectedList = new ArrayList<>();

        expectedList.add(toDoOne);expectedList.add(toDoFour);expectedList.add(toDoTwo);expectedList.add(toDoThree);

        assertThat(actualList).isEqualTo(expectedList);
    }

    @Test
    void whenToDosAdded_thenSortedByPriorityAscendant_checkContent() {
        ToDo toDoOne = toDoService.addToDo(new ToDo("Create tests", "High", LocalDate.parse("2023-12-17")));
        ToDo toDoTwo = toDoService.addToDo(new ToDo("Get ready for the thesis", "Low", LocalDate.parse("2023-12-02")));
        ToDo toDoThree = toDoService.addToDo(new ToDo("Study study study", "Medium"));

        List<ToDo> actualList = toDoService.getToDosSortedAndFilteredWithPagination("", "default", null, "asc", "default", 1);
        List<ToDo> expectedList = new ArrayList<>();

        expectedList.add(toDoOne);expectedList.add(toDoThree);expectedList.add(toDoTwo);

        assertThat(actualList).isEqualTo(expectedList);
    }

    @Test
    void whenToDosAdded_thenSortedByPriorityDescendant_checkContent() {
        ToDo toDoOne = toDoService.addToDo(new ToDo("Create tests", "High", LocalDate.parse("2023-12-17")));
        ToDo toDoTwo = toDoService.addToDo(new ToDo("Study for QA", "Low", LocalDate.parse("2023-12-02")));
        ToDo toDoThree = toDoService.addToDo(new ToDo("pass some tests", "Medium"));

        List<ToDo> actualList = toDoService.getToDosSortedAndFilteredWithPagination("", "default", null, "desc", "default", 1);
        List<ToDo> expectedList = new ArrayList<>();

        expectedList.add(toDoTwo);expectedList.add(toDoThree);expectedList.add(toDoOne);

        assertThat(actualList).isEqualTo(expectedList);
    }

    @Test
    void whenToDosAdded_thenFilteredAndSorted_checkSizeAndContent() {
        ToDo toDoOne = toDoService.addToDo(new ToDo("Create tests", "High", LocalDate.parse("2023-12-17")));
        ToDo toDoTwo = toDoService.addToDo(new ToDo("Study for the exam", "Low", LocalDate.parse("2023-12-18")));
        ToDo toDoThree = toDoService.addToDo(new ToDo("Sleep", "Medium"));
        ToDo toDoFour = toDoService.addToDo(new ToDo("Go to Walmart", "Low", LocalDate.parse("2023-12-02")));
        ToDo toDoFive = toDoService.addToDo(new ToDo("Run 5 miles", "High", LocalDate.parse("2023-12-17")));
        ToDo toDoSix = toDoService.addToDo(new ToDo("Go to the doctor", "Low", LocalDate.parse("2023-12-13")));
        ToDo toDoSeven = toDoService.addToDo(new ToDo("Buy milk", "Medium"));
        ToDo toDoEight = toDoService.addToDo(new ToDo("Bake a cake", "Low"));

        List<ToDo> actualList = toDoService.getToDosSortedAndFilteredWithPagination("", "Low", null, "default", "desc", 1);
        List<ToDo> expectedList = new ArrayList<>();

        expectedList.add(toDoTwo);expectedList.add(toDoSix); expectedList.add(toDoFour);expectedList.add(toDoEight);

        assertThat(actualList).isEqualTo(expectedList);
    }

}