package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("B")            // 구분을 위해서 넣는 값 => Book : "B" / 기본으로 두면 클래스 이름으로 자동
@Getter
@Setter
public class Book extends Item{     // 상속받음
    private String author;

    private String isbn;
}
