package com.example.artifact_catalog;

import java.util.List;
import java.util.Objects;

public class Artifact {
    private String artifactId;
    private String artifactName;
    private String category;
    private String civilization;
    private String discoveryLocation;
    private String composition;
    private String discoveryDate;
    private String currentPlace;
    private Dimensions dimensions;
    private double weight;
    private List<String> tags;
    private String imagePath;


    public Artifact(String artifactId, String artifactName, String category, String civilization,
                    String discoveryLocation, String composition, String discoveryDate,
                    String currentPlace, Dimensions dimensions, double weight, List<String> tags,
                    String imagePath) {
        this.artifactId = artifactId;
        this.artifactName = artifactName;
        this.category = category;
        this.civilization = civilization;
        this.discoveryLocation = discoveryLocation;
        this.composition = composition;
        this.discoveryDate = discoveryDate;
        this.currentPlace = currentPlace;
        this.dimensions = dimensions;
        this.weight = weight;
        this.tags = tags;
        this.imagePath = imagePath;
    }


    public Artifact() {}


    public String getArtifactId() { return artifactId; }
    public void setArtifactId(String artifactId) { this.artifactId = artifactId; }

    public String getArtifactName() { return artifactName; }
    public void setArtifactName(String artifactName) { this.artifactName = artifactName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCivilization() { return civilization; }
    public void setCivilization(String civilization) { this.civilization = civilization; }

    public String getDiscoveryLocation() { return discoveryLocation; }
    public void setDiscoveryLocation(String discoveryLocation) { this.discoveryLocation = discoveryLocation; }

    public String getComposition() { return composition; }
    public void setComposition(String composition) { this.composition = composition; }

    public String getDiscoveryDate() { return discoveryDate; }
    public void setDiscoveryDate(String discoveryDate) { this.discoveryDate = discoveryDate; }

    public String getCurrentPlace() { return currentPlace; }
    public void setCurrentPlace(String currentPlace) { this.currentPlace = currentPlace; }

    public Dimensions getDimensions() { return dimensions; }
    public void setDimensions(Dimensions dimensions) { this.dimensions = dimensions; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artifact artifact = (Artifact) o;
        return Double.compare(artifact.weight, weight) == 0 &&
                Objects.equals(artifactId, artifact.artifactId) &&
                Objects.equals(artifactName, artifact.artifactName) &&
                Objects.equals(category, artifact.category) &&
                Objects.equals(civilization, artifact.civilization) &&
                Objects.equals(discoveryLocation, artifact.discoveryLocation) &&
                Objects.equals(composition, artifact.composition) &&
                Objects.equals(discoveryDate, artifact.discoveryDate) &&
                Objects.equals(currentPlace, artifact.currentPlace) &&
                Objects.equals(dimensions, artifact.dimensions) &&
                Objects.equals(tags, artifact.tags) &&
                Objects.equals(imagePath, artifact.imagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artifactId, artifactName, category, civilization, discoveryLocation,
                composition, discoveryDate, currentPlace, dimensions, weight, tags, imagePath);
    }

    @Override
    public String toString() {
        return "Artifact{" +
                "artifactId='" + artifactId + '\'' +
                ", artifactName='" + artifactName + '\'' +
                ", category='" + category + '\'' +
                ", civilization='" + civilization + '\'' +
                ", discoveryLocation='" + discoveryLocation + '\'' +
                ", composition='" + composition + '\'' +
                ", discoveryDate='" + discoveryDate + '\'' +
                ", currentPlace='" + currentPlace + '\'' +
                ", dimensions=" + dimensions +
                ", weight=" + weight +
                ", tags=" + tags +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
