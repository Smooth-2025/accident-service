package com.smooth.accident_service.global.auth;

import com.smooth.accident_service.global.exception.BusinessException;
import com.smooth.accident_service.global.exception.CommonErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Slf4j
public class AuthenticationUtils {

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_EMAIL_HEADER = "X-User-Email";
    private static final String USER_ROLE_HEADER = "X-User-Role";
    private static final String AUTHENTICATED_HEADER = "X-Authenticated";


    public static Long getCurrentUserId() {
        return getUserIdFromHeader();
    }

    public static Long getCurrentUserIdOrThrow() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(CommonErrorCode.UNAUTHORIZED, "인증되지 않은 사용자입니다.");
        }
        return userId;
    }

    public static String getCurrentUserEmail() {
        return getUserEmailFromHeader();
    }

    public static String getCurrentUserRole() {
        return getUserRoleFromHeader();
    }

    public static boolean isAdmin() {
        String role = getCurrentUserRole();
        boolean isAdminResult = "ADMIN".equals(role);
        log.info("isAdmin() 체크 - role: {}, isAdmin: {}", role, isAdminResult);
        return isAdminResult;
    }

    public static boolean isAuthenticated() {
        return getCurrentUserId() != null;
    }

    private static Long getUserIdFromHeader() {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            String userIdHeader = request.getHeader(USER_ID_HEADER);
            String authenticatedHeader = request.getHeader(AUTHENTICATED_HEADER);

            log.info("헤더 확인 - X-User-Id: {}, X-Authenticated: {}", userIdHeader, authenticatedHeader);

            if (StringUtils.hasText(userIdHeader) && "true".equals(authenticatedHeader)) {
                return Long.valueOf(userIdHeader);
            }
        } catch (Exception e) {
            log.debug("HTTP 헤더에서 사용자 ID 추출 실패: {}", e.getMessage());
        }
        return null;
    }

    private static String getUserEmailFromHeader() {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            String emailHeader = request.getHeader(USER_EMAIL_HEADER);
            String authenticatedHeader = request.getHeader(AUTHENTICATED_HEADER);

            if (StringUtils.hasText(emailHeader) && "true".equals(authenticatedHeader)) {
                return emailHeader;
            }
        } catch (Exception e) {
            log.debug("HTTP 헤더에서 사용자 이메일 추출 실패: {}", e.getMessage());
        }
        return null;
    }

    private static String getUserRoleFromHeader() {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            String roleHeader = request.getHeader(USER_ROLE_HEADER);
            String authenticatedHeader = request.getHeader(AUTHENTICATED_HEADER);

            log.info("헤더 확인 - X-User-Role: {}, X-Authenticated: {}", roleHeader, authenticatedHeader);

            if (StringUtils.hasText(roleHeader) && "true".equals(authenticatedHeader)) {
                log.info("관리자 권한 확인됨: {}", roleHeader);
                return roleHeader;
            }
        } catch (Exception e) {
            log.debug("HTTP 헤더에서 사용자 역할 추출 실패: {}", e.getMessage());
        }
        log.info("기본 사용자 권한으로 설정됨");
        return "USER";
    }
}