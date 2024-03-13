package byteblaze.sms.service;

import byteblaze.sms.model.Nutzer;
import byteblaze.sms.repository.NutzerRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@Data
public class LoginService {

    @Autowired
    private NutzerRepository nutzerRepository;

    private Map<String, SessionInfo> activeSessions = new HashMap<>();
    private static final Duration SESSION_DURATION = Duration.ofHours(1); // Beispiel: Sitzungsdauer von 1 Stunde

    public boolean authenticate(String nutzername, String password) {
        Nutzer nutzer = nutzerRepository.findByNutzernameAndPassword(nutzername, password);
        return nutzer != null;
    }

    public String createSession(Long nutzerId) {
        String sessionId = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plus(SESSION_DURATION);
        activeSessions.put(sessionId, new SessionInfo(nutzerId, expirationTime));
        return sessionId;
    }

    public boolean isValidSession(String sessionId) {
        if (!activeSessions.containsKey(sessionId)) {
            return false;
        }

        SessionInfo sessionInfo = activeSessions.get(sessionId);
        return !sessionInfo.isExpired();
    }

    public Long getUserIdFromSession(String sessionId) {
        if (isValidSession(sessionId)) {
            return activeSessions.get(sessionId).getUserId();
        }
        return null;
    }

    public Long getUserIdByUsername(String nutzername) {
        Nutzer nutzer = nutzerRepository.findByNutzername(nutzername);
        if (nutzer != null) {
            return nutzer.getNutzerId();
        }
        return null;
    }

    public void invalidateSession(String sessionId) {
        activeSessions.remove(sessionId);
    }

    public void logout(String sessionId) {
        invalidateSession(sessionId);
    }

    public boolean isSessionExpired(String sessionId) {
        if (!isValidSession(sessionId)) {
            return true;
        }
        SessionInfo sessionInfo = activeSessions.get(sessionId);
        return sessionInfo.isExpired();
    }

    // Innere statische Klasse zur Speicherung von Sitzungsinformationen
    @Data
    private static class SessionInfo {
        private Long userId;
        private LocalDateTime expirationTime;

        public SessionInfo(Long userId, LocalDateTime expirationTime) {
            this.userId = userId;
            this.expirationTime = expirationTime;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expirationTime);
        }
    }
}
