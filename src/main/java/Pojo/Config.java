package Pojo;

import java.util.List;

public class Config {

    private List<String> sourcePaths;
    private String resultPaths;
    private List<sourcePathsArray> sourcePathsArray;
    private List<Filters> filters;



    public List<String> getSourcePaths() {
        return sourcePaths;
    }

    public void setSourcePaths(List<String> sourcePaths) {
        this.sourcePaths = sourcePaths;
    }

    public String getResultPaths() {
        return resultPaths;
    }

    public void setResultPaths(String resultPaths) {
        this.resultPaths = resultPaths;
    }

    public List<Pojo.sourcePathsArray> getSourcePathsArray() {
        return sourcePathsArray;
    }

    public void setSourcePathsArray(List<Pojo.sourcePathsArray> sourcePathsArray) {
        this.sourcePathsArray = sourcePathsArray;
    }

    public List<Filters> getFilters() {
        return filters;
    }

    public void setFilters(List<Filters> filters) {
        this.filters = filters;
    }
}

