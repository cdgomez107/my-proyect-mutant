package meli.processors;

import meli.dbo.DatabaseUtils;
import org.apache.camel.Exchange;
import org.apache.camel.util.json.JsonObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.SQLException;
import java.util.HashMap;

@ApplicationScoped
public class StatProcessor {

    @Inject
    DatabaseUtils databaseUtils;

    public Exchange getStats(Exchange exchange) throws SQLException {
        HashMap<String, String> list = databaseUtils.readDatabase();
        JsonObject response = new JsonObject();
        int cantHumans = list.get("0") == null ? 0 : Integer.parseInt(list.get("0"));
        int cantMutants = list.get("1") == null ? 0 : Integer.parseInt(list.get("1"));
        int totalSamples = cantHumans + cantMutants;
        double ratioMutants = (double) cantMutants / totalSamples;
        JsonObject responseData = new JsonObject();
        responseData.put("count_mutant_dna", cantMutants);
        responseData.put("count_human_dna", cantHumans);
        responseData.put("ratio", ratioMutants);
        response.put("ADN", responseData);
        exchange.getIn().setBody(response);
        return exchange;
    }
}
