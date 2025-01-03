package com.kh.springJpa241217.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 게시글에 관한 Entity
@Entity
@Table(name="board")
@Getter @Setter @ToString
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private String title;  // 글 제목
    @Lob
    @Column(length = 1000)
    private String content; // 글 내용

    private String imgPath; // 게시글 이미지 경로
    private LocalDateTime regDate;  // 게시글 등록 일자
    @PrePersist
    public void prePersist() {
        regDate = LocalDateTime.now();
    }
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    // 영속성 전이 : 부모 엔티티의 상태 변화가 자식 엔티티에도 전이 되는 것
    // 고아 객체 제거 : 부모와의 연관 관계가 끊어진 자식 엔티티를 자동으로 데이터베이스에서 제거하는 것
    // 부모가 관리하는 List에서 해당 객체를 삭제하는 경우 실제 DB에서 삭제 됨
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setBoard(this);
    }
    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setBoard(null);
    }
}
