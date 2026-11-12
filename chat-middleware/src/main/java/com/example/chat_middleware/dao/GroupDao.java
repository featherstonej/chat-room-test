package com.example.chat_middleware.dao;

import com.example.chat_middleware.dao.entities.Groups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupDao extends JpaRepository<Groups, UUID> {

    /**
     * Search groups by name (case-insensitive).
     *
     * @param name group name search query
     * @return list of matching groups
     */
    List<Groups> findByNameContainingIgnoreCase(String name);

    /**
     * Find all groups that a specific user belongs to.
     *
     * @param userId the user's UUID
     * @return list of groups the user is a member of
     */
    @Query("SELECT gu.group FROM GroupUsers gu WHERE gu.user.id = :userId")
    List<Groups> findGroupsByUserId(@Param("userId") UUID userId);

    /**
     * Check if a group with a specific name already exists.
     *
     * @param name group name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);
}
