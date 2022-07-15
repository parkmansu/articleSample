package com.example.articlesample.Controller;

import com.example.articlesample.dto.PostDetailResponseDto;
import com.example.articlesample.dto.PostListResponseDto;
import com.example.articlesample.dto.PostRequestDto;
import com.example.articlesample.dto.PostSearchDto;
import com.example.articlesample.security.UserDetailsImpl;
import com.example.articlesample.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //게시판 등록
    @PostMapping("/api/post")
    public ResponseEntity<String> createPost(
            @RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.createPost(requestDto, userDetails.getUser());
        return ResponseEntity.ok()
                .body("단어장 등록완료.");
    }

    //게시판 수정
    @PutMapping("/api/post/{postId}/update")
    public ResponseEntity<String> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.updatePost(postId, requestDto, userDetails.getUser());
        return ResponseEntity.ok()
                .body("단어장 수정완료.");
    }

    //게시판 삭제
    @DeleteMapping("/api/post/{postId}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(postId, userDetails.getUser());
        return ResponseEntity.ok()
                .body("단어장 삭제완료");
    }

    // 게시판 리스트 전체 조회
    @GetMapping("/api/post/list")
    public ResponseEntity<List<PostListResponseDto>> getListPosts(
            @RequestParam(value = "page") int page,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        List<PostListResponseDto> postListResponseDtos = postService.findListPosts(page, userDetails.getUser());

        return ResponseEntity.ok()
                .body(postListResponseDtos);
    }

    //게시판 상세조회
    @GetMapping("/api/post/{postId}/detail")
    public ResponseEntity<PostDetailResponseDto> findDetailPost(
            @PathVariable Long postId){
        PostDetailResponseDto postDetailResponseDto = postService.findDetailPost(postId);
        return ResponseEntity.ok()
                .body(postDetailResponseDto);
    }

    // 검색
    @GetMapping("/api/post/search")
    public ResponseEntity<PostSearchDto> search(
            @RequestParam(value = "keyword") String keyword) {
        PostSearchDto searchList = postService.searchPosts(keyword,keyword);
        return ResponseEntity.ok()
                .body(searchList);
    }

    //게시판 잠금
    @PostMapping("/api/post/{postId}/lock")
    public ResponseEntity<PostListResponseDto> PostLock(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostListResponseDto postListResponseDto = postService.postLock(postId, userDetails.getUser());
        return ResponseEntity.ok()
                .body(postListResponseDto);
    }


}
