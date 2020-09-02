package rule.configurer.support;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;
import rule.configurer.Rule;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class RuleUtils {

    public static List<Rule> getRuleList() {
        var pathList = List.of("/rules/hello.js", "/rules/hello-world.js");

        var ruleList = new ArrayList<Rule>();
        for (int i = 0; i < pathList.size(); i++) {
            var path = pathList.get(i);
            var script = getFileAsStringFromClassPath(path);
            var rule = new Rule();
            rule.setId(path);
            rule.setPriority(i + 1);
            rule.setScript(script);
            ruleList.add(rule);
        }
        return ruleList;
    }

    @SneakyThrows
    private static String getFileAsStringFromClassPath(String path) {
        var resource = new ClassPathResource(path);
        Assert.state(resource.exists(), "resource not found");
        @Cleanup var is = resource.getInputStream();
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }
}
