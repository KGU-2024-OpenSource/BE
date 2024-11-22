package com.be_provocation.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailTask {

    private final String to;
    private final String subject;
    private final String content;
//    private final List<File> files;
//    private final EmailTemplateType templateType;
}

