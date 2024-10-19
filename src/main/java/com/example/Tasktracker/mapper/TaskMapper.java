package com.example.Tasktracker.mapper;

import com.example.Tasktracker.dto.request.UpsertTaskRequest;
import com.example.Tasktracker.dto.response.TaskResponse;
import com.example.Tasktracker.model.Task;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@DecoratedWith(TaskMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class})
public interface TaskMapper {

    Task requestToTask(UpsertTaskRequest request);

    Task requestToTask(String id, UpsertTaskRequest request);

    TaskResponse taskToResponse(Task task);

}
