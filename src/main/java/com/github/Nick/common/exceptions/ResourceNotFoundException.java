package com.github.Nick.common.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException() {
        super("No resource found using the provided search parameters.");
    }
}
