package com.example.project1.config;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRField;

import java.util.List;
import java.util.Map;
public class CustomDataSource implements JRDataSource {
    private final List<Map<String, Object>> data;
    private int index = -1;

    public CustomDataSource(List<Map<String, Object>> data) {
        this.data = data;
    }

    @Override
    public boolean next() {
        return ++index < data.size();
    }

    @Override
    public Object getFieldValue(JRField jrField) {
        return data.get(index).get(jrField.getName());
    }
}
