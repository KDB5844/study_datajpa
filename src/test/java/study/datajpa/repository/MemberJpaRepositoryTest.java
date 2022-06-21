package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    TeamJpaRepository teamJpaRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    void save() {
        Member member = new Member("user1");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertThat(savedMember.getId()).isEqualTo(findMember.getId());
        assertThat(savedMember.getUsername()).isEqualTo(findMember.getUsername());
        assertThat(findMember).isEqualTo(member).isEqualTo(savedMember);
    }

    @Test
    void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        // 단건 조회 검증
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // List 조회 검증
        List<Member> findMembers = memberJpaRepository.findAll();
        assertThat(findMembers.size()).isEqualTo(2);

        // count 검증
        assertThat(memberJpaRepository.count()).isEqualTo(2);

        // 삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        assertThat(memberJpaRepository.count()).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThen() {

        Member memberA = new Member("AAA", 11);
        Member memberB = new Member("AAA", 21);

        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThen("AAA", 11);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(21);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    void paging() {
        memberJpaRepository.save(new Member("김덕배", 10));
        memberJpaRepository.save(new Member("최덕배", 10));
        memberJpaRepository.save(new Member("하덕배", 10));
        memberJpaRepository.save(new Member("박덕배", 10));

        List<Member> result = memberJpaRepository.findByPage(10, 0, 3);
        long totalCnt = memberJpaRepository.totalCount(10);
        System.out.println("totalCnt = " + totalCnt);
        result.stream().forEach(System.out::println);

    }

    @Test
    void bulkUpdate() {
        // given
        memberJpaRepository.save(new Member("김덕배", 11));
        memberJpaRepository.save(new Member("최덕배", 11));
        memberJpaRepository.save(new Member("하덕배", 10));
        memberJpaRepository.save(new Member("박덕배", 13));

        //when
        int count = memberJpaRepository.bulkAgePlus(11);

        //then
        assertThat(count).isEqualTo(3);
    }

}