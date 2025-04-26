package com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.users.app.domain.models.Follow;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateFollowRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FollowRestMapper {
    default Follow toFollow(String userId, CreateFollowRequest rq){
        return Follow.builder()
                .followerId(userId)
                .followedId(rq.getFollowedId())
                .build();
    }
}
