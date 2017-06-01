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

package com.facebook.presto.cost;

import com.facebook.presto.Session;
import com.facebook.presto.metadata.InternalNodeManager;
import com.facebook.presto.spi.type.Type;
import com.facebook.presto.sql.planner.Symbol;
import com.facebook.presto.sql.planner.iterative.Lookup;
import com.facebook.presto.sql.planner.plan.PlanNode;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;

import java.util.Map;

import static com.facebook.presto.cost.PlanNodeCostEstimate.UNKNOWN_COST;
import static java.util.Objects.requireNonNull;

/**
 * This is a wrapper class around CostCalculator that estimates ExchangeNodes cost.
 */
@ThreadSafe
public class CostCalculatorWithEstimatedExchanges
        implements CostCalculator
{
    private final CostCalculator costCalculator;
    private final int numberOfNodes;

    @Inject
    public CostCalculatorWithEstimatedExchanges(CostCalculator costCalculator, InternalNodeManager nodeManager)
    {
        this(costCalculator, nodeManager.getAllNodes().getActiveNodes().size());
    }

    public CostCalculatorWithEstimatedExchanges(CostCalculator costCalculator, int numberOfNodes)
    {
        this.costCalculator = requireNonNull(costCalculator, "costCalculator is null");
        this.numberOfNodes = numberOfNodes;
    }

    @Override
    public PlanNodeCostEstimate calculateCost(PlanNode planNode, Lookup lookup, Session session, Map<Symbol, Type> types)
    {
        return UNKNOWN_COST;
    }
}
