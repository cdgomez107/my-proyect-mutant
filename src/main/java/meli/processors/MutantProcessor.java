package meli.processors;

import meli.dbo.DatabaseUtils;
import org.apache.camel.Exchange;
import org.apache.camel.util.json.JsonObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

@ApplicationScoped
public class MutantProcessor {

    @Inject
    DatabaseUtils databaseUtils;

    public Exchange validateMutant(Exchange exchange) throws SQLException {
        LinkedHashMap object = (LinkedHashMap) exchange.getIn().getBody();
        String[] dna = ((ArrayList<String>) object.get("dna")).toArray(new String[0]);
        JsonObject response = new JsonObject();
        if (isDnaValid(dna)){
            Boolean isMutant = isMutant(dna);
            if (isMutant){
                exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE,200);
                response.put("response", "Is mutant");
            }else{
                exchange.getIn().setHeader(exchange.HTTP_RESPONSE_CODE, 403);
                response.put("response", "Is not mutant");
            }
            databaseUtils.saveInformation(dna, isMutant);
        }else{
            exchange.getIn().setHeader(exchange.HTTP_RESPONSE_CODE, 400);
            response.put("response", "Dna invalid");
        }
        exchange.getIn().setBody(response);
        return exchange;
    }

    private static Boolean isMutant(String[] dna) {
        int sequenceCount = 0;
        sequenceCount += countHorizontal(dna);
        sequenceCount += countVertical(dna);
        sequenceCount += countObliqueUpToDown(dna);
        sequenceCount += countObliqueDownToUp(dna);
        return sequenceCount > 1;
    }

    private static Boolean isDnaValid(String[] dna) {
        String[] possibleValues = {"A", "T", "C", "G"};
        for (int i = 0; i < dna.length; i++) {
            for (int j = 0; j < dna[i].length(); j++) {
                if (!Arrays.asList(possibleValues).contains(String.valueOf(dna[i].charAt(j)))) return false;
                if (dna[0].length() != dna[i].length()) return false;
            }
        }
        return true;
    }

    private static int countHorizontal(String[] dna) {
        int sequenceCount = 0;
        for (int i = 0; i < dna.length; i++) {
            for (int j = 0; j < dna[i].length() - 3; j++) {
                int countEquals = 0;
                for (int k = 1; k < 4; k++) {
                    if (dna[i].charAt(j) == dna[i].charAt(j + k)) {
                        countEquals += 1;
                    } else {
                        k = 4;
                    }
                }
                if (countEquals >= 3) sequenceCount += 1;
            }
        }
        return sequenceCount;
    }

    private static int countVertical(String[] dna) {
        int sequenceCount = 0;
        for (int i = 0; i < dna.length - 3; i++) {
            for (int j = 0; j < dna[i].length(); j++) {
                int countEquals = 0;
                for (int k = 1; k < 4; k++) {
                    if (dna[i].charAt(j) == dna[i + k].charAt(j)) {
                        countEquals += 1;
                    } else {
                        k = 4;
                    }
                }
                if (countEquals >= 3) sequenceCount += 1;
            }

        }
        return sequenceCount;
    }

    private static int countObliqueUpToDown(String[] dna) {
        int sequenceCount = 0;
        for (int i = 0; i < dna.length - 3; i++) {
            for (int j = 0; j < dna[i].length() -3; j++) {
                int countEquals = 0;
                for (int k = 1; k < 4; k++) {
                    if (dna[i].charAt(j) == dna[i + k].charAt(j + k)) {
                        countEquals += 1;
                    } else {
                        k = 4;
                    }
                }
                if (countEquals >= 3) sequenceCount += 1;
            }

        }
        return sequenceCount;
    }

    private static int countObliqueDownToUp(String[] dna) {
        int sequenceCount = 0;
        for (int i = dna.length - 1; i > 2; i--) {
            for (int j = 0; j < dna[i].length() - 3; j++) {
                int countEquals = 0;
                for (int k = 1; k < 4; k++) {
                    if (dna[i].charAt(j) == dna[i - k].charAt(j + k)) {
                        countEquals += 1;
                    } else {
                        k = 4;
                    }
                }
                if (countEquals >= 3) sequenceCount += 1;
            }
        }
        return sequenceCount;
    }
}

