package com.acme.webserviceserentcar.client.resource;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
public class CommentResource {
    private Long id;
    private Long clientId;
    private Long clientCommentId;
    private String date;
    private int stars;
    private String comment;
}
