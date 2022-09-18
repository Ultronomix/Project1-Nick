package com.github.Nick.common;

import java.util.Objects;

public class ResourceCreationResponse {
    
    private String resourceId;

    public ResourceCreationResponse(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String toString() {
        return "ResourceCreationResponse {" +
        "resourceId = " + resourceId + 
        "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj == null || getClass() != obj.getClass()) return false;
        ResourceCreationResponse other = (ResourceCreationResponse) obj;
        return Objects.equals(resourceId, other.resourceId);
    }

}
