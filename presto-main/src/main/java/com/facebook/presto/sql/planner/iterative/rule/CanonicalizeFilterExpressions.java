/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.sql.planner.iterative.rule;

import com.facebook.presto.matching.Pattern;
import com.facebook.presto.sql.planner.iterative.Rule;
import com.facebook.presto.sql.planner.plan.FilterNode;
import com.facebook.presto.sql.planner.plan.PlanNode;
import com.facebook.presto.sql.tree.Expression;

import java.util.Optional;

import static com.facebook.presto.sql.planner.optimizations.CanonicalizeExpressions.canonicalizeExpression;
import static com.facebook.presto.sql.planner.plan.Patterns.filter;

public class CanonicalizeFilterExpressions
        implements Rule
{
    private static final Pattern PATTERN = filter();

    @Override
    public Pattern getPattern()
    {
        return PATTERN;
    }

    @Override
    public Optional<PlanNode> apply(PlanNode node, Context context)
    {
        FilterNode filterNode = (FilterNode) node;

        Expression canonicalized = canonicalizeExpression(filterNode.getPredicate());

        if (canonicalized.equals(filterNode.getPredicate())) {
            return Optional.empty();
        }

        return Optional.of(new FilterNode(node.getId(), filterNode.getSource(), canonicalized));
    }
}
