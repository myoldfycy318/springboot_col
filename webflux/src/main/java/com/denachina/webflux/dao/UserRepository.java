package com.denachina.webflux.dao;import com.denachina.webflux.pojo.User;import org.springframework.data.mongodb.repository.ReactiveMongoRepository;import org.springframework.stereotype.Repository;import reactor.core.publisher.Mono;/** * UserRepository * ReactiveCrudRepository已经提供了基本的增删改查的方法 * * @author shanmin.zhang * @date 18/12/6 **/public interface UserRepository extends ReactiveMongoRepository<User, String> {    /**     * 在此膜拜一下Spring团队的牛人们，使得我们仅需按照规则定义接口方法名即可完成DAO层逻辑的开发，牛     *     * @param username     * @return User     */    Mono<User> findUserByUsername(String username);    /**     * 我们仅需按照规则定义接口方法名即可完成DAO层逻辑的开发     *     * @param username     * @return User     */    Mono<Long> deleteByUsername(String username);}