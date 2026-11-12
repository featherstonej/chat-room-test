package com.example.chat_middleware.dao;

import com.example.chat_middleware.dao.entities.UserFriends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserFriendDao extends JpaRepository<UserFriends, UUID> {

    /**
     * Retrieve all friendship records involving a user as either person1 or person2.
     *
     * @param person1Id the user's UUID
     * @param person2Id the same user's UUID
     * @return list of friendship records
     */
    List<UserFriends> findByPerson1_IdOrPerson2_Id(UUID person1Id, UUID person2Id);

    /**
     * Retrieve sent friend requests by initiator with a given status (e.g., 'PENDING', 'ACCEPTED').
     *
     * @param person1Id initiator user UUID
     * @param status relationship status
     * @return list of friendships
     */
    List<UserFriends> findByPerson1_IdAndStatus(UUID person1Id, String status);

    /**
     * Retrieve received friend requests by recipient with a given status (e.g., 'PENDING').
     *
     * @param person2Id recipient user UUID
     * @param status relationship status
     * @return list of friendships
     */
    List<UserFriends> findByPerson2_IdAndStatus(UUID person2Id, String status);

    /**
     * Find all friendships for a user matching a specific status regardless of who initiated.
     *
     * @param userId the user's UUID
     * @param status relationship status (e.g., 'ACCEPTED', 'BLOCKED')
     * @return list of friendships
     */
    @Query("SELECT uf FROM UserFriends uf WHERE (uf.person1.id = :userId OR uf.person2.id = :userId) AND uf.status = :status")
    List<UserFriends> findByUserIdAndStatus(@Param("userId") UUID userId, @Param("status") String status);

    /**
     * Find the relationship entry between two users in either direction.
     *
     * @param u1 first user UUID
     * @param u2 second user UUID
     * @return Optional containing the friendship relationship if one exists
     */
    @Query("SELECT uf FROM UserFriends uf WHERE (uf.person1.id = :u1 AND uf.person2.id = :u2) OR (uf.person1.id = :u2 AND uf.person2.id = :u1)")
    Optional<UserFriends> findFriendshipBetween(@Param("u1") UUID u1, @Param("u2") UUID u2);

    /**
     * Find a relationship where person1 sent a request to person2 specifically.
     *
     * @param person1Id initiator user UUID
     * @param person2Id recipient user UUID
     * @return Optional containing UserFriends record
     */
    Optional<UserFriends> findByPerson1_IdAndPerson2_Id(UUID person1Id, UUID person2Id);

    /**
     * Check if a specific directed friend relation exists.
     *
     * @param person1Id initiator user UUID
     * @param person2Id recipient user UUID
     * @return true if exists, false otherwise
     */
    boolean existsByPerson1_IdAndPerson2_Id(UUID person1Id, UUID person2Id);

    /**
     * Check whether two users have an active friendship status (e.g., 'ACCEPTED') in either direction.
     *
     * @param u1 first user UUID
     * @param u2 second user UUID
     * @param status friendship status to check for
     * @return true if active friends, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(uf) > 0 THEN TRUE ELSE FALSE END FROM UserFriends uf " +
           "WHERE ((uf.person1.id = :u1 AND uf.person2.id = :u2) OR (uf.person1.id = :u2 AND uf.person2.id = :u1)) " +
           "AND uf.status = :status")
    boolean areFriends(@Param("u1") UUID u1, @Param("u2") UUID u2, @Param("status") String status);
}
