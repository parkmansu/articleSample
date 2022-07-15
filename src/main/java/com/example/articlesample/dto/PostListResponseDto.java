package com.example.articlesample.dto;

import com.example.articlesample.model.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class PostListResponseDto {

    private boolean lockStatus;
    private Long postId;
    private String title;
    private String contents;
    private String nickname;

    public PostListResponseDto(Post post) {
        this.title = post.getTitle();
        this.contents = post.getContents();
    }

    public PostListResponseDto(Post post, boolean lockStatus) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.lockStatus = lockStatus;
        this.nickname = post.getUsers().getNickname();
    }
}
