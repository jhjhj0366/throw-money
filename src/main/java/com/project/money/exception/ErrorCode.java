package com.project.money.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    INVALID_INPUT_VALUE(400, "유효하지 않는 입력값 입니다."),
    INVALID_TOKEN_VALUE(401, "유효하지 않는 뿌리기 토큰값 입니다."),
    HANDLE_ACCESS_DENIED(403, "접근이 허용되지 않은 사용자 입니다."),
    METHOD_NOT_ALLOWED(405, "지원하지 않는 요청입니다."),
    INTERNAL_SERVER_ERROR(500, "서버에서 에러가 발생했습니다."),
    ;

    private int status;
    private final String message;

}
