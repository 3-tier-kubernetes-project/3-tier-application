package org.corodiak.dockerbackendsample.type;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@AllArgsConstructor
@Builder
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_seq")
    private Long seq;

    private String title;

    private String writer;

    private String password;

    private String status;

    private String content;


    @UpdateTimestamp
    private LocalDateTime createDate;

    public void updateDiary(DiaryDto.Request diaryDto) {
        this.title = diaryDto.getTitle();
        this.writer = diaryDto.getWriter();
        this.password = diaryDto.getPassword();
        this.status = diaryDto.getStatus();
        this.content = diaryDto.getContent();
    }

}
