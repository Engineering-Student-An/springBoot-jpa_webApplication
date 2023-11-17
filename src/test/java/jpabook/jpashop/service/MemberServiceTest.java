package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@RunWith(SpringRunner.class)    // JUnit과 spring 엮어서 사용한
@SpringBootTest                 // spring이랑 integration => spring boot 띄워서 테스트 가능
@Transactional                  // data 변경 => test case에 이 annotation 있으면 test 끝나면 기본적으로 Rollback함
public class MemberServiceTest {

    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;
    @Autowired EntityManager em;

    @Test
    // @Rollback(value = false)     // => Rollback X -> 실제 DB에 들어감
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);

        //then
        //em.flush();     // transaction이 커밋하는 순간 플러쉬 되면서 jpa 영속성 context에 있는 member 객체에 해당하는 insert 문이 만들어지면서 db에 날라감
                        // rollback하게 설정되어 있으므로 자체적으로 flush 해줌으로써 db에 날라가는 쿼리를 볼 수 있음 -> 실제 DB에는 안들어감, 쿼리만 볼 수 있음, 마지막에 Rollback 됨
        assertEquals(member, memberRepository.findOne(savedId));    // JPA에서 같은 entity (pk 값이 동일하면) 같은 영속성 context에서 똑같은 애로 관리됨! => TestCase : True
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        /*  => @Test(expected = IllegalStateException.class) 로 인해서 try-catch 사용 안해도 됨
        try{
            memberService.join(member2);        // 예외가 발생해야 한다!
        } catch (IllegalStateException e){
            return;
        }
         */
        memberService.join(member2);

        //then
        fail("예외가 발생해야 한다.");           // 이 코드가 실행되면 실패라는
    }

}