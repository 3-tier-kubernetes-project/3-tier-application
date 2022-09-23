package org.corodiak.dockerbackendsample.type;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

	private String content;

	private LocalDateTime createDate;

}
