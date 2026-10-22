package br.com.churchmanager.common.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.Map;

/**
 * Endpoint simples para o frontend verificar se a API está no ar.
 * GET /api/ping
 */
@RestController
public class PingController {

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of(
                "status", "ok",
                "service", "church-manager-api",
                "timestamp", OffsetDateTime.now().toString());
    }
}
