/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.client.dataframe.transforms.pivot;

import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.xcontent.ConstructingObjectParser;
import org.elasticsearch.common.xcontent.ToXContentObject;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;

import java.io.IOException;
import java.util.Objects;

import static org.elasticsearch.common.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.common.xcontent.ConstructingObjectParser.optionalConstructorArg;

public class PivotConfig implements ToXContentObject {

    private static final ParseField GROUP_BY = new ParseField("group_by");
    private static final ParseField AGGREGATIONS = new ParseField("aggregations");

    private final GroupConfig groups;
    private final AggregationConfig aggregationConfig;

    private static final ConstructingObjectParser<PivotConfig, Void> PARSER = new ConstructingObjectParser<>("pivot_config", true,
                args -> new PivotConfig((GroupConfig) args[0], (AggregationConfig) args[1]));

    static {
        PARSER.declareObject(constructorArg(), (p, c) -> (GroupConfig.fromXContent(p)), GROUP_BY);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> AggregationConfig.fromXContent(p), AGGREGATIONS);
    }

    public static PivotConfig fromXContent(final XContentParser parser) {
        return PARSER.apply(parser, null);
    }

    public PivotConfig(GroupConfig groups, final AggregationConfig aggregationConfig) {
        this.groups = groups;
        this.aggregationConfig = aggregationConfig;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(GROUP_BY.getPreferredName(), groups);
        builder.field(AGGREGATIONS.getPreferredName(), aggregationConfig);
        builder.endObject();
        return builder;
    }

    public AggregationConfig getAggregationConfig() {
        return aggregationConfig;
    }

    public GroupConfig getGroupConfig() {
        return groups;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        final PivotConfig that = (PivotConfig) other;

        return Objects.equals(this.groups, that.groups) && Objects.equals(this.aggregationConfig, that.aggregationConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groups, aggregationConfig);
    }

    public boolean isValid() {
        return groups.isValid() && aggregationConfig.isValid();
    }
}
