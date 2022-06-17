package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    @Transactional
    void save() {
        Member member = new Member("user1");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getMemberId());

        assertThat(savedMember.getMemberId()).isEqualTo(findMember.getMemberId());
        assertThat(savedMember.getUsername()).isEqualTo(findMember.getUsername());
    }

    @Test
    void find() {
    }
}