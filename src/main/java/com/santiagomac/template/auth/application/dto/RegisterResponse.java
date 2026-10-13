package com.santiagomac.template.auth.application.dto;

import java.util.UUID;

public record RegisterResponse(UUID id, String email) {
}
