package com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response;

import lombok.Builder;

@Builder
public record ExistsUserFollowedResponse(Boolean exists){}