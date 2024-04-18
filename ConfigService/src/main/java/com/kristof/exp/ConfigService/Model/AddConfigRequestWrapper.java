package com.kristof.exp.ConfigService.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddConfigRequestWrapper {
    private Long applicationId;
    private Long environmentId;
    private String key;
    private String value;
}
