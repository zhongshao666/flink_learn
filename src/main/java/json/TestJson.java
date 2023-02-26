package json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class TestJson {
    public static void main(String[] args) {
        String s="{\n" +
                "    \"message\": \"success\",\n" +
                "    \"code\": 0,\n" +
                "    \"data\": {\n" +
                "        \"page\": 1,\n" +
                "        \"total_page\": 1,\n" +
                "        \"count\": 1,\n" +
                "        \"next\": null,\n" +
                "        \"previous\": null,\n" +
                "        \"items\": [\n" +
                "            {\n" +
                "                \"id\": 1,\n" +
                "                \"sn\": \"NO2019091610001755\",\n" +
                "                \"title_my\": \"a\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 2,\n" +

                "            },\n" +
                "            {\n" +
                "                \"id\": 3,\n" +
                "                \"sn\": \"NO2019091610001757\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"result\": true\n" +
                "}\n";
        BkTO bkTO = JSON.parseObject(s, BkTO.class);
        JSONObject jsonObject = JSON.parseObject(bkTO.getData());

        String items = jsonObject.getString("items");
        List<Pojo> pojos = JSON.parseArray(items, Pojo.class);
        for (Pojo p: pojos) {
            System.out.println(p.toString());
        }

        String s1 = JSON.toJSONString(items);
        System.out.println(s1);

//        JSONObject jsonObject1 = JSON.parseObject(s);
//        String items1 = jsonObject1.getString("data");
//        JSONObject jsonObject2 = JSON.parseObject(items1);
//        System.out.println(jsonObject2.getString("items"));


    }
}
