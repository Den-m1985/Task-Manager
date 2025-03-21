package com.example.model.map_stract;

import com.example.model.Comment;
import com.example.model.Task;
import com.example.model.User;
import com.example.model.dto.TaskRequest;
import com.example.model.dto.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "authorId", source = "author", qualifiedByName = "userToId")
    @Mapping(target = "assigneeId", source = "assignee", qualifiedByName = "userToId")
    @Mapping(target = "commentsIds", source = "comments", qualifiedByName = "mapCommentIds")
    TaskResponse taskToDto(Task task);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    Task taskDtoToEntity(TaskRequest request);

    @Named("userToId")
    default Integer userToId(User user) {
        return user != null ? user.getId() : null;
    }

    @Named("mapCommentIds")
    default List<Integer> mapCommentIds(List<Comment> comments) {
        return comments != null ? comments.stream()
                .map(com.example.model.Comment::getId)
                .collect(Collectors.toList()) : null;
    }
}
