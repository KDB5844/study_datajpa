package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
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

    @Test
    void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        // 단건 조회 검증
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // List 조회 검증
        List<Member> findMembers = memberRepository.findAll();
        assertThat(findMembers.size()).isEqualTo(2);

        // count 검증
        assertThat(memberRepository.count()).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        assertThat(memberRepository.count()).isEqualTo(0);
    }

}