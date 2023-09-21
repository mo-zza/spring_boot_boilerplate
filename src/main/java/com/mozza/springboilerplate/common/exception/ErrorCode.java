package com.mozza.springboilerplate.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {
    BAD_REQUEST_ERROR(400, "G001", "Bad Request Exception"),

    REQUEST_BODY_MISSING_ERROR(400, "G002", "Required request body is missing"),

    INVALID_TYPE_VALUE(400, "G003", " Invalid Type Value"),

    MISSING_REQUEST_PARAMETER_ERROR(400, "G004", "Missing Servlet RequestParameter Exception"),

    IO_ERROR(400, "G005", "I/O Exception"),

    FORBIDDEN_ERROR(403, "G006", "Forbidden Exception"),

    NOT_FOUND_ERROR(404, "G007", "Not Found Exception"),

    NULL_POINT_ERROR(404, "G008", "Null Point Exception"),

    NOT_VALID_ERROR(404, "G009", "handle Validation Exception"),

    NOT_VALID_HEADER_ERROR(404, "G010", "Header에 데이터가 존재하지 않는 경우 "),

    UNAUTHORIZED_ERROR(401, "G011", "Unauthorized Exception"),

    TOKEN_EXPIRED(401, "G012", "Token Expired Exception"),

    INVALID_TOKEN(401, "G013", "Invalid Token Exception"),

    LARGE_FILE_ERROR(413, "G014", "Large File Exception"),

    INTERNAL_SERVER_ERROR(500, "G999", "Internal Server Error Exception"),

    INSERT_ERROR(200, "9999", "Insert Transaction Error Exception"),

    UPDATE_ERROR(200, "9999", "Update Transaction Error Exception"),

    DELETE_ERROR(200, "9999", "Delete Transaction Error Exception"),

    ;

    private final int status;

    private final String divisionCode;

    private final String message;

    ErrorCode(final int status, final String divisionCode, final String message) {
        this.status = status;
        this.divisionCode = divisionCode;
        this.message = message;
    }

    public static ErrorCode getCodeByHttpStatusCode(HttpStatusCode code) {
        if (code.equals(BAD_REQUEST)) {
            return BAD_REQUEST_ERROR;
        } else if (code.equals(UNAUTHORIZED)) {
            return UNAUTHORIZED_ERROR;
        } else if (code.equals(FORBIDDEN)) {
            return FORBIDDEN_ERROR;
        } else if (code.equals(NOT_FOUND)) {
            return NOT_FOUND_ERROR;
        } else if (code.equals(ErrorCode.INTERNAL_SERVER_ERROR)) {
            return INTERNAL_SERVER_ERROR;
        }
        throw new IllegalArgumentException("Unexpected value: " + code);
    }
}
