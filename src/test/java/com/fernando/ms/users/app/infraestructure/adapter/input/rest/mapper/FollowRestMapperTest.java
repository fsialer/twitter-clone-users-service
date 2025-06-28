package com.fernando.ms.users.app.infraestructure.adapter.input.rest.mapper;

import com.fernando.ms.users.app.domain.models.Follow;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper.FollowRestMapper;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateFollowRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.ExistsUserFollowedResponse;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.QuantityFollowersResponse;
import com.fernando.ms.users.app.utils.TestUtilFollow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FollowRestMapperTest {
    private FollowRestMapper followRestMapper;

    @BeforeEach
    void setUp(){
        followRestMapper= Mappers.getMapper(FollowRestMapper.class);
    }

    @Test
    @DisplayName("When Map UserId And CreateFollowRequest Expect FluxUser")
    void When_MapUserIDAndCreateFollowRequest_Expect_Follow(){
        CreateFollowRequest createFollowRequest= TestUtilFollow.buildCreateFollowRequestMock();

        Follow follow=followRestMapper.toFollow("cde8c071a420424abf28b189ae2cd6982",createFollowRequest);
        assertEquals("cde8c071a420424abf28b189ae2cd6982", follow.getFollowerId());
        assertEquals("fdsfds4544", follow.getFollowedId());
    }

    @Test
    @DisplayName("When Map Boolean Expect ExistsUserFollowedResponse")
    void When_MapBoolean_Expect_ExistsUserFollowedResponse(){
        ExistsUserFollowedResponse existsUserFollowedResponse=followRestMapper.toExistsUserFollowed(Boolean.TRUE);
        assertTrue(existsUserFollowedResponse.exists());
    }

    @Test
    @DisplayName("When Map Quantity Expect Quantity Followers Response")
    void When_MapQuantity_Expect_QuantityFollowersResponse(){
        QuantityFollowersResponse quantityFollowersResponse=followRestMapper.toQuantityFollowersResponse(1L);
        assertEquals(1L,quantityFollowersResponse.quantity());
    }

}
