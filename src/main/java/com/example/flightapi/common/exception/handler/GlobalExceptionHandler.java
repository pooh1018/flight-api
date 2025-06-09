package com.example.flightapi.common.exception.handler;

import com.example.flightapi.common.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import com.example.flightapi.common.exception.SystemException;
import com.example.flightapi.common.exception.EntityExistException;
import com.example.flightapi.common.exception.EntityNotFoundException;
import com.example.flightapi.common.utils.ThrowableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageUtils messageUtils;

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiResult> handleException(Throwable e){
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResult.fail(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage()));
    }

    /**
     * BadCredentialsException
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResult> badCredentialsException(BadCredentialsException e){
        // 打印堆栈信息
        String message = messageUtils.getMessage("user.credentials.bad").equals(e.getMessage()) ? messageUtils.getMessage("user.password.error") : e.getMessage();
        log.error(message);
        return buildResponseEntity(ApiResult.failMessage(message));
    }

    /**
     * 处理自定义异常
     */
	@ExceptionHandler(value = SystemException.class)
	public ResponseEntity<ApiResult> SystemException(SystemException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiResult.fail(e.getCode(),e.getMessage()));
	}

    /**
     * 处理 EntityExist
     */
    @ExceptionHandler(value = EntityExistException.class)
    public ApiResult entityExistException(EntityExistException e) {
        // 打印堆栈信息
//        log.error(ThrowableUtil.getStackTrace(e));
//        return buildResponseEntity(ApiResult.fail(e.getMessage()));
        return ApiResult.failMessage(e.getMessage());
    }

    /**
     * 处理 EntityNotFound
     */
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ApiResult entityNotFoundException(EntityNotFoundException e) {
        // 打印堆栈信息
//        log.error(ThrowableUtil.getStackTrace(e));
//        return buildResponseEntity(ApiResult.fail(String.valueOf(HttpStatus.NOT_FOUND.value()), e.getMessage()));
        return ApiResult.failMessage(e.getMessage());
    }

    /**
     * 处理所有接口数据验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        String message = objectError.getDefaultMessage();
        if (objectError instanceof FieldError) {
            message = ((FieldError) objectError).getField() + ": " + message;
        }
        return buildResponseEntity(ApiResult.failMessage(message));
    }

    /**
     * 处理通用认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    public ApiResult handleAuthenticationException(AuthenticationException e) {
        // 打印堆栈信息
//        log.error(ThrowableUtil.getStackTrace(e));
        String message = messageUtils.getMessage("user.authentication.failed");
//        return buildResponseEntity(ApiResult.fail(String.valueOf(HttpStatus.UNAUTHORIZED.value()), message));
        return ApiResult.fail(String.valueOf(HttpStatus.UNAUTHORIZED.value()), message);
    }

    /**
     * 处理访问权限异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ApiResult handleAccessDeniedException(AccessDeniedException e) {
        // 打印堆栈信息
//        log.error(ThrowableUtil.getStackTrace(e));
        String message = messageUtils.getMessage("user.access.denied");
//        return buildResponseEntity(ApiResult.fail(String.valueOf(HttpStatus.FORBIDDEN.value()), message));
        return ApiResult.failMessage(message);
    }

    /**
     * 处理用户不存在异常
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ApiResult handleUsernameNotFoundException(UsernameNotFoundException e) {
        // 打印堆栈信息
//        log.error(ThrowableUtil.getStackTrace(e));
        String message = messageUtils.getMessage("user.not.found");
//        return buildResponseEntity(ApiResult.fail(String.valueOf(HttpStatus.UNAUTHORIZED.value()), message));
        return ApiResult.fail(String.valueOf(HttpStatus.UNAUTHORIZED.value()), message);
    }

    /**
     * 处理账户过期异常
     */
    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<ApiResult> handleAccountExpiredException(AccountExpiredException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        String message = messageUtils.getMessage("user.account.expired");
        return buildResponseEntity(ApiResult.fail(String.valueOf(HttpStatus.UNAUTHORIZED.value()), message));
    }

    /**
     * 处理账户锁定异常
     */
    @ExceptionHandler(LockedException.class)
    public ApiResult handleLockedException(LockedException e) {
        // 打印堆栈信息
//        log.error(ThrowableUtil.getStackTrace(e));
        String message = messageUtils.getMessage("user.account.locked");
//        return buildResponseEntity(ApiResult.fail(String.valueOf(HttpStatus.UNAUTHORIZED.value()), message));
        return ApiResult.failMessage(message);
    }

    /**
     * 处理账户禁用异常
     */
    @ExceptionHandler(DisabledException.class)
    public ApiResult handleDisabledException(DisabledException e) {
        // 打印堆栈信息
//        log.error(ThrowableUtil.getStackTrace(e));
        String message = messageUtils.getMessage("user.disabled");
//        return buildResponseEntity(ApiResult.fail(String.valueOf(HttpStatus.UNAUTHORIZED.value()), message));
        return ApiResult.failMessage(message);
    }

    /**
     * 统一返回
     */
    private ResponseEntity<ApiResult> buildResponseEntity(ApiResult apiResult) {
        return new ResponseEntity<>(apiResult, HttpStatus.valueOf(apiResult.getCode()));
    }
}
