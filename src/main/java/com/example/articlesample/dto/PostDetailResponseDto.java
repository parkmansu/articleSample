package com.example.articlesample.dto;

import com.example.articlesample.model.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor

public class PostDetailResponseDto {
    private String title;
    private String contents;
    private String nickname;
    private LocalDateTime modifiedAt;

    public PostDetailResponseDto(Post post) {
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.modifiedAt = post.getModifiedAt();
    }
}
