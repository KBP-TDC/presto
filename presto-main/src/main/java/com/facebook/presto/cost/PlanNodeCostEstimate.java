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

import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.isNaN;

public class PlanNodeCostEstimate
{
    public static final PlanNodeCostEstimate INFINITE_COST = new PlanNodeCostEstimate(POSITIVE_INFINITY, POSITIVE_INFINITY, POSITIVE_INFINITY);
    public static final PlanNodeCostEstimate UNKNOWN_COST = PlanNodeCostEstimate.builder().build();
    public static final PlanNodeCostEstimate ZERO_COST = new PlanNodeCostEstimate(0, 0, 0);

    private final double cpuCost;
    private final double memoryCost;
    private final double networkCost;

    private PlanNodeCostEstimate(double cpuCost, double memoryCost, double networkCost)
    {
        checkArgument(isNaN(cpuCost) || cpuCost >= 0, "cpuCost cannot be negative");
        checkArgument(isNaN(memoryCost) || memoryCost >= 0, "memoryCost cannot be negative");
        checkArgument(isNaN(networkCost) || networkCost >= 0, "networkCost cannot be negative");
        this.cpuCost = cpuCost;
        this.memoryCost = memoryCost;
        this.networkCost = networkCost;
    }

    /**
     * Returns CPU component of the cost. Unknown value is represented by {@link Double#NaN}
     */
    public double getCpuCost()
    {
        return cpuCost;
    }

    /**
     * Returns memory component of the cost. Unknown value is represented by {@link Double#NaN}
     */
    public double getMemoryCost()
    {
        return memoryCost;
    }

    /**
     * Returns network component of the cost. Unknown value is represented by {@link Double#NaN}
     */
    public double getNetworkCost()
    {
        return networkCost;
    }

    /**
     * Returns true if this cost has unknown components.
     */
    public boolean hasUnknownComponents()
    {
        return isNaN(cpuCost) || isNaN(memoryCost) || isNaN(networkCost);
    }

    @Override
    public String toString()
    {
        return toStringHelper(this)
                .add("cpuCost", cpuCost)
                .add("memoryCost", memoryCost)
                .add("networkCost", networkCost)
                .toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlanNodeCostEstimate that = (PlanNodeCostEstimate) o;
        return Double.compare(that.cpuCost, cpuCost) == 0 &&
                Double.compare(that.memoryCost, memoryCost) == 0 &&
                Double.compare(that.networkCost, networkCost) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(cpuCost, memoryCost, networkCost);
    }

    public PlanNodeCostEstimate add(PlanNodeCostEstimate other)
    {
        return new PlanNodeCostEstimate(
                cpuCost + other.cpuCost,
                memoryCost + other.memoryCost,
                networkCost + other.networkCost);
    }

    public static PlanNodeCostEstimate networkCost(double networkCost)
    {
        return builder().setNetworkCost(networkCost).build();
    }

    public static PlanNodeCostEstimate cpuCost(double cpuCost)
    {
        return builder().setCpuCost(cpuCost).build();
    }

    public static PlanNodeCostEstimate memoryCost(double memoryCost)
    {
        return builder().setMemoryCost(memoryCost).build();
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static final class Builder
    {
        private double cpuCost = NaN;
        private double memoryCost = NaN;
        private double networkCost = NaN;

        public Builder setFrom(PlanNodeCostEstimate otherStatistics)
        {
            return setCpuCost(otherStatistics.getCpuCost())
                    .setMemoryCost(otherStatistics.getMemoryCost())
                    .setNetworkCost(otherStatistics.getNetworkCost());
        }

        public Builder setCpuCost(double cpuCost)
        {
            this.cpuCost = cpuCost;
            return this;
        }

        public Builder setMemoryCost(double memoryCost)
        {
            this.memoryCost = memoryCost;
            return this;
        }

        public Builder setNetworkCost(double networkCost)
        {
            this.networkCost = networkCost;
            return this;
        }

        public PlanNodeCostEstimate build()
        {
            return new PlanNodeCostEstimate(cpuCost, memoryCost, networkCost);
        }
    }
}
