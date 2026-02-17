package br.com.fiap.cheffy.infrastructure.persistence.user.repository;

import br.com.fiap.cheffy.infrastructure.persistence.user.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {

    List<UserJpaEntity> findAllByProfilesId(Long id);

    @EntityGraph(attributePaths = {"profiles", "addresses"})
    List<UserJpaEntity> findAll();

    //FIXME - Trocar retorno para List<UserJpaEntity>
    @Query("""
            SELECT distinct u FROM UserJpaEntity u 
                    JOIN FETCH u.profiles
                    LEFT JOIN FETCH u.addresses
                WHERE u.name = :name 
            """)
    Optional<UserJpaEntity> findByName(@Param("name") String name);

    @EntityGraph(attributePaths = {"profiles", "addresses"})
    Optional<UserJpaEntity> findByEmail(@Param("email") String email);

    boolean existsByEmailOrLogin(String email, String login);

    @EntityGraph(attributePaths = {"profiles", "addresses"})
    Optional<UserJpaEntity> findById(@Param("id") UUID id);

    @EntityGraph(attributePaths = {"profiles", "addresses"})
    Optional<UserJpaEntity> findByLogin(@Param("login") String login);


}