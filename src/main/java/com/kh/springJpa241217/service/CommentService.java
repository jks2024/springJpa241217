package com.kh.springJpa241217.service;

import com.kh.springJpa241217.dto.CommentReqDto;
import com.kh.springJpa241217.dto.CommentResDto;
import com.kh.springJpa241217.entity.Board;
import com.kh.springJpa241217.entity.Comment;
import com.kh.springJpa241217.entity.Member;
import com.kh.springJpa241217.repository.BoardRepository;
import com.kh.springJpa241217.repository.CommentRepository;
import com.kh.springJpa241217.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j   // log message
@Service // 스프링 컨테이너에 빈 등록
@RequiredArgsConstructor  // 생성자를 자동으로 생성
public class CommentService {
    private final MemberRepository memberRepository;  // member 정보를 가져 오기 위해서
    private final BoardRepository boardRepository; // 게시글 정보를 가져 오기 위해서
    private final CommentRepository commentRepository; // 댓글 작성을 위해서
    // 댓글 등록
    @Transactional
    public boolean commentRegister(CommentReqDto commentReqDto) {
        try {
            Member member = memberRepository.findByEmail(commentReqDto.getEmail())
                    .orElseThrow(()-> new RuntimeException("회원이 존재 하지 않습니다."));
            Board board = boardRepository.findById(commentReqDto.getBoardId())
                    .orElseThrow(()->new RuntimeException("게시글이 존재 하지 않습니다."));
            Comment comment = new Comment();
            comment.setContent(commentReqDto.getContent());
            comment.setMember(member);
            comment.setBoard(board);
            commentRepository.save(comment);
            return true;
        } catch (Exception e) {
            log.error("댓글 등록 실패 : {}", e.getMessage());
            return false;
        }
    }
    // 댓글 삭제
    public boolean commentDelete(Long commentId, String email) {
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(()->new RuntimeException("해당 댓글이 존재하지 않습니다."));
            if(comment.getMember().getEmail().equals(email)) {
                commentRepository.delete(comment);
                return true;
            } else {
                log.error("댓글은 작성자만 삭제 할 수 있습니다.");
                return false;
            }

        } catch (Exception e) {
            log.error("댓글 삭제에 실패 했습니다 : {}", e.getMessage());
            return false;
        }
    }

    // 댓글 목록 조회
    public List<CommentResDto> commentList(Long boardId) {
        try {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(()->new RuntimeException("해당 게시글이 존재하지 않습니다."));
            List<Comment> commentList = commentRepository.findByBoard(board); // 해당 보드에 대한 댓글 목록을 가져 옴
            List<CommentResDto> commentResDtoList = new ArrayList<>();
            for(Comment comment : commentList) {
                CommentResDto commentResDto = new CommentResDto();
                commentResDto.setBoardId(comment.getBoard().getId());
                commentResDto.setCommentId(comment.getCommentId());
                commentResDto.setEmail(comment.getMember().getEmail());
                commentResDto.setContent(comment.getContent());
                commentResDto.setRegDate(comment.getRegDate());
                commentResDtoList.add(commentResDto);
            }
            return commentResDtoList;

        } catch (Exception e) {
            log.error("맷글 목록 조회 실패 : {}", e.getMessage());
            return null;
        }
    }

}
