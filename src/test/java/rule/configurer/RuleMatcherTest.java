package rule.configurer;

import org.junit.jupiter.api.Test;
import rule.configurer.support.RuleUtils;
import rule.configurer.support.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RuleMatcherTest {

    @Test
    void testMatch() {
        var ruleList = RuleUtils.getRuleList();
        var ruleMatcher = new RuleMatcher<User, String>(ruleList);
        var ada = new User(1, "Ada");
        assertEquals("hello Ada", ruleMatcher.matchFirst(ada).getRight());
        var nobody = new User(0, "?");
        assertEquals("hello world", ruleMatcher.matchFirst(nobody).getRight());
        // match all
        var matchAll = ruleMatcher.matchAll(ada);
        assertEquals(2, matchAll.size());
    }
}
