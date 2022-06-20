package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Arrays;
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

    @Autowired
    TeamRepository teamRepository;

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

    @Test
    void findByUsernameAndAgeGreaterThen() {

        Member memberA = new Member("AAA", 11);
        Member memberB = new Member("AAA", 21);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 11);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(21);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    void top3Test() {
        List<Member> top3By = memberRepository.findTop3By();
        for (Member member : top3By) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 10);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);

        assertThat(result.get(0).getUsername()).isEqualTo(m1.getUsername());
        assertThat(result.get(0).getAge()).isEqualTo(m1.getAge());
    }


    @Test
    void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 10);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();

        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    void findMemberDto() {

        Team teamA = new Team("TeamA");
        teamRepository.save(teamA);

        Member m1 = new Member("AAA", 10, teamA);
        Member m2 = new Member("BBB", 10, teamA);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<MemberDto> memberDto = memberRepository.findMemberDto();

        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    void findByNames() {

        Team teamA = new Team("TeamA");
        teamRepository.save(teamA);

        Member m1 = new Member("AAA", 10, teamA);
        Member m2 = new Member("BBB", 10, teamA);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void returnTypeTest() {

        Team teamA = new Team("TeamA");
        teamRepository.save(teamA);

        Member m1 = new Member("AAA", 10, teamA);
        Member m2 = new Member("BBB", 10, teamA);
        Member m3 = new Member("BBB", 10, teamA);
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);

        Member findMember1 = memberRepository.findMemberByUsername("AAA");
        List<Member> findMember2 = memberRepository.findListByUsername("BBB");
        Optional<Member> findMember3 = memberRepository.findOptionalByUsername("AAA");

    }
}