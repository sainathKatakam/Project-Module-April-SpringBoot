package com.scaler.springboot1.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TasksService {
    @Autowired
    private TaskRepo repo ;

    private int nextTaskId = 0;

    public List<Task> getAllTasks() {
        return repo.findAll();
    }

    public Task getTaskById(Integer id) {

         Optional<Task> task= repo.findById(id);
         if(task.isEmpty())
             throw new TaskNotFoundException(id);
         return task.get();
    }

    public Task createTask(Task task) {
       return repo.save(task);

    }


    public Task updateTask(Integer id, Date dueDate, Boolean completed) {
        Task task = getTaskById(id);
        if (dueDate != null) {
            task.setDueDate(dueDate);
        }
        if (completed != null) {
            task.setCompleted(completed);
        }

        return task;
    }

    public Task patchTask(Integer id, String name, Date dueDate, Boolean completed) {
        Task task = getTaskById(id);

        for (char c : name.toCharArray()) {
            if (!(Character.isAlphabetic(c) || Character.isSpaceChar(c)))
                throw new TaskNameIllegalException(name);
        }

        if (dueDate != null) {
            task.setDueDate(dueDate);
        } else
            throw new DueDateInValidException(dueDate);
        if (completed != null) {
            task.setCompleted(completed);
        }

        return repo.save(task);
    }


    public void deleteTask(Integer id) {
        Task task = getTaskById(id);
        repo.delete(task);
    }

    /*
Create a new class for Exception handling that extemdomg Runtime/IllegalStateException

*/
    public static class TaskNotFoundException extends IllegalStateException {
        public TaskNotFoundException(Integer id) {
            super("Task with id " + id + " not found");
        }
    }

    public static class DueDateInValidException extends RuntimeException {
        public DueDateInValidException(Date duedate) {
            super("Provide due Date " + duedate + " is NOT VALID");
        }
    }

    public static class TaskNameIllegalException extends RuntimeException {
        public TaskNameIllegalException(String name) {
            super("Provide Task Name " + name + " is NOT VALID");
        }
    }

}