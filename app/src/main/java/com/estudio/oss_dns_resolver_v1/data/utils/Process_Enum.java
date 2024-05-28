package com.estudio.oss_dns_resolver_v1.data.utils;

import kotlinx.serialization.Serializable;

@Serializable
public enum Process_Enum {
    OSS_PROCESS("oss"),
    YUMING_PROCESS("yuming");
    final String name;
    Process_Enum(String name){this.name = name; }
}
