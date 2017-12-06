package de.zalando.zally.rule.zalando

import de.zalando.zally.rule.AbstractRule
import de.zalando.zally.rule.api.Check
import de.zalando.zally.rule.api.Severity
import de.zalando.zally.rule.api.Violation
import de.zalando.zally.util.PatternUtil.isPathVariable
import io.swagger.models.Swagger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class EverySecondPathLevelParameterRule(@Autowired ruleSet: ZalandoRuleSet) : AbstractRule(ruleSet) {
    override val title = "Every Second Path Level To Be Parameter"
    override val id = "143"
    override val severity = Severity.MUST
    private val DESCRIPTION = "Every second path level must be a path parameter"

    @Check(severity = Severity.MUST)
    fun validate(swagger: Swagger): Violation? {
        val paths = swagger.paths.orEmpty().keys.filterNot {
            val pathSegments = it.split("/").filter { it.isNotEmpty() }
            pathSegments.filterIndexed { i, segment -> isPathVariable(segment) == (i % 2 == 0) }.isEmpty()
        }
        return if (paths.isNotEmpty()) Violation(DESCRIPTION, paths) else null
    }
}
