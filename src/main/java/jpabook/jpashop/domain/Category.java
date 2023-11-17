package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    // 중간 테이블 맵핑 해줘야 함
    // (관계형 DB는 다대다 관계를 양쪽에 가질 수 없으므로 일대다 + 다대일로 풀어내는 중간 테이블이 필요함)
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    // 같은 엔티티에 대해서 셀프로 양방향 연관관계 설정 시작----------------------------------
    @ManyToOne(fetch = LAZY)  // EAGER 사용 시 N+1 문제 발생
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();
    // 같은 엔티티에 대해서 셀프로 양방향 연관관계 설정 끝------------------------------------

    /**
     * 연관관계 (편의) 메서드
     * @param child
     */
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
