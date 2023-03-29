package todoapp.presentation;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import todoapp.business.ToDoService;
import todoapp.persistence.model.ToDo;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(ToDoController.class)
class ToDoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToDoService toDoService;

    @Test
    void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

// findAllToDos
    @Test
    void whenGetAllToDos_withNoToDosAdded_thenHttp200_andReturnEmptyArray() throws Exception {
        ArrayList<ToDo> toDosList = new ArrayList<>();

        Mockito.doReturn(toDosList).when(toDoService).findAllToDos();

        mockMvc.perform(MockMvcRequestBuilders.get("/toDo/getAll"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void whenGetAllToDos_withToDosAdded_thenHttp200_andCheckSize() throws Exception {
        ArrayList<ToDo> toDosList = new ArrayList<>();
        ToDo toDoOne = toDoService.addToDo(new ToDo("Create tests", "High"));
        toDosList.add(toDoOne);

        Mockito.doReturn(toDosList).when(toDoService).findAllToDos();

        mockMvc.perform(MockMvcRequestBuilders.get("/toDo/getAll"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void whenAddToDo_thenGetHttp200() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/toDo/addToDo")
                .content("{\"name\": \"Custom Task\", \"priority\": \"High\", \"dueDate\": \"2023-12-03\"}\"")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
    }
    // deleteToDo
    @Test
    void whenAddedToDo_thenDeleteToDo_Http200() throws Exception {
        ToDo toDo = new ToDo("Custom", "High");
        toDo.setId("customId");

        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/toDo/delete/{id}", toDo.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }

    @Test
    void whenMarkToDoAsDone_thenHttp200()throws Exception {
        ToDo toDo = new ToDo("Custom", "High");
        toDo.setId("customId");
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/toDo/{id}/done", toDo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }

    @Test
    void whenMarkToDoAsUndone_thenHttp200()throws Exception {
        ToDo toDo = new ToDo("Custom", "High");
        toDo.setId("customId");
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/toDo/{id}/undone", toDo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }

    @Test
    void whenGetStatistics_thenHttp200() throws  Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/toDo/getStats"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }

    @Test
    void whenGetToDosFilteredAndSorted_thenHttp200() throws  Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/toDo/getToDosFilteredAndSorted?name=&priority=default&doneUnDoneFlag=true&priorityOrder=default&dateOrder=desc&pageNumber=1"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }
}

