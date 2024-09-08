package com.imjustdoom.minecrashservice.dto.in;

import java.util.List;
import java.util.Map;

public record ModifyKnownErrorDto(int id, String error, List<String> matches, Map<String, String> arguments, String solution) {
}
