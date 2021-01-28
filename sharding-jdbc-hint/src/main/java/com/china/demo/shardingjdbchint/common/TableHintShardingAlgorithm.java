package com.china.demo.shardingjdbchint.common;

import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.hint.HintShardingAlgorithm;

import java.util.Collection;

/**
 * TableHintShardingAlgorithm
 *
 * @author shanmin.zhang
 * @description:
 * @date 2020/10/29
 */

public class TableHintShardingAlgorithm implements HintShardingAlgorithm {
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ShardingValue shardingValue) {
        return null;
    }
}
