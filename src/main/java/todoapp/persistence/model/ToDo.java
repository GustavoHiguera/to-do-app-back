package todoapp.persistence.model;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ToDo {

    private String id;
    @NotNull
    private String name;
    @NotNull
    private String priority;
    private LocalDate dueDate;
    private LocalDateTime creationDate;
    private LocalDateTime doneDate;
    private boolean doneUndoneFlag;
    private Long timeToComplete;
    private Long daysToComplete;

    public ToDo() {
    }

    public ToDo(String name, String priority, LocalDate dueDate) {
        this.name = name;
        this.priority = priority;
        this.dueDate = dueDate;
        this.creationDate = java.time.LocalDateTime.now();
        this.doneDate = null;
        this.doneUndoneFlag = false;
        this.timeToComplete = null;
        this.daysToComplete = ChronoUnit.DAYS.between(this.creationDate, this.dueDate.atStartOfDay().plusDays(1));
    }

    public ToDo(String name, String priority) {
        this.name = name;
        this.priority = priority;
        this.dueDate = null;
        this.creationDate = java.time.LocalDateTime.now();
        this.doneDate = null;
        this.doneUndoneFlag = false;
        this.timeToComplete = null;
        this.daysToComplete = 0L;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(LocalDateTime doneDate) {
        this.doneDate = doneDate;
    }

    public boolean isDoneUndoneFlag() {
        return doneUndoneFlag;
    }

    public void setDoneUndoneFlag(boolean doneUndoneFlag) {
        this.doneUndoneFlag = doneUndoneFlag;
    }

    public Long getTimeToComplete() {
        return timeToComplete;
    }

    public void setTimeToComplete(Long timeToComplete) {
        this.timeToComplete = timeToComplete;
    }

    public Long getDaysToComplete() {
        return daysToComplete;
    }

    public void setDaysToComplete(Long daysToComplete) {
        this.daysToComplete = daysToComplete;
    }

    @Override
    public String toString() {
        return "ToDo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", priority='" + priority + '\'' +
                ", dueDate=" + dueDate +
                ", creationDate=" + creationDate +
                ", doneDate=" + doneDate +
                ", doneUndoneFlag=" + doneUndoneFlag +
                ", timeToComplete=" + timeToComplete +
                ", daysToComplete=" + daysToComplete +
                '}';
    }
}
