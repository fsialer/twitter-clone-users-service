package com.fernando.ms.users.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.UserEntity;
import reactor.core.publisher.Flux;

public interface UserRepositoryCustom {
    Flux<UserEntity> findAllByFullNamePagination(String postId, int page, int size);
}
