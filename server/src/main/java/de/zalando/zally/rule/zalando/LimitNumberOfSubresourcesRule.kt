package de.zalando.zally.rule.zalando

import com.typesafe.config.Config
import de.zalando.zally.rule.AbstractRule
import de.zalando.zally.rule.api.Check
import de.zalando.zally.rule.api.Severity
import de.zalando.zally.rule.api.Violation
import de.zalando.zally.util.PatternUtil
import io.swagger.models.Swagger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LimitNumberOfSubresourcesRule(@Autowired ruleSet: ZalandoRuleSet, @Autowired rulesConfig: Config) : AbstractRule(ruleSet) {
    override val title = "Limit number of Sub-resources level"
    override val id = "147"
    override val severity = Severity.SHOULD
    private val DESC = "Number of sub-resources should not exceed 3"
    private val subresourcesLimit = rulesConfig.getConfig(name).getInt("subresources_limit")

    @Check(severity = Severity.SHOULD)
    fun validate(swagger: Swagger): Violation? {
        val paths = swagger.paths.orEmpty().keys.filter { path ->
            path.split("/").filter { it.isNotEmpty() && !PatternUtil.isPathVariable(it) }.size - 1 > subresourcesLimit
        }
        return if (paths.isNotEmpty()) Violation(DESC, paths) else null
    }
}
