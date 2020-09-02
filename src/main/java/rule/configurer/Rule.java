package rule.configurer;

import lombok.Data;

@Data
public class Rule {

    private String id;
    private Integer priority;
    private String script;
}
