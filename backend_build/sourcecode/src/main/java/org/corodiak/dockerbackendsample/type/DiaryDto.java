package org.corodiak.dockerbackendsample.type;

import lombok.*;

import java.time.LocalDateTime;

public class DiaryDto {

    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    @Getter
    public static class Request {
        private String title;
        private String writer;

        private String password;

        private String status;
        private String content;
    }

    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    @Getter
    public static class Response {
        private Long seq;
        private String title;

        private String writer;

        private String password;

        private String status;
        private String content;
        private LocalDateTime createDate;



        public Response(Diary diary) {
            this.seq = diary.getSeq();
            this.title = diary.getTitle();
            this.writer = diary.getWriter();
            this.password = diary.getPassword();
            this.status = diary.getStatus();
            this.content = diary.getContent();
            this.createDate = diary.getCreateDate();
        }
    }

}
