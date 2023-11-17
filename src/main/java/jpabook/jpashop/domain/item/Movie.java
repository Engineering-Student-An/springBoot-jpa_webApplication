package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("M")            // 구분을 위해서 넣는 값 => Movie : "M" / 기본으로 두면 클래스 이름으로 자동
@Getter @Setter
public class Movie extends Item {   // 상속받음

    private String director;

    private String actor;
}
