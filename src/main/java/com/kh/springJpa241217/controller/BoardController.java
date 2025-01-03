package com.kh.springJpa241217.controller;

import com.kh.springJpa241217.dto.BoardReqDto;
import com.kh.springJpa241217.dto.BoardResDto;
import com.kh.springJpa241217.dto.CommentReqDto;
import com.kh.springJpa241217.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    // 게시글 등록
    @PostMapping("/new")
    public ResponseEntity<Boolean> boardRegister(@RequestBody BoardReqDto boardReqDto) {
        boolean isSuccess = boardService.saveBoard(boardReqDto);
        return ResponseEntity.ok(isSuccess);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> boardModify(@PathVariable Long id, @RequestBody BoardReqDto boardReqDto) {
        boolean isSuccess = boardService.modifyBoard(id, boardReqDto);
        return ResponseEntity.ok(isSuccess);
    }

    // 게시글 전체 목록 조회
    @GetMapping("")
    public ResponseEntity<List<BoardResDto>> boardList() {
        List<BoardResDto> list = boardService.findAllBoard();
        return ResponseEntity.ok(list);
    }

    // 게시글 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<BoardResDto> boardDetail(@PathVariable Long id) {
        BoardResDto boardResDto = boardService.findByBoardId(id);
        return ResponseEntity.ok(boardResDto);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteBoard(@PathVariable Long id, @RequestParam String email) {
        boolean isSuccess = boardService.deleteBoard(id, email);
        return ResponseEntity.ok(isSuccess);
    }

    // 게시글 페이징 카운트
    @GetMapping("/count")
    public ResponseEntity<Integer> boardPageCnt(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        Integer pageCnt = boardService.getBoardPageCount(page, size);
        return ResponseEntity.ok(pageCnt);
    }

    // 게시글 페이징 조회
    @GetMapping("/list/page")
    public ResponseEntity<List<BoardResDto>> boardPageList(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        List<BoardResDto> list = boardService.pagingBoardList(page, size);
        return ResponseEntity.ok(list);
    }

    // 게시글 제목 검색
    @GetMapping("/search-title")
    public ResponseEntity<List<BoardResDto>> boardSearchTitle(@RequestParam String keyword) {
        List<BoardResDto> list = boardService.searchBoard(keyword);
        return ResponseEntity.ok(list);
    }

    // 게시글 제목과 내용 검색
    @GetMapping("/search-title-content")
    public ResponseEntity<List<BoardResDto>> boardSearchTitleOrContent(@RequestParam String title,
                                                                       @RequestParam String content) {
        List<BoardResDto> list = boardService.searchTitleOrContentBoard(title, content);
        return ResponseEntity.ok(list);
    }
    // 댓글 추가
    @PostMapping("/{boardId}/comments")
    public ResponseEntity<Boolean> addComment(@RequestBody CommentReqDto commentReqDto) {
        boolean isSuccess = boardService.addComment(commentReqDto);
        return ResponseEntity.ok(isSuccess);
    }

    // 댓글 제거
    @DeleteMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<Boolean> removeComment(@PathVariable("boardId") Long boardId, @PathVariable Long commentId) {
        boolean isSuccess = boardService.removeComment(boardId, commentId);
        return ResponseEntity.ok(isSuccess);
    }
}
