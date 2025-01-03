package com.kh.springJpa241217.repository;

import com.kh.springJpa241217.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 기본적인 CRUD는 이미 만들어져 있음
    boolean existsByEmail(String email); // select * from member where email = "";
    Optional<Member> findByEmail(String email);
    Optional<Member> findByPwd(String pwd);
    Optional<Member> findByEmailAndPwd(String email, String pwd);

    @EntityGraph(attributePaths = "boards")
    Optional<Member> findById(Long id);
}
