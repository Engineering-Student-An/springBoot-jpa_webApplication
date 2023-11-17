package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)     // 읽기 전용
//@AllArgsConstructor
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

//    @AllArgsConstructor[모든 필드] / @RequiredArgsConstructor[final 필드] 생성자 주입 (아래 코드) 동일하게 해줌!
//    //@Autowired (생략가능 : 생성자가 하나뿐이므로)
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입
     * @param member
     * @return Long
     */
    @Transactional                  // 읽기 전용 X
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복 회원 검증
     * 중복이 있으면 예외 발생
     * @param member
     */
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     * @return List<Member>
     */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    /**
     * 회원 단건 조회
     * @param memberId
     * @return Member
     */
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
