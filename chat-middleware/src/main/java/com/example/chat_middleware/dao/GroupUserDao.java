package com.example.chat_middleware.dao;

import com.example.chat_middleware.dao.entities.GroupUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupUserDao extends JpaRepository<GroupUsers, UUID> {

    /**
     * Retrieve all member associations in a specific group.
     *
     * @param groupId the group's UUID
     * @return list of group memberships
     */
    List<GroupUsers> findByGroup_Id(UUID groupId);

    /**
     * Retrieve all group memberships for a specific user.
     *
     * @param userId the user's UUID
     * @return list of group memberships
     */
    List<GroupUsers> findByUser_Id(UUID userId);

    /**
     * Find a user's membership in a group (useful for permission and role checking).
     *
     * @param groupId the group's UUID
     * @param userId the user's UUID
     * @return Optional containing the GroupUsers record if found
     */
    Optional<GroupUsers> findByGroup_IdAndUser_Id(UUID groupId, UUID userId);

    /**
     * Check whether a user is currently a member of a group.
     *
     * @param groupId the group's UUID
     * @param userId the user's UUID
     * @return true if member, false otherwise
     */
    boolean existsByGroup_IdAndUser_Id(UUID groupId, UUID userId);

    /**
     * Find members of a group having a specific role (e.g., ADMIN, MODERATOR, MEMBER).
     *
     * @param groupId the group's UUID
     * @param role the role title
     * @return list of group users
     */
    List<GroupUsers> findByGroup_IdAndRole(UUID groupId, String role);

    /**
     * Remove a user from a group.
     *
     * @param groupId the group's UUID
     * @param userId the user's UUID
     */
    void deleteByGroup_IdAndUser_Id(UUID groupId, UUID userId);

    /**
     * Count total members in a group.
     *
     * @param groupId the group's UUID
     * @return member count
     */
    long countByGroup_Id(UUID groupId);
}
