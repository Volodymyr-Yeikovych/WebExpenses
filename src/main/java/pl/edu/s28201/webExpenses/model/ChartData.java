package pl.edu.s28201.webExpenses.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChartData {

    private String chartName;
    private List<String> labels;
    private List<String> decimalValues;
}
