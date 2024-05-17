package com.panda.pandasocialmediaback.repository;

import com.panda.pandasocialmediaback.document.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
    @Query("{'$or':[{'fullName': { $regex: ?0, $options: 'i' }}, {'email': { $regex: ?0, $options: 'i' }}]}")
    Flux<User> searchUser(@Param("query") String query);

    Mono<User> findByEmail(String email);
}
