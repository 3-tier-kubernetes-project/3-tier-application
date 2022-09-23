package org.corodiak.dockerbackendsample.type;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class ResponseModel {

	@Builder.Default
	private HttpStatus httpStatus = HttpStatus.OK;

	@Builder.Default
	private String message = "요청에 성공하였습니다.";

	@Builder.Default
	private Map<String, Object> data = new HashMap<>();

	public void addData(String name, Object data) {
		this.data.put(name, data);
	}
}