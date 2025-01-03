package com.kh.springJpa241217.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommentReqDto {  // 댓글 쓰기
    private String email;
    private Long boardId;
    private String content;
}
