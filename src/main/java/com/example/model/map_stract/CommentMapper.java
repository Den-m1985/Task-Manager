package com.example.model.map_stract;

import com.example.model.Comment;
import com.example.model.dto.CommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "assigneeId", source = "assignee.id")
    CommentResponse commentToDto(Comment comment);
}
