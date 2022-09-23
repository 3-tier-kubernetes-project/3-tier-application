package org.corodiak.dockerbackendsample.type;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DiaryDto {

	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Builder
	@Getter
	public static class Request {
		private String title;
		private String content;
	}

	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Builder
	@Getter
	public static class Response {
		private Long seq;
		private String title;
		private String content;
		private LocalDateTime createDate;

		public Response(Diary diary) {
			this.seq = diary.getSeq();
			this.title = diary.getTitle();
			this.content = diary.getContent();
			this.createDate = diary.getCreateDate();
		}
	}

}
