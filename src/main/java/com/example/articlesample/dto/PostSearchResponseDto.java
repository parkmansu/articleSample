package com.example.articlesample.dto;

import com.example.articlesample.model.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostSearchResponseDto {

    private Long postId;
    private String title;
    private String contents;


    @Builder
    public PostSearchResponseDto(Long id, String title, String contents) {
        this.postId = id;
        this.title = title;
        this.contents = contents;

    }

    public Post toEntity() {
        Post build = Post.builder()
                .id(postId)
                .title(title)
                .contents(contents)
                .build();
        return build;

    }

}
