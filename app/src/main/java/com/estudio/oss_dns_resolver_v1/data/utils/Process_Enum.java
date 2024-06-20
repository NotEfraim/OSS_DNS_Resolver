package com.estudio.oss_dns_resolver_v1.data.utils;


import java.io.Serializable;

public enum Process_Enum implements Serializable {
    OSS_PROCESS("oss"),
    YUMING_PROCESS("yuming");
    final String name;
    Process_Enum(String name){this.name = name; }
}
