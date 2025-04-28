package com.fernando.ms.users.app.utils;

import com.fernando.ms.users.app.domain.models.Follow;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateFollowRequest;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.FollowDocument;

import java.time.LocalDateTime;

public class TestUtilFollow {
    public static FollowDocument buildFollowDocumentMock(){
        return FollowDocument.builder()
                .id("pod44851125dffe6e2de223e55b")
                .followerId("68045526dffe6e2de223e55b")
                .followedId("fdsfds4544")
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Follow buildFollowMock(){
        return Follow.builder()
                .id("pod44851125dffe6e2de223e55b")
                .followerId("68045526dffe6e2de223e55b")
                .followedId("fdsfds4544")
                .build();
    }

    public static CreateFollowRequest buildCreateFollowRequestMock(){
        return CreateFollowRequest.builder()
                .followedId("fdsfds4544")
                .build();
    }
}
