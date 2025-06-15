package com.fernando.ms.users.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom{
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    @Override
    public Flux<UserEntity> findAllByFullNamePagination(String fullName, int page, int size) {
        Query query=new Query(Criteria.where("fullName").regex(".*" + fullName + ".*", "i"));
        query.with(Sort.by(Sort.Direction.DESC,"fullName"))
                .skip((long) (page-1)*size)
                .limit(size);
        return reactiveMongoTemplate.find(query, UserEntity.class);
    }
}
