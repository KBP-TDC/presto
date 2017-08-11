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
package com.facebook.presto.sql.analyzer;

import com.google.common.collect.ImmutableMap;
import io.airlift.configuration.testing.ConfigAssertions;
import io.airlift.units.DataSize;
import io.airlift.units.Duration;
import org.testng.annotations.Test;

import java.util.Map;

import static com.facebook.presto.sql.analyzer.FeaturesConfig.JoinDistributionType.REPARTITIONED;
import static com.facebook.presto.sql.analyzer.FeaturesConfig.JoinDistributionType.REPLICATED;
import static com.facebook.presto.sql.analyzer.FeaturesConfig.JoinReorderingStrategy.ELIMINATE_CROSS_JOINS;
import static com.facebook.presto.sql.analyzer.FeaturesConfig.JoinReorderingStrategy.NONE;
import static com.facebook.presto.sql.analyzer.RegexLibrary.JONI;
import static com.facebook.presto.sql.analyzer.RegexLibrary.RE2J;
import static io.airlift.configuration.testing.ConfigAssertions.assertDeprecatedEquivalence;
import static io.airlift.configuration.testing.ConfigAssertions.assertFullMapping;
import static io.airlift.configuration.testing.ConfigAssertions.assertRecordedDefaults;
import static io.airlift.units.DataSize.Unit.KILOBYTE;
import static io.airlift.units.DataSize.Unit.MEGABYTE;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class TestFeaturesConfig
{
    @Test
    public void testDefaults()
    {
        assertRecordedDefaults(ConfigAssertions.recordDefaults(FeaturesConfig.class)
                .setCpuCostWeight(75)
                .setMemoryCostWeight(10)
                .setNetworkCostWeight(15)
                .setResourceGroupsEnabled(false)
                .setDistributedIndexJoinsEnabled(false)
                .setJoinDistributionType(REPARTITIONED)
                .setFastInequalityJoins(true)
                .setColocatedJoinsEnabled(false)
                .setJoinReorderingStrategy(ELIMINATE_CROSS_JOINS)
                .setRedistributeWrites(true)
                .setOptimizeMetadataQueries(false)
                .setOptimizeHashGeneration(true)
                .setOptimizeSingleDistinct(true)
                .setPushTableWriteThroughUnion(true)
                .setDictionaryAggregation(false)
                .setLegacyArrayAgg(false)
                .setLegacyMapSubscript(false)
                .setRegexLibrary(JONI)
                .setRe2JDfaStatesLimit(Integer.MAX_VALUE)
                .setRe2JDfaRetries(5)
                .setSpillEnabled(false)
                .setAggregationOperatorUnspillMemoryLimit(DataSize.valueOf("4MB"))
                .setSpillerSpillPaths("")
                .setSpillerThreads(4)
                .setSpillMaxUsedSpaceThreshold(0.9)
                .setMemoryRevokingThreshold(0.9)
                .setMemoryRevokingTarget(0.5)
                .setOptimizeMixedDistinctAggregations(false)
                .setLegacyOrderBy(false)
                .setIterativeOptimizerEnabled(true)
                .setLegacyTimestamp(false)
                .setIterativeOptimizerTimeout(new Duration(3, MINUTES))
                .setExchangeCompressionEnabled(false)
                .setEnableIntermediateAggregations(false)
                .setPushAggregationThroughJoin(true)
                .setUseNewStatsCalculator(true)
                .setParseDecimalLiteralsAsDouble(false)
                .setDistributedSortEnabled(false)
                .setRedistributeSort(true)
                .setFilterAndProjectMinOutputPageSize(new DataSize(25, KILOBYTE))
                .setFilterAndProjectMinOutputPageRowCount(256));
    }

    @Test
    public void testExplicitPropertyMappings()
    {
        Map<String, String> propertiesLegacy = new ImmutableMap.Builder<String, String>()
                .put("cpu-cost-weight", "0.4")
                .put("memory-cost-weight", "0.3")
                .put("network-cost-weight", "0.2")
                .put("experimental.resource-groups-enabled", "true")
                .put("experimental.iterative-optimizer-enabled", "false")
                .put("experimental.iterative-optimizer-timeout", "10s")
                .put("deprecated.legacy-array-agg", "true")
                .put("deprecated.legacy-order-by", "true")
                .put("deprecated.legacy-map-subscript", "true")
                .put("distributed-index-joins-enabled", "true")
                .put("join-distribution-type", "REPLICATED")
                .put("fast-inequality-joins", "false")
                .put("colocated-joins-enabled", "true")
                .put("optimizer.join-reordering-strategy", "NONE")
                .put("redistribute-writes", "false")
                .put("optimizer.optimize-metadata-queries", "true")
                .put("optimizer.optimize-hash-generation", "false")
                .put("optimizer.optimize-single-distinct", "false")
                .put("optimizer.optimize-mixed-distinct-aggregations", "true")
                .put("optimizer.push-table-write-through-union", "false")
                .put("optimizer.dictionary-aggregation", "true")
                .put("optimizer.push-aggregation-through-join", "false")
                .put("regex-library", "RE2J")
                .put("re2j.dfa-states-limit", "42")
                .put("re2j.dfa-retries", "42")
                .put("beta.spill-enabled", "true")
                .put("beta.aggregation-operator-unspill-memory-limit", "100MB")
                .put("beta.spiller-spill-path", "/tmp/custom/spill/path1,/tmp/custom/spill/path2")
                .put("beta.spiller-threads", "42")
                .put("beta.spiller-max-used-space-threshold", "0.8")
                .put("experimental.memory-revoking-threshold", "0.2")
                .put("experimental.memory-revoking-target", "0.8")
                .put("exchange.compression-enabled", "true")
                .put("deprecated.legacy-timestamp", "true")
                .put("optimizer.enable-intermediate-aggregations", "true")
                .put("experimental.use-new-stats-calculator", "false")
                .put("deprecated.parse-decimal-literals-as-double", "true")
                .put("experimental.distributed-sort", "true")
                .put("experimental.redistribute-sort", "false")
                .put("experimental.filter-and-project-min-output-page-size", "10MB")
                .put("experimental.filter-and-project-min-output-page-row-count", "2048")
                .build();
        Map<String, String> properties = new ImmutableMap.Builder<String, String>()
                .put("cpu-cost-weight", "0.4")
                .put("memory-cost-weight", "0.3")
                .put("network-cost-weight", "0.2")
                .put("experimental.resource-groups-enabled", "true")
                .put("experimental.iterative-optimizer-enabled", "false")
                .put("experimental.iterative-optimizer-timeout", "10s")
                .put("deprecated.legacy-array-agg", "true")
                .put("deprecated.legacy-order-by", "true")
                .put("deprecated.legacy-map-subscript", "true")
                .put("distributed-index-joins-enabled", "true")
                .put("join-distribution-type", "REPLICATED")
                .put("fast-inequality-joins", "false")
                .put("colocated-joins-enabled", "true")
                .put("optimizer.join-reordering-strategy", "NONE")
                .put("redistribute-writes", "false")
                .put("optimizer.optimize-metadata-queries", "true")
                .put("optimizer.optimize-hash-generation", "false")
                .put("optimizer.optimize-single-distinct", "false")
                .put("optimizer.optimize-mixed-distinct-aggregations", "true")
                .put("optimizer.push-table-write-through-union", "false")
                .put("optimizer.dictionary-aggregation", "true")
                .put("optimizer.push-aggregation-through-join", "false")
                .put("regex-library", "RE2J")
                .put("re2j.dfa-states-limit", "42")
                .put("re2j.dfa-retries", "42")
                .put("beta.spill-enabled", "true")
                .put("beta.aggregation-operator-unspill-memory-limit", "100MB")
                .put("beta.spiller-spill-path", "/tmp/custom/spill/path1,/tmp/custom/spill/path2")
                .put("beta.spiller-threads", "42")
                .put("beta.spiller-max-used-space-threshold", "0.8")
                .put("experimental.memory-revoking-threshold", "0.2")
                .put("experimental.memory-revoking-target", "0.8")
                .put("exchange.compression-enabled", "true")
                .put("deprecated.legacy-timestamp", "true")
                .put("optimizer.enable-intermediate-aggregations", "true")
                .put("experimental.use-new-stats-calculator", "false")
                .put("deprecated.parse-decimal-literals-as-double", "true")
                .put("experimental.distributed-sort", "true")
                .put("experimental.redistribute-sort", "false")
                .put("experimental.filter-and-project-min-output-page-size", "10MB")
                .put("experimental.filter-and-project-min-output-page-row-count", "2048")
                .build();

        FeaturesConfig expected = new FeaturesConfig()
                .setCpuCostWeight(0.4)
                .setMemoryCostWeight(0.3)
                .setNetworkCostWeight(0.2)
                .setResourceGroupsEnabled(true)
                .setIterativeOptimizerEnabled(false)
                .setIterativeOptimizerTimeout(new Duration(10, SECONDS))
                .setDistributedIndexJoinsEnabled(true)
                .setJoinDistributionType(REPLICATED)
                .setFastInequalityJoins(false)
                .setColocatedJoinsEnabled(true)
                .setJoinReorderingStrategy(NONE)
                .setRedistributeWrites(false)
                .setOptimizeMetadataQueries(true)
                .setOptimizeHashGeneration(false)
                .setOptimizeSingleDistinct(false)
                .setOptimizeMixedDistinctAggregations(true)
                .setPushTableWriteThroughUnion(false)
                .setDictionaryAggregation(true)
                .setPushAggregationThroughJoin(false)
                .setLegacyArrayAgg(true)
                .setLegacyMapSubscript(true)
                .setRegexLibrary(RE2J)
                .setRe2JDfaStatesLimit(42)
                .setRe2JDfaRetries(42)
                .setSpillEnabled(true)
                .setAggregationOperatorUnspillMemoryLimit(DataSize.valueOf("100MB"))
                .setSpillerSpillPaths("/tmp/custom/spill/path1,/tmp/custom/spill/path2")
                .setSpillerThreads(42)
                .setSpillMaxUsedSpaceThreshold(0.8)
                .setMemoryRevokingThreshold(0.2)
                .setMemoryRevokingTarget(0.8)
                .setLegacyOrderBy(true)
                .setLegacyTimestamp(true)
                .setExchangeCompressionEnabled(true)
                .setEnableIntermediateAggregations(true)
                .setUseNewStatsCalculator(false)
                .setParseDecimalLiteralsAsDouble(true)
                .setDistributedSortEnabled(true)
                .setRedistributeSort(false)
                .setFilterAndProjectMinOutputPageSize(new DataSize(10, MEGABYTE))
                .setFilterAndProjectMinOutputPageRowCount(2048);

        assertFullMapping(properties, expected);
        assertDeprecatedEquivalence(FeaturesConfig.class, properties, propertiesLegacy);
    }
}
