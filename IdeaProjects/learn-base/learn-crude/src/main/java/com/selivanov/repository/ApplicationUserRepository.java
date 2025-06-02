package com.selivanov.repository;

import com.selivanov.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

@SuppressWarnings("all")
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Integer> {
    @Query("select u from ApplicationUser u left join fetch u.roles where u.username = :username")
    Optional<ApplicationUser> findApplicationUserByUsername(String username);

    @Query("select u from ApplicationUser u left join fetch u.roles where u.id = :id")
    Optional<ApplicationUser> findApplicationUserById(Integer id);

    @Query("select u from ApplicationUser u where u.username = :username")
    Optional<ApplicationUser> findUserByUsername(String username);

    @Modifying
    @Query(value = """
            insert into users_roles(user_id, role_id) 
            values (:userId, :roleId) 
            on conflict do nothing
            """, nativeQuery = true)
    void addRoleToUser(Integer userId, Integer roleId);

    @Modifying
    @Query(value = """
            delete from users_roles ur
            where ur.user_id = :userId and ur.role_id = :roleId
            """, nativeQuery = true)
    void removeRoleFromUser(Integer userId, Integer roleId);
}