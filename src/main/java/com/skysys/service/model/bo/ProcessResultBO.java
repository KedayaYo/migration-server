package com.skysys.service.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProcessResultBO<S, T> {
    private boolean success;
    private S source;
    private T target;
    private String errorMessage;
}