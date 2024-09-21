package com.example.Tasktracker.mapper;

import com.example.Tasktracker.dto.request.UpsertTaskRequest;
import com.example.Tasktracker.dto.response.TaskResponse;
import com.example.Tasktracker.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class})
public interface TaskMapper {

    Task requestToTask(UpsertTaskRequest request);

    //    @Mapping(source = "taskId", target = "id")
    Task requestToTask(String id, UpsertTaskRequest request);

    TaskResponse taskToResponse(Task task);
//
//    List<TaskResponse> taskListToResponseList(List<Task> tasks);
//
//    default TaskListResponse taskListToTaskListResponse(List<Task> tasks) {
//        TaskListResponse response = new TaskListResponse();
//        response.setTasks(taskListToResponseList(tasks));
//
//        return response;
//    }

}
