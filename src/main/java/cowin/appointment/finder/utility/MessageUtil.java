package cowin.appointment.finder.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.json.flattener.JsonFlattener;
import cowin.appointment.finder.response.Center;
import cowin.appointment.finder.response.Session;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Log4j
public class MessageUtil {

    @Value("#{${center.column.map}}")
    Map<String,String> centerColumnMap;

    @Value("#{${session.column.map}}")
    Map<String,String> sessionColumnMap;
    @Autowired
    ObjectMapper objectMapper;

    public String createMessageTable(List<Center> centers) {
        log.info(centerColumnMap);
        try {
            Map<String, List<String>> valueMap = createValueMap(centers);
            //String message = createTableMessage(valueMap);
            return createEmailMessage(valueMap);
        } catch (JsonProcessingException jsonProcessingException) {
            jsonProcessingException.printStackTrace();
        }
        return null;
    }

    private String createEmailMessage(Map<String,List<String>> valueMap){
        StringBuilder sb = new StringBuilder();
        sb.append(" Centers available are ").append(valueMap.get("name")).append(" for age greater than ").append(valueMap.get("min_age_limit")).
                append(" one date ").append(valueMap.get("date")).append(". Vaccine name is ").append(valueMap.get("vaccine")).append( " and fee type is ").append(valueMap.get("fee_type"));
        return sb.toString();
    }

    private Map<String,List<String>> createValueMap(List<Center> centers) throws JsonProcessingException {
        Map<String,List<String>> valueTable = new HashMap<>();
        for (Center center : centers) {
            Map<String, Object> centerAsMap = JsonFlattener.flattenAsMap(objectMapper.writeValueAsString(center));
            for (String column : centerColumnMap.keySet()){
                if(centerAsMap.containsKey(column) && ( centerAsMap.get(column) instanceof String || centerAsMap.get(column) instanceof BigDecimal)){
                    if(valueTable.containsKey(column)){
                        valueTable.get(column).add(centerAsMap.get(column).toString());
                    }else {
                        List<String> list = new ArrayList<>();
                        list.add(centerAsMap.get(column).toString());
                        valueTable.put(column,list);
                    }
                }
            }
            for (Session session : center.getSessions()){
                Map<String, Object> sessionAsMap = JsonFlattener.flattenAsMap(objectMapper.writeValueAsString(session));
                for (String column : sessionColumnMap.keySet()){
                    if(sessionAsMap.containsKey(column) && ( sessionAsMap.get(column) instanceof String || sessionAsMap.get(column) instanceof BigDecimal)){
                        if(valueTable.containsKey(column)){
                            valueTable.get(column).add(sessionAsMap.get(column).toString());
                        }else {
                            List<String> list = new ArrayList<>();
                            list.add(sessionAsMap.get(column).toString());
                            valueTable.put(column,list);
                        }
                    }
                }
            }
        }
        return valueTable;
    }

    private String createTableMessage(Map<String, List<String>> valueMap){
        TreeMap<String,String> columnTitleMap = new TreeMap<>();
        columnTitleMap.putAll(sessionColumnMap);
        columnTitleMap.putAll(centerColumnMap);
        Map<String,Integer> maxSize = new HashMap<>();
        for(Map.Entry<String,String> entry : columnTitleMap.entrySet()){
            maxSize.put(entry.getKey(),entry.getValue().length());
        }
        int rowSize = 0;
        for(Map.Entry<String,List<String>> entry : valueMap.entrySet()){
            for(String value : entry.getValue()){
                if(maxSize.get(entry.getKey())<value.length()){
                    maxSize.put(entry.getKey(),value.length());
                }
            }
            rowSize = entry.getValue().size();
        }
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,Integer> entry : maxSize.entrySet()){
            sb.append("__");
            for(int i=0;i<entry.getValue();i++){
                sb.append("_");
            }
            sb.append("_");
        }
        sb.append("_\n").append("|");
        for(Map.Entry<String,String> entry : columnTitleMap.entrySet()){
            sb.append(" ").append(entry.getValue());
            for (int i=entry.getValue().length(); i< maxSize.get(entry.getKey());i++){
                sb.append(" ");
            }
            sb.append(" |");
        }
        printNewRowWithPipes(sb,maxSize,columnTitleMap);
        for(int i=0;i<rowSize;i++){
            sb.append("|");
            for(Map.Entry<String,String> entry : columnTitleMap.entrySet()){
                sb.append(" ").append(valueMap.get(entry.getKey()).get(i));
                for(int j= valueMap.get(entry.getKey()).get(i).length();j<maxSize.get(entry.getKey());j++){
                    sb.append(" ");
                }
                sb.append(" |");
            }
            printNewRowWithPipes(sb,maxSize,columnTitleMap);
        }
        log.debug(sb);
        return sb.toString();
    }

    private void printNewRowWithPipes(StringBuilder sb, Map<String,Integer> maxSize,TreeMap<String,String> columnTitleMap){
        sb.append("\n").append("|");
        for(Map.Entry<String,String> entry : columnTitleMap.entrySet()){
            sb.append("_");
            for(int i=0;i<maxSize.get(entry.getKey());i++){
                sb.append("_");
            }
            sb.append("_|");
        }
        sb.append("\n");
    }
}
