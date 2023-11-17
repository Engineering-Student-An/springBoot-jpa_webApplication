package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("A")            // 구분을 위해서 넣는 값 => Album : "A" / 기본으로 두면 클래스 이름으로 자동
@Getter
@Setter
public class Album extends Item{     // 상속받음

    private String artist;

    private String etc;
}
