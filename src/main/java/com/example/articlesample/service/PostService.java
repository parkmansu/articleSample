package com.example.articlesample.service;

import com.example.articlesample.dto.*;
import com.example.articlesample.model.Post;
import com.example.articlesample.model.PostLock;
import com.example.articlesample.model.Users;
import com.example.articlesample.repository.PostLockRepository;
import com.example.articlesample.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostLockRepository postLockRepository;

    //게사판 작성
    public void createPost(PostRequestDto requestDto, Users users) {

        // 단어
        String title = requestDto.getTitle();
        // 내용
        String contents = requestDto.getContents();

        //post 저장
        Post post = new Post(title, contents, users);
        postRepository.save(post);
    }

    // 게시판 수정
    @Transactional
    public void updatePost(Long postId, PostRequestDto requestDto, Users users) {

        // 해당 게시판 체크(게시판Id, 게시판 작성자)
        Post post = checkPostAndWriter(postId, users);

        //해당 게시판 유효성 검사
        checkPost(requestDto);

        //post update
        post.update(requestDto);

    }

    // 게시판 삭제
    public void deletePost(Long postId, Users users) {
        checkPostAndWriter(postId, users);
        postRepository.deleteById(postId);
    }

    //게시판 작성자 검사
    public Post checkPostAndWriter(Long postId, Users users){

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시판 Id에 해당하는 글이 없습니다.")
        );

        Long postUserId = post.getUsers().getId();
        if(!users.getId().equals(postUserId)){
            throw new IllegalArgumentException("작성자가 아니므로 게시판 글 수정이 불가합니다.");
        }
        return post;
    }

    //게시판 유효성 검사
    public void checkPost(PostRequestDto requestDto) {
        if (requestDto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("제목입력은 필수값입니다.");
        } else if (requestDto.getContents().isEmpty()) {
            throw new IllegalArgumentException("내용입력은 필수값입니다.");
        }
    }

    // 최신순으로 게시판 전체 리스트 페이지 조회
    public List<PostListResponseDto> findListPosts(int page, Users users) {
        List<PostListResponseDto> postListResponseDtos = new ArrayList<>();

        Pageable pageable = PageRequest.of(page -1, 5);
        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        for (Post post : posts) {
            PostListResponseDto postListResponseDto = new PostListResponseDto(post);
            postListResponseDto.setNickname(post.getUsers().getNickname());
            postListResponseDto.setTitle(post.getTitle());
            postListResponseDto.setContents(post.getContents());
            postListResponseDto.setPostId(post.getId());
            PostLock savedPostLock = postLockRepository.findByPost(post);
            if (savedPostLock != null) {
                postListResponseDto.setLockStatus(true);
            } else {
                postListResponseDto.setLockStatus(false);
            }
            postListResponseDtos.add(postListResponseDto);

        }


        return postListResponseDtos;


    }

    // 게시판 상세보기
    public PostDetailResponseDto findDetailPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시판 Id에 해당하는 글이 없습니다.")
        );

        PostDetailResponseDto postDetailResponseDto = new PostDetailResponseDto(post);
        postDetailResponseDto.setTitle(post.getTitle());
        postDetailResponseDto.setContents(post.getContents());
        postDetailResponseDto.setNickname(post.getUsers().getNickname());
        postDetailResponseDto.setModifiedAt(post.getModifiedAt());

        return postDetailResponseDto;
    }

    //검색
    public PostSearchDto searchPosts(String title, String contents) {

        List<Post> posts = postRepository.findByTitleContainingOrContentsContaining(title, contents);
        List<PostSearchResponseDto> searchList = new ArrayList<>();
        PostSearchDto postSearchList = new PostSearchDto();
        if (posts.isEmpty())
            return postSearchList;

        for (Post post : posts) {
            searchList.add(this.convertEntityToDto(post));
        }
        Long size = (long) searchList.size();
        postSearchList.setListCount(size);
        postSearchList.setPostSearchList(searchList);

        return postSearchList;
    }
    //검색
    private PostSearchResponseDto convertEntityToDto(Post post) {
        return PostSearchResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .build();
    }

    // 게시판 잠금
    public PostListResponseDto postLock(Long postId, Users users) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시판 Id에 해당하는 글이 없습니다."));

        Long postUserId = post.getUsers().getId();
        if(!users.getId().equals(postUserId)){
            throw new IllegalArgumentException("작성자만 잠금이 가능합니다.");
        }

        PostLock postLock = postLockRepository.findByUsersAndPost(users, post);
        boolean lockStatus;

        if (postLock != null) {
            postLockRepository.deleteById(postLock.getId());
            lockStatus = false;
        } else {
            PostLock lock = new PostLock(users, post);
            postLockRepository.save(lock);
            lockStatus = true;
        }
        return new PostListResponseDto(post, lockStatus);
    }

}
