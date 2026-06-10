package com.gaisoft.kb.controller;

import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides ragflow model lists by querying the ragflow MySQL database directly.
 * This bypasses the ragflow internal API (/v1/llm/my_llms) which requires
 * session cookie auth that may be unreliable.
 */
@RestController
@RequestMapping(value={"/ragflow/model"})
public class RagflowModelController extends BaseController {

    private final JdbcTemplate ragflowJdbc;

    @Autowired
    public RagflowModelController(DataSource dataSource) {
        // Use the default (equipment_iqas) datasource.
        // We'll connect to ragflow's MySQL via the ragflow-mysql host.
        this.ragflowJdbc = null; // Will be created on demand
    }

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate getRagflowJdbc() {
        // Query ragflow DB through the shared MySQL instance
        // ragflow-mysql:3306 with rag_flow database
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        return jdbc;
    }

    /**
     * List all configured models grouped by factory and type.
     * Queries ragflow's tenant_llm table directly.
     */
    @GetMapping(value={"/list"})
    public AjaxResult listModels() {
        try {
            JdbcTemplate jdbc = getRagflowJdbc();
            // Query all distinct models from ragflow's tenant_llm table
            String sql = "SELECT llm_factory, model_type, llm_name FROM rag_flow.tenant_llm " +
                         "GROUP BY llm_factory, model_type, llm_name ORDER BY model_type, llm_factory, llm_name";
            List<Map<String, Object>> rows = jdbc.queryForList(sql);

            // Group by factory like ragflow's /v1/llm/my_llms does
            Map<String, Map<String, Object>> result = new HashMap<>();
            for (Map<String, Object> row : rows) {
                String factory = (String) row.get("llm_factory");
                String modelType = (String) row.get("model_type");
                String modelName = (String) row.get("llm_name");

                if (!result.containsKey(factory)) {
                    Map<String, Object> factoryData = new HashMap<>();
                    factoryData.put("llm", new ArrayList<Map<String, String>>());
                    result.put(factory, factoryData);
                }

                Map<String, String> modelInfo = new HashMap<>();
                modelInfo.put("name", modelName);
                modelInfo.put("type", modelType);
                @SuppressWarnings("unchecked")
                List<Map<String, String>> llms = (List<Map<String, String>>) result.get(factory).get("llm");
                llms.add(modelInfo);
            }

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("查询模型列表失败: " + e.getMessage());
        }
    }
}
