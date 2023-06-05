package com.tommazon.gatewayservice.util;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

public class ReceiptJSONTableConverter {
    public static String convertJSONToTable(String jsonArrayString) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonArrayString, JsonArray.class);

        List<String[]> rows = new ArrayList<>();
        String[] header = {"Name", "Quantity", "Price", "Deal", "Remark"};
        rows.add(header);

        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            String name = getStringValue(jsonObject, "Name");
            String remark = getStringValue(jsonObject, "Remark");
            String quantity = getStringValue(jsonObject, "Quantity");
            String price = getStringValue(jsonObject, "Price");
            String deal = getStringValue(jsonObject, "Deal");

            String[] row = {name, remark, quantity, price, deal};
            rows.add(row);
        }

        StringBuilder tableBuilder = new StringBuilder();
        tableBuilder.append(printRow(header));
        tableBuilder.append(printSeparator(header));

        for (int i = 1; i < rows.size(); i++) {
            tableBuilder.append(printRow(rows.get(i)));
        }

        return tableBuilder.toString();
    }

    private static String getStringValue(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        if (element != null && !element.isJsonNull()) {
            return element.getAsString();
        }
        return "";
    }

    private static String printRow(String[] row) {
        StringBuilder rowBuilder = new StringBuilder();
        rowBuilder.append("| ");
        for (String cell : row) {
            rowBuilder.append(cell).append(" | ");
        }
        rowBuilder.append("\n");
        return rowBuilder.toString();
    }

    private static String printSeparator(String[] header) {
        StringBuilder separatorBuilder = new StringBuilder();
        separatorBuilder.append("|");
        for (String columnName : header) {
            separatorBuilder.append(new String(new char[columnName.length() + 2]).replace('\0', '-')).append("|");
        }
        separatorBuilder.append("\n");
        return separatorBuilder.toString();
    }
}
