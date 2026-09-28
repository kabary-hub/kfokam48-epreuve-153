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
        return ResponseEntity.badRequest().body(Map.of(
                "code", "VALIDATION_ERROR",
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
