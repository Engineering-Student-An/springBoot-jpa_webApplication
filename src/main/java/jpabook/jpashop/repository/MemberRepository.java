package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor      // 3. @Autowired가 가능하기 때문에 @RequiredArgsConstructor 로 생성자 주입 가능
public class MemberRepository {

    //@Autowired              // 2. 스프링 데이터 jpa가 지원해주기 때문에 @PersistenceContext -> @Autowired로 대체 가능
    //@PersistenceContext     // 1. JPA가 제공하는 표준 annotation => spring이 EntityManager 생성해서 주입해줌
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    /**
     * 단건 조회
     * @param id
     * @return Member
     */
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    /**
     * 리스트 조회
     * @return List<Member>
     */
    public List<Member> findAll(){
        // JPQL : entity 객체에 대한 쿼리 vs SQL : 테이블에 대한 쿼리
        // createQuery : 첫번째 JPQL, 두번째 반환타입
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    /**
     * 이름으로 조회
     * @param name
     * @return List<Member>
     */
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
