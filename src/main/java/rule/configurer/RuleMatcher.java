package rule.configurer;

import lombok.SneakyThrows;
import org.graalvm.collections.Pair;
import rule.configurer.support.Json;

import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RuleMatcher<T, R> {

    private static final String INPUT_BINDING = "input";

    private final List<Rule> ruleList = new ArrayList<>();

    public RuleMatcher(List<Rule> ruleList) {
        this.ruleList.addAll(ruleList);
        // 按优先级排序
        this.ruleList.sort(Comparator.comparingInt(Rule::getPriority));
    }

    public Pair<Rule, R> matchFirst(T input) {
        var matched = this.internalMatch(input, false);
        return matched.isEmpty() ? null : matched.get(0);
    }

    public List<Pair<Rule, R>> matchAll(T input) {
        return this.internalMatch(input, true);
    }

    @SneakyThrows
    private List<Pair<Rule, R>> internalMatch(T input, boolean all) {
        var manager = new ScriptEngineManager();
        var engine = manager.getEngineByName("graal.js");
        var bindings = engine.createBindings();
        bindings.put(INPUT_BINDING, Json.encode(input));
        var matchedList = new ArrayList<Pair<Rule, R>>();
        for (var rule : this.ruleList) {
            var result = engine.eval(rule.getScript(), bindings);
            if (result != null) {
                matchedList.add(Pair.create(rule, cast(result)));
            }
            // match first
            if (!all && !matchedList.isEmpty()) {
                return matchedList;
            }
        }
        return matchedList;
    }

    @SuppressWarnings("unchecked")
    private static <U> U cast(Object o) {
        return (U) o;
    }
}
