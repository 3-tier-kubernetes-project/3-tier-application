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

        public void encryptPassword(String BCryptpassword) {
            this.password = BCryptpassword;
        }

        public void replaceToBr(){
            this.content = this.content.replace("\r\n","<br>");
        }
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

        public void replaceToNR(){
            this.content = this.content.replace("<br>","\r\n");
        }

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

    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class password{
        String password;
    }

}
