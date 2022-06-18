package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void save() {
        Member member = new Member("memberA");

        Member savedMember = memberRepository.save(member);
        Optional<Member> findMember = memberRepository.findById(savedMember.getId());

        if(findMember.isEmpty()) throw new IllegalArgumentException("찾는 회원이 없습니다.");

        Member member1 = findMember.get();

        assertThat(savedMember.getId()).isEqualTo(member1.getId());
        assertThat(savedMember.getUsername()).isEqualTo(member1.getUsername());
        assertThat(savedMember).isEqualTo(member1);

    }

}