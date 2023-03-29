package todoapp.persistence.exception;

public class ToDoNotFoundException extends ClassNotFoundException{
    public ToDoNotFoundException(String message) {
        super((message));
    }
}
