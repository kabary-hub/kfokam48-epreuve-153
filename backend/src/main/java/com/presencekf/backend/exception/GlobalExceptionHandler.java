package com.presencekf.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Erreurs de validation des DTO (@Valid) → 400 VALIDATION_ERROR
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(e -> e.getField() + " : " + e.getDefaultMessage())
                .orElse("Requête invalide");
        boolean invalidReviewNote = ex.getBindingResult().getFieldErrors().stream()
                .anyMatch(error -> "note".equals(error.getField()));
        String code = invalidReviewNote ? "NOTE_INVALIDE" : "VALIDATION_ERROR";
        return ResponseEntity.badRequest().body(Map.of(
                "code", code,
                "message", message
        ));
    }

    /**
     * Paramètre de requête manquant → 400 PARAMETRE_MANQUANT
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingParam(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "code", "PARAMETRE_MANQUANT",
                "message", "Paramètre manquant : " + ex.getParameterName()
        ));
    }

    /**
     * Corps JSON illisible → 400 CORPS_INVALIDE
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleUnreadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "code", "CORPS_INVALIDE",
                "message", "Le corps de la requête est invalide."
        ));
    }

    /**
     * Code de session inconnu → 400 CODE_INCONNU (M2)
     */
    @ExceptionHandler(CodeInconnuException.class)
    public ResponseEntity<Map<String, String>> handleCodeInconnu(CodeInconnuException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "code", "CODE_INCONNU",
                "message", ex.getMessage()
        ));
    }

    /**
     * Code expiré → 410 CODE_EXPIRE (M2, RG1)
     */
    @ExceptionHandler(CodeExpireException.class)
    public ResponseEntity<Map<String, String>> handleCodeExpire(CodeExpireException ex) {
        return ResponseEntity.status(HttpStatus.GONE).body(Map.of(
                "code", "CODE_EXPIRE",
                "message", ex.getMessage()
        ));
    }

    /**
     * Session inexistante pour l'ajout manuel → 404 SESSION_INCONNUE (M3).
     */
    @ExceptionHandler(SessionInconnueException.class)
    public ResponseEntity<Map<String, String>> handleSessionInconnue(SessionInconnueException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "code", "SESSION_INCONNUE",
                "message", ex.getMessage()
        ));
    }

    /**
     * Étudiant déjà présent → 409 DEJA_PRESENT (M2, RG15)
     */
    @ExceptionHandler(DejaPresentException.class)
    public ResponseEntity<Map<String, String>> handleDejaPresent(DejaPresentException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "code", "DEJA_PRESENT",
                "message", ex.getMessage()
        ));
    }

    /**
     * Trop de tentatives erronées → 429 Too Many Requests (RG3).
     */
    @ExceptionHandler(TropTentativesException.class)
    public ResponseEntity<Map<String, String>> handleTropTentatives(TropTentativesException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Map.of(
                "code", "TROP_TENTATIVES",
                "message", ex.getMessage()
        ));
    }

    /**
     * Lien d'exercice invalide → 400 LIEN_INVALIDE (M4).
     */
    @ExceptionHandler(LienInvalideException.class)
    public ResponseEntity<Map<String, String>> handleLienInvalide(LienInvalideException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "code", "LIEN_INVALIDE",
                "message", ex.getMessage()
        ));
    }

    /**
     * Exercice déjà déposé → 409 EXERCICE_DEJA_DEPOSE (M4, RG16).
     */
    @ExceptionHandler(ExerciceDejaDeposeException.class)
    public ResponseEntity<Map<String, String>> handleExerciceDejaDepose(ExerciceDejaDeposeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "code", "EXERCICE_DEJA_DEPOSE",
                "message", ex.getMessage()
        ));
    }

    /** Note invalide → 400 NOTE_INVALIDE (M6, RG8). */
    @ExceptionHandler(NoteInvalideException.class)
    public ResponseEntity<Map<String, String>> handleNoteInvalide(NoteInvalideException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "code", "NOTE_INVALIDE",
                "message", ex.getMessage()
        ));
    }

    /** Auto-relecture interdite → 403 AUTO_RELECTURE (M6, RG4). */
    @ExceptionHandler(AutoRelectureException.class)
    public ResponseEntity<Map<String, String>> handleAutoRelecture(AutoRelectureException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "code", "AUTO_RELECTURE",
                "message", ex.getMessage()
        ));
    }

    /** Relecture déjà rendue → 409 RELECTURE_DEJA_RENDUE (M6, RG9). */
    @ExceptionHandler(RelectureDejaRendueException.class)
    public ResponseEntity<Map<String, String>> handleRelectureDejaRendue(
            RelectureDejaRendueException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "code", "RELECTURE_DEJA_RENDUE",
                "message", ex.getMessage()
        ));
    }

    /** Promotion inexistante pour le tableau → 404 PROMOTION_INCONNUE (M7). */
    @ExceptionHandler(PromotionInconnueException.class)
    public ResponseEntity<Map<String, String>> handlePromotionInconnue(
            PromotionInconnueException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "code", "PROMOTION_INCONNUE",
                "message", ex.getMessage()
        ));
    }

    /**
     * Filet de sécurité — toute autre erreur non gérée → 500 ERREUR_INTERNE
     * Aucune stack trace n'est renvoyée au client (B4).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleUnexpected(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "code", "ERREUR_INTERNE",
                "message", "Une erreur interne est survenue."
        ));
    }
}
